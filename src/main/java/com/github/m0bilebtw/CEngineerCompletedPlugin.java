package com.github.m0bilebtw;

import com.github.m0bilebtw.announce.AnnouncementTriggers;
import com.github.m0bilebtw.eastereggs.EasterEggTriggers;
import com.github.m0bilebtw.player.CEngineerPlayer;
import com.github.m0bilebtw.player.LoggedInState;
import com.github.m0bilebtw.qol.QualityOfLifeTriggers;
import com.github.m0bilebtw.sound.Sound;
import com.github.m0bilebtw.sound.SoundEngine;
import com.github.m0bilebtw.sound.SoundFileManager;
import com.github.m0bilebtw.trolls.TrollTriggers;
import com.google.inject.Provides;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.Experience;
import net.runelite.api.GameState;
import net.runelite.api.Skill;
import net.runelite.api.Varbits;
import net.runelite.api.annotations.Varbit;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.StatChanged;
import net.runelite.api.events.VarbitChanged;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.chat.ChatColorType;
import net.runelite.client.chat.ChatMessageBuilder;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.chat.QueuedMessage;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import okhttp3.OkHttpClient;

import javax.inject.Inject;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;
import java.util.regex.Pattern;

@Slf4j
@PluginDescriptor(
        name = "C Engineer: Completed",
        description = "C Engineer announces when you complete an achievement",
        tags = {"c engineer", "stats", "levels", "quests", "diary", "announce"}
)

public class CEngineerCompletedPlugin extends Plugin {
    @Inject
    private Client client;

    @Getter(AccessLevel.PACKAGE)
    @Inject
    private ClientThread clientThread;

    @Inject
    private ChatMessageManager chatMessageManager;

    @Inject
    private CEngineerCompletedConfig config;

    @Inject
    private ScheduledExecutorService executor;

    @Inject
    private OkHttpClient okHttpClient;

    @Inject
    private EventBus eventBus;

    @Inject
    private SoundEngine soundEngine;

    @Inject
    private CEngineerPlayer cEngineer;

    @Inject
    private AnnouncementTriggers announcementTriggers;

    @Inject
    private EasterEggTriggers easterEggTriggers;

    @Inject
    private TrollTriggers trollTriggers;

    @Inject
    private QualityOfLifeTriggers qolTriggers;

    @Inject
    private LoggedInState loggedInState;

    private final int[] varbitsAchievementDiaries = {
            Varbits.DIARY_ARDOUGNE_EASY,   Varbits.DIARY_ARDOUGNE_MEDIUM,   Varbits.DIARY_ARDOUGNE_HARD,   Varbits.DIARY_ARDOUGNE_ELITE,
            Varbits.DIARY_DESERT_EASY, 	   Varbits.DIARY_DESERT_MEDIUM, 	Varbits.DIARY_DESERT_HARD, 	   Varbits.DIARY_DESERT_ELITE,
            Varbits.DIARY_FALADOR_EASY,    Varbits.DIARY_FALADOR_MEDIUM,    Varbits.DIARY_FALADOR_HARD,    Varbits.DIARY_FALADOR_ELITE,
            Varbits.DIARY_KANDARIN_EASY,   Varbits.DIARY_KANDARIN_MEDIUM,   Varbits.DIARY_KANDARIN_HARD,   Varbits.DIARY_KANDARIN_ELITE,
            Varbits.DIARY_KARAMJA_EASY,    Varbits.DIARY_KARAMJA_MEDIUM,    Varbits.DIARY_KARAMJA_HARD,    Varbits.DIARY_KARAMJA_ELITE,
            Varbits.DIARY_KOUREND_EASY,    Varbits.DIARY_KOUREND_MEDIUM,    Varbits.DIARY_KOUREND_HARD,    Varbits.DIARY_KOUREND_ELITE,
            Varbits.DIARY_LUMBRIDGE_EASY,  Varbits.DIARY_LUMBRIDGE_MEDIUM,  Varbits.DIARY_LUMBRIDGE_HARD,  Varbits.DIARY_LUMBRIDGE_ELITE,
            Varbits.DIARY_MORYTANIA_EASY,  Varbits.DIARY_MORYTANIA_MEDIUM,  Varbits.DIARY_MORYTANIA_HARD,  Varbits.DIARY_MORYTANIA_ELITE,
            Varbits.DIARY_VARROCK_EASY,    Varbits.DIARY_VARROCK_MEDIUM,    Varbits.DIARY_VARROCK_HARD,    Varbits.DIARY_VARROCK_ELITE,
            Varbits.DIARY_WESTERN_EASY,    Varbits.DIARY_WESTERN_MEDIUM,    Varbits.DIARY_WESTERN_HARD,    Varbits.DIARY_WESTERN_ELITE,
            Varbits.DIARY_WILDERNESS_EASY, Varbits.DIARY_WILDERNESS_MEDIUM, Varbits.DIARY_WILDERNESS_HARD, Varbits.DIARY_WILDERNESS_ELITE
    };

    private static final Pattern COLLECTION_LOG_ITEM_REGEX = Pattern.compile("New item added to your collection log:.*");
    private static final Pattern COMBAT_TASK_REGEX = Pattern.compile("Congratulations, you've completed an? (?:\\w+) combat task:.*");
    private static final Pattern QUEST_REGEX = Pattern.compile("Congratulations, you've completed a quest:.*");

    private static final Set<Integer> badCollectionLogNotificationSettingValues = Set.of(0, 2);

    private final Map<Skill, Integer> oldExperience = new EnumMap<>(Skill.class);
    private final Map<Integer, Integer> oldAchievementDiaries = new HashMap<>();

    private int lastColLogSettingWarning = -1;

    @Override
    protected void startUp() throws Exception {
        eventBus.register(cEngineer);
        eventBus.register(announcementTriggers);
        eventBus.register(easterEggTriggers);
        eventBus.register(trollTriggers);
        eventBus.register(qolTriggers);
        eventBus.register(loggedInState);
        loggedInState.setForCurrentGameState(client.getGameState());

        clientThread.invoke(this::setupOldMaps);
        executor.submit(() -> {
            SoundFileManager.ensureDownloadDirectoryExists();
            SoundFileManager.downloadAllMissingSounds(okHttpClient, config.downloadStreamerTrolls());
        });
    }

    @Override
    protected void shutDown() throws Exception {
        eventBus.unregister(cEngineer);
        eventBus.unregister(announcementTriggers);
        eventBus.unregister(easterEggTriggers);
        eventBus.unregister(trollTriggers);
        eventBus.unregister(qolTriggers);
        eventBus.unregister(loggedInState);

        oldExperience.clear();
        oldAchievementDiaries.clear();
        soundEngine.close();
    }

    private void setupOldMaps() {
        if (client.getGameState() != GameState.LOGGED_IN) {
            oldExperience.clear();
            oldAchievementDiaries.clear();
        } else {
            for (final Skill skill : Skill.values()) {
                oldExperience.put(skill, client.getSkillExperience(skill));
            }
            for (@Varbit int diary : varbitsAchievementDiaries) {
                int value = client.getVarbitValue(diary);
                oldAchievementDiaries.put(diary, value);
            }
        }
    }

    @Subscribe
    public void onGameStateChanged(GameStateChanged event) {
        switch (event.getGameState()) {
            case LOGIN_SCREEN:
            case HOPPING:
            case LOGGING_IN:
            case LOGIN_SCREEN_AUTHENTICATOR:
                oldExperience.clear();
                oldAchievementDiaries.clear();
            case CONNECTION_LOST:
                // set to -1 here in-case of race condition with varbits changing before this handler is called
                // when game state becomes LOGGED_IN
                lastColLogSettingWarning = client.getTickCount(); // avoid warning during DC
                break;
            default:
                break;
        }
    }

    @Subscribe
    public void onStatChanged(StatChanged statChanged) {
        final Skill skill = statChanged.getSkill();

        // Modified from Nightfirecat's virtual level ups plugin as this info isn't (yet?) built in to statChanged event
        final int xpAfter = client.getSkillExperience(skill);
        final int levelAfter = Experience.getLevelForXp(xpAfter);
        final int xpBefore = oldExperience.getOrDefault(skill, -1);
        final int levelBefore = xpBefore == -1 ? -1 : Experience.getLevelForXp(xpBefore);

        oldExperience.put(skill, xpAfter);

        // Do not proceed if any of the following are true:
        //  * xpBefore == -1              (don't fire when first setting new known value)
        //  * xpAfter <= xpBefore         (do not allow 200m -> 200m exp drops)
        //  * levelBefore >= levelAfter   (stop if if we're not actually reaching a new level)
        //  * levelAfter > MAX_REAL_LEVEL && config says don't include virtual (level is virtual and config ignores virtual)
        if (xpBefore == -1 || xpAfter <= xpBefore || levelBefore >= levelAfter ||
                (levelAfter > Experience.MAX_REAL_LEVEL && !config.announceLevelUpIncludesVirtual())) {
            return;
        }

        // If we get here, 'skill' was leveled up!
        if (config.announceLevelUp()) {
            cEngineer.sendChatIfEnabled("Level up: completed.");
            soundEngine.playClip(Sound.LEVEL_UP, executor);
        }
    }

    @Subscribe
    public void onChatMessage(ChatMessage chatMessage) {
        if (chatMessage.getType() != ChatMessageType.GAMEMESSAGE && chatMessage.getType() != ChatMessageType.SPAM)
            return;

        if (config.announceCollectionLog() && COLLECTION_LOG_ITEM_REGEX.matcher(chatMessage.getMessage()).matches()) {
            cEngineer.sendChatIfEnabled("Collection log slot: completed.");
            soundEngine.playClip(Sound.COLLECTION_LOG_SLOT, executor);

        } else if (config.announceQuestCompletion() && QUEST_REGEX.matcher(chatMessage.getMessage()).matches()) {
            cEngineer.sendChatIfEnabled("Quest: completed.");
            soundEngine.playClip(Sound.QUEST, executor);

        } else if (config.announceCombatAchievement() && COMBAT_TASK_REGEX.matcher(chatMessage.getMessage()).matches()) {
            cEngineer.sendChatIfEnabled("Combat task: completed.");
            soundEngine.playClip(Sound.COMBAT_TASK, executor);
        }
    }

    private void checkAndWarnForCollectionLogNotificationSetting(int newVarbitValue) {
        if (!config.announceCollectionLog())
            return;

        if (loggedInState.isLoggedOut())
            return;

        if (badCollectionLogNotificationSettingValues.contains(newVarbitValue)) {
            if (lastColLogSettingWarning == -1 || client.getTickCount() - lastColLogSettingWarning > 16) {
                lastColLogSettingWarning = client.getTickCount();
                sendHighlightedMessage("Please enable \"Collection log - New addition notification\" in your game settings for C Engineer to know when to announce it! (The chat message one, pop-up doesn't matter)");
            }
        }
    }

    @Subscribe
    public void onVarbitChanged(VarbitChanged varbitChanged) {
        if (varbitChanged.getVarbitId() == Varbits.COLLECTION_LOG_NOTIFICATION) {
            checkAndWarnForCollectionLogNotificationSetting(varbitChanged.getValue());
        }

        // As we can't listen to specific varbits, we get a tonne of events BEFORE the game has even set the player's
        // diary varbits correctly, meaning it assumes every diary is on 0, then suddenly every diary that has been
        // completed gets updated to the true value and tricks the plugin into thinking they only just finished it.
        // To avoid this behaviour, we make sure the current tick count is sufficiently high that we've already passed
        // the initial wave of varbit changes from logging in.
        if (loggedInState.isLoggedOut() || loggedInState.onlyJustLoggedIn(8)) {
            return; // Ignoring varbit change as only just logged in
        }

        // Apparently I can't check if it's a particular varbit using the names from Varbits enum, so this is the way
        for (@Varbit int diary : varbitsAchievementDiaries) {
            int newValue = client.getVarbitValue(diary);
            int previousValue = oldAchievementDiaries.getOrDefault(diary, -1);
            oldAchievementDiaries.put(diary, newValue);
            if (config.announceAchievementDiary() && previousValue != -1 && previousValue != newValue && isAchievementDiaryCompleted(diary, newValue)) {
                // value was not unknown (we know the previous value), value has changed, and value indicates diary is completed now
                cEngineer.sendChatIfEnabled("Achievement diary: completed.");
                soundEngine.playClip(Sound.ACHIEVEMENT_DIARY, executor);
            }
        }
    }

    private boolean isAchievementDiaryCompleted(int diary, int value) {
        switch (diary) {
            case Varbits.DIARY_KARAMJA_EASY:
            case Varbits.DIARY_KARAMJA_MEDIUM:
            case Varbits.DIARY_KARAMJA_HARD:
                return value == 2; // jagex, why?
            default:
                return value == 1;
        }
    }

    @Provides
    CEngineerCompletedConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(CEngineerCompletedConfig.class);
    }

    private void sendHighlightedMessage(String message) {
        String highlightedMessage = new ChatMessageBuilder()
                .append(ChatColorType.HIGHLIGHT)
                .append(message)
                .build();

        chatMessageManager.queue(QueuedMessage.builder()
                .type(ChatMessageType.CONSOLE)
                .runeLiteFormattedMessage(highlightedMessage)
                .build());
    }

    @Subscribe
    public void onConfigChanged(ConfigChanged event) {
        if (CEngineerCompletedConfig.GROUP.equals(event.getGroup())) {

// Disabled - fires continuously while spinner arrow is held - when this is avoidable, can enable
//			if ("announcementVolume".equals(event.getKey())) {
//				soundEngine.playClip(Sound.LEVEL_UP);
//			}

            if ("announceCollectionLog".equals(event.getKey())) {
                clientThread.invokeLater(() ->
                        checkAndWarnForCollectionLogNotificationSetting(client.getVarbitValue(Varbits.COLLECTION_LOG_NOTIFICATION)));
            }
        }
    }
}
