package com.github.m0bilebtw;

import com.github.m0bilebtw.emote.EmoteTriggers;
import com.google.inject.Provides;

import java.time.Duration;
import java.util.HashMap;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;

import static com.github.m0bilebtw.Sound.BOND_OFFER_SOUNDS;
import static com.github.m0bilebtw.Sound.SNOWBALL_SOUNDS;
import static com.github.m0bilebtw.Sound.STAT_SPY_SOUNDS;
import static net.runelite.api.Varbits.DIARY_KARAMJA_EASY;
import static net.runelite.api.Varbits.DIARY_KARAMJA_HARD;
import static net.runelite.api.Varbits.DIARY_KARAMJA_MEDIUM;
import net.runelite.api.annotations.Varbit;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldArea;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.*;
import net.runelite.api.widgets.ComponentID;
import net.runelite.api.widgets.InterfaceID;
import net.runelite.api.widgets.Widget;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.chat.ChatColorType;
import net.runelite.client.chat.ChatMessageBuilder;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.chat.QueuedMessage;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.util.Text;
import okhttp3.OkHttpClient;

import javax.inject.Inject;
import java.util.EnumMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@PluginDescriptor(
	name = "C Engineer: Completed",
	description = "C Engineer announces when you complete an achievement",
	tags = {"c engineer", "stats", "levels", "quests", "diary", "announce"}
)

public class CEngineerCompletedPlugin extends Plugin
{
	@Inject
	private Client client;

	@Getter(AccessLevel.PACKAGE)
	@Inject
	private ClientThread clientThread;

	@Inject
	private ChatMessageManager chatMessageManager;

	@Inject
	private SoundEngine soundEngine;

	@Inject
	private CEngineerCompletedConfig config;

	@Inject
	private ScheduledExecutorService executor;

	@Inject
	private OkHttpClient okHttpClient;

	@Inject
	private EmoteTriggers emoteTriggers;

	private final int[] varbitsAchievementDiaries = {
			Varbits.DIARY_ARDOUGNE_EASY, Varbits.DIARY_ARDOUGNE_MEDIUM, Varbits.DIARY_ARDOUGNE_HARD, Varbits.DIARY_ARDOUGNE_ELITE,
			Varbits.DIARY_DESERT_EASY, Varbits.DIARY_DESERT_MEDIUM, Varbits.DIARY_DESERT_HARD, Varbits.DIARY_DESERT_ELITE,
			Varbits.DIARY_FALADOR_EASY, Varbits.DIARY_FALADOR_MEDIUM, Varbits.DIARY_FALADOR_HARD, Varbits.DIARY_FALADOR_ELITE,
			Varbits.DIARY_KANDARIN_EASY, Varbits.DIARY_KANDARIN_MEDIUM, Varbits.DIARY_KANDARIN_HARD, Varbits.DIARY_KANDARIN_ELITE,
			DIARY_KARAMJA_EASY, DIARY_KARAMJA_MEDIUM, DIARY_KARAMJA_HARD, Varbits.DIARY_KARAMJA_ELITE,
			Varbits.DIARY_KOUREND_EASY, Varbits.DIARY_KOUREND_MEDIUM, Varbits.DIARY_KOUREND_HARD, Varbits.DIARY_KOUREND_ELITE,
			Varbits.DIARY_LUMBRIDGE_EASY, Varbits.DIARY_LUMBRIDGE_MEDIUM, Varbits.DIARY_LUMBRIDGE_HARD, Varbits.DIARY_LUMBRIDGE_ELITE,
			Varbits.DIARY_MORYTANIA_EASY, Varbits.DIARY_MORYTANIA_MEDIUM, Varbits.DIARY_MORYTANIA_HARD, Varbits.DIARY_MORYTANIA_ELITE,
			Varbits.DIARY_VARROCK_EASY, Varbits.DIARY_VARROCK_MEDIUM, Varbits.DIARY_VARROCK_HARD, Varbits.DIARY_VARROCK_ELITE,
			Varbits.DIARY_WESTERN_EASY, Varbits.DIARY_WESTERN_MEDIUM, Varbits.DIARY_WESTERN_HARD, Varbits.DIARY_WESTERN_ELITE,
			Varbits.DIARY_WILDERNESS_EASY, Varbits.DIARY_WILDERNESS_MEDIUM, Varbits.DIARY_WILDERNESS_HARD, Varbits.DIARY_WILDERNESS_ELITE
	};

	// Killcount and new pb patterns from runelite/ChatCommandsPlugin
	private static final String ZULRAH = "Zulrah";
	private static final String C_ENGINEER = "C Engineer";
	private static final String SKILL_SPECS = "Skill Specs";
	private static final Pattern KILLCOUNT_PATTERN = Pattern.compile("Your (?:completion count for |subdued |completed )?(.+?) (?:(?:kill|harvest|lap|completion) )?(?:count )?is: <col=ff0000>(\\d+)</col>");
	private static final Pattern NEW_PB_PATTERN = Pattern.compile("(?i)(?:(?:Fight |Lap |Challenge |Corrupted challenge )?duration:|Subdued in) <col=[0-9a-f]{6}>(?<pb>[0-9:]+(?:\\.[0-9]+)?)</col> \\(new personal best\\)");
	private static final Pattern STRAY_DOG_GIVEN_BONES_REGEX = Pattern.compile("You give the dog some nice.*bones.*");
	private static final Pattern COLLECTION_LOG_ITEM_REGEX = Pattern.compile("New item added to your collection log:.*");
	private static final Pattern COMBAT_TASK_REGEX = Pattern.compile("Congratulations, you've completed an? (?:\\w+) combat task:.*");
	private static final Pattern QUEST_REGEX = Pattern.compile("Congratulations, you've completed a quest:.*");
	private static final Pattern BOND_OFFER_REGEX = Pattern.compile(C_ENGINEER + " is offering to give you a bond\\.");
	private static final Pattern STAT_SPY_REGEX = Pattern.compile(Text.standardize(C_ENGINEER + " is reading your skill stats!"));

	private static final Random random = new Random();

	private static final WorldArea FALADOR_HAIRDRESSER = new WorldArea(new WorldPoint(2942, 3377, 0), 8, 12);
	private static final int FALADOR_HAIRCUT_WIDGET_GROUP_ID = 516;
	private static final int CHILD_ID_MASK = 0xffff;

	private static final int ID_OBJECT_LUMCASTLE_GROUND_LEVEL_STAIRCASE = 16671;
    private static final int WORLD_POINT_LUMCASTLE_STAIRCASE_NORTH_X = 3204;
    private static final int WORLD_POINT_LUMCASTLE_STAIRCASE_NORTH_Y = 3229;

    private static final Set<Integer> badCollectionLogNotificationSettingValues = Set.of(0, 2);

	private final Map<Skill, Integer> oldExperience = new EnumMap<>(Skill.class);
	private final Map<Integer, Integer> oldAchievementDiaries = new HashMap<>();

	private int lastLoginTick = -1;
	private int lastGEOfferTick = -1;
	private int lastZulrahKillTick = -1;
	private int lastSnowballTriggerTick = -1;
	private int lastColLogSettingWarning = -1;

	private Player cEngineerPlayer = null;
	private boolean gameStateLoggedIn = false;
	private boolean warnedAboutNonParchmentedInfernalThisSession = false;

	@Override
	protected void startUp() throws Exception
	{
		clientThread.invoke(this::setupOldMaps);
		lastLoginTick = -1;
		executor.submit(() -> {
			SoundFileManager.ensureDownloadDirectoryExists();
			SoundFileManager.downloadAllMissingSounds(okHttpClient, config.downloadStreamerTrolls());
		});
		warnedAboutNonParchmentedInfernalThisSession = false;
	}

	@Override
	protected void shutDown() throws Exception
	{
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
	public void onGameStateChanged(GameStateChanged event)
	{
		gameStateLoggedIn = event.getGameState() == GameState.LOGGED_IN;
		switch(event.getGameState())
		{
			case LOGIN_SCREEN:
			case HOPPING:
			case LOGGING_IN:
			case LOGIN_SCREEN_AUTHENTICATOR:
				oldExperience.clear();
				oldAchievementDiaries.clear();
			case CONNECTION_LOST:
				// set to -1 here in-case of race condition with varbits changing before this handler is called
				// when game state becomes LOGGED_IN
				lastLoginTick = -1;
				lastColLogSettingWarning = client.getTickCount(); // avoid warning during DC
				break;
			case LOGGED_IN:
				lastLoginTick = client.getTickCount();
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
			if (config.showChatMessages()) {
				client.addChatMessage(ChatMessageType.PUBLICCHAT, C_ENGINEER, "Level up: completed.", null);
			}
			soundEngine.playClip(Sound.LEVEL_UP, executor);
		}
	}

	@Subscribe
	public void onActorDeath(ActorDeath actorDeath) {
		if (config.announceDeath() && actorDeath.getActor() == client.getLocalPlayer()) {
			if (config.showChatMessages()) {
				client.addChatMessage(ChatMessageType.PUBLICCHAT, C_ENGINEER, "Dying on my HCIM: completed.", null);
			}
			soundEngine.playClip(Sound.DEATH, executor);
		}
	}

	@Subscribe
	public void onChatMessage(ChatMessage chatMessage) {
		if (chatMessage.getType() != ChatMessageType.GAMEMESSAGE && chatMessage.getType() != ChatMessageType.SPAM) {
			return;
		}

		if (config.announceCollectionLog() && COLLECTION_LOG_ITEM_REGEX.matcher(chatMessage.getMessage()).matches()) {
			if (config.showChatMessages()) {
				client.addChatMessage(ChatMessageType.PUBLICCHAT, C_ENGINEER, "Collection log slot: completed.", null);
			}
			soundEngine.playClip(Sound.COLLECTION_LOG_SLOT, executor);

		} else if (config.announceQuestCompletion() && QUEST_REGEX.matcher(chatMessage.getMessage()).matches()) {
			if (config.showChatMessages()) {
				client.addChatMessage(ChatMessageType.PUBLICCHAT, C_ENGINEER, "Quest: completed.", null);
			}
			soundEngine.playClip(Sound.QUEST, executor);

		} else if (config.announceCombatAchievement() && COMBAT_TASK_REGEX.matcher(chatMessage.getMessage()).matches()) {
			if (config.showChatMessages()) {
				client.addChatMessage(ChatMessageType.PUBLICCHAT, C_ENGINEER, "Combat task: completed.", null);
			}
			soundEngine.playClip(Sound.COMBAT_TASK, executor);

		} else if (config.easterEggs() && STRAY_DOG_GIVEN_BONES_REGEX.matcher(chatMessage.getMessage()).matches()) {
			if (config.showChatMessages()) {
				client.addChatMessage(ChatMessageType.PUBLICCHAT, C_ENGINEER, "I love you.", null);
			}
			soundEngine.playClip(Sound.EASTER_EGG_STRAYDOG_BONE, executor);
		} else if (config.easterEggs() && STAT_SPY_REGEX.matcher(Text.standardize(chatMessage.getMessage())).matches()) {
			Player localPlayer = client.getLocalPlayer();
			if (localPlayer == null) return;
			String localPlayerName = localPlayer.getName();
			if (localPlayerName == null) return;

			if (SKILL_SPECS.equalsIgnoreCase(Text.toJagexName(localPlayerName))) {
				soundEngine.playClip(Sound.STAT_SPY_TORVESTA, executor);
			} else {
				soundEngine.playClip(STAT_SPY_SOUNDS[random.nextInt(STAT_SPY_SOUNDS.length)], executor);
			}

		} else if (config.easterEggs()) { /* check for zulrah kc and then pb same kill */
			Matcher matcher = KILLCOUNT_PATTERN.matcher(chatMessage.getMessage());
			if (matcher.find() && ZULRAH.equals(matcher.group(1))) {
				// tick count used to prevent re-announcing if user gets pb on non-zulrah boss after killing zulrah
				lastZulrahKillTick = client.getTickCount();
			}
			matcher = NEW_PB_PATTERN.matcher(chatMessage.getMessage());
			if (matcher.find() && client.getTickCount() - lastZulrahKillTick < 2) {
				// Player just got pb, and last zulrah kill was within a tick from now, already checked config.easterEggs()
				if (config.showChatMessages()) {
					client.addChatMessage(ChatMessageType.PUBLICCHAT, C_ENGINEER, "Gz on the new personal best! Last time I got a pb here, I died on my HCIM!", null);
				}
				soundEngine.playClip(Sound.EASTER_EGG_ZULRAH_PB, executor);
			}
		}
	}

	private void checkAndWarnForCollectionLogNotificationSetting(int newVarbitValue) {
        if (!config.announceCollectionLog())
            return;

        if (!gameStateLoggedIn)
        	return;

        if (badCollectionLogNotificationSettingValues.contains(newVarbitValue)) {
            if (lastColLogSettingWarning == -1 || client.getTickCount() - lastColLogSettingWarning > 16) {
                lastColLogSettingWarning = client.getTickCount();
                sendHighlightedMessage("Please enable \"Collection log - New addition notification\" in your game settings for C Engineer to know when to announce it! (The chat message one, pop-up doesn't matter)");
            }
        }
	}

	private void checkAndWarnForUnparchmentedInfernal() {
		if (!config.announceNonTrouverInfernal())
			return;

		if (!gameStateLoggedIn)
			return;

		if (warnedAboutNonParchmentedInfernalThisSession)
			return;

		ItemContainer equipment = client.getItemContainer(InventoryID.EQUIPMENT);
		boolean warnForEquip = equipment != null &&
				(equipment.contains(ItemID.INFERNAL_CAPE) || equipment.contains(ItemID.INFERNAL_MAX_CAPE));
		ItemContainer inventory = client.getItemContainer(InventoryID.INVENTORY);
		boolean warnForInvent = inventory != null &&
				(inventory.contains(ItemID.INFERNAL_CAPE) || inventory.contains(ItemID.INFERNAL_MAX_CAPE));

		if (warnForEquip || warnForInvent) {
			warnedAboutNonParchmentedInfernalThisSession = true;
			if (config.showChatMessages()) {
				client.addChatMessage(ChatMessageType.PUBLICCHAT, C_ENGINEER, "Don't forget to lock your infernal cape with a trouver parchment!", null);
			}
			soundEngine.playClip(Sound.EASTER_EGG_TWISTED_BOW_1GP, executor); // todo use proper sound (change chat message above to match)
		}
	}

	@Subscribe
	public void onVarbitChanged(VarbitChanged varbitChanged) {
		if (varbitChanged.getVarbitId() == Varbits.COLLECTION_LOG_NOTIFICATION) {
			checkAndWarnForCollectionLogNotificationSetting(varbitChanged.getValue());
		}

		if (varbitChanged.getVarbitId() == Varbits.IN_WILDERNESS && varbitChanged.getValue() == 1) {
			checkAndWarnForUnparchmentedInfernal();
		}

		// As we can't listen to specific varbits, we get a tonne of events BEFORE the game has even set the player's
		// diary varbits correctly, meaning it assumes every diary is on 0, then suddenly every diary that has been
		// completed gets updated to the true value and tricks the plugin into thinking they only just finished it.
		// To avoid this behaviour, we make sure the current tick count is sufficiently high that we've already passed
		// the initial wave of varbit changes from logging in.
		if (lastLoginTick == -1 || client.getTickCount() - lastLoginTick < 8) {
			return; // Ignoring varbit change as only just logged in
		}

		// Apparently I can't check if it's a particular varbit using the names from Varbits enum, so this is the way
		for (@Varbit int diary : varbitsAchievementDiaries) {
			int newValue = client.getVarbitValue(diary);
			int previousValue = oldAchievementDiaries.getOrDefault(diary, -1);
			oldAchievementDiaries.put(diary, newValue);
			if (config.announceAchievementDiary() && previousValue != -1 && previousValue != newValue && isAchievementDiaryCompleted(diary, newValue)) {
				// value was not unknown (we know the previous value), value has changed, and value indicates diary is completed now
				if (config.showChatMessages()) {
					client.addChatMessage(ChatMessageType.PUBLICCHAT, C_ENGINEER, "Achievement diary: completed.", null);
				}
				soundEngine.playClip(Sound.ACHIEVEMENT_DIARY, executor);
			}
		}
	}

	@Subscribe
	public void onMenuOptionClicked(MenuOptionClicked menuOptionClicked) {
		if (config.easterEggs() && menuOptionClicked.getId() == ID_OBJECT_LUMCASTLE_GROUND_LEVEL_STAIRCASE &&
				menuOptionClicked.getMenuOption().equals("Climb-up")) {
			WorldPoint wp = WorldPoint.fromLocal(client, LocalPoint.fromScene(menuOptionClicked.getParam0(), menuOptionClicked.getParam1()));
			if (wp.getX() == WORLD_POINT_LUMCASTLE_STAIRCASE_NORTH_X && wp.getY() == WORLD_POINT_LUMCASTLE_STAIRCASE_NORTH_Y) {
				// Now we know this is the northern staircase only in lumbridge castle ground floor
				if (config.showChatMessages()) {
					client.addChatMessage(ChatMessageType.PUBLICCHAT, C_ENGINEER, "Please do not use the northern staircase, use the southern one instead.", null);
				}
				soundEngine.playClip(Sound.EASTER_EGG_STAIRCASE, executor);
			}
		}
	}

	@Subscribe
	public void onGrandExchangeOfferChanged(GrandExchangeOfferChanged offerEvent) {
		if (lastLoginTick == -1 || client.getTickCount() - lastLoginTick < 3) {
			return; // Ignoring offer change as likely simply because user just logged in
		}

		final GrandExchangeOffer offer = offerEvent.getOffer();
		if (config.easterEggs() && offer.getItemId() == ItemID.TWISTED_BOW && offer.getPrice() == 1 && offer.getState() == GrandExchangeOfferState.SELLING) {
			// selling tbow 1gp, not changed from login, not cancelled, not sold - now just check ticks to avoid double detection because we get sent each offer twice
			if (lastGEOfferTick == -1 || client.getTickCount() - lastGEOfferTick > 4) {
				if (config.showChatMessages()) {
					client.addChatMessage(ChatMessageType.PUBLICCHAT, C_ENGINEER, "Are you stupid? Did you just try to sell a twisted bow for 1gp?", null);
				}
				soundEngine.playClip(Sound.EASTER_EGG_TWISTED_BOW_1GP, executor);
			}
		}

		// save tick so that next time we get an offer, we can check it isn't the duplicate of this offer
		lastGEOfferTick = client.getTickCount();
	}

	@Subscribe
	public void onWidgetLoaded(WidgetLoaded e) {
		if (!config.easterEggs())
			return;

		if (e.getGroupId() == FALADOR_HAIRCUT_WIDGET_GROUP_ID) {
			// getting the haircut widget via IDs etc seems overly difficult, so just check location
			WorldPoint currentLocation = client.getLocalPlayer().getWorldLocation();
			if (FALADOR_HAIRDRESSER.contains(currentLocation)) {
				soundEngine.playClip(Sound.EASTER_EGG_HAIRCUT, executor);
			}
			return;
		}

		if (e.getGroupId() != InterfaceID.DIALOG_OPTION)
			return;

		clientThread.invokeLater(() -> {
			Widget root = client.getWidget(InterfaceID.DIALOG_OPTION, getChildId(ComponentID.DIALOG_OPTION_OPTIONS));
			if (root == null)
				return;

			Widget[] children = root.getChildren();
			if (children == null)
				return;

			for (Widget child : children) {
				if (BOND_OFFER_REGEX.matcher(Text.removeTags(child.getText())).matches()) {
					soundEngine.playClip(BOND_OFFER_SOUNDS[random.nextInt(BOND_OFFER_SOUNDS.length)], executor);
					return;
				}
			}
		});
	}

	private int getChildId(int componentId) {
		return componentId & CHILD_ID_MASK;
	}

	@Subscribe
	public void onPlayerSpawned(PlayerSpawned playerSpawned) {
		Player player = playerSpawned.getPlayer();

		if (C_ENGINEER.equals(player.getName())) {
			cEngineerPlayer = player;
		}
	}

	@Subscribe
	public void onPlayerDespawned(PlayerDespawned playerDespawned) {
		Player player = playerDespawned.getPlayer();

		if (C_ENGINEER.equals(player.getName())) {
			cEngineerPlayer = null;
		}
	}

	@Subscribe
	public void onProjectileMoved(ProjectileMoved projectileMoved) {
		if (cEngineerPlayer == null)
			return;

		Projectile projectile = projectileMoved.getProjectile();
		if (projectile.getId() != /*snowball*/ 861)
			return;

		int currentTick = client.getTickCount();
		if (currentTick - lastSnowballTriggerTick < 50) // 30s cool down
			return;

		Actor myself = client.getLocalPlayer();
		if (myself == null)
			return;

		Actor projectileInteracting = projectile.getInteracting();
		if (!myself.equals(projectileInteracting))
			return;

		if (!config.easterEggs())
			return;

		WorldPoint cEngineerWP = cEngineerPlayer.getWorldLocation();
		WorldPoint projectileWP = WorldPoint.fromLocal(client, projectile.getX1(), projectile.getY1(), cEngineerWP.getPlane()); // we don't care about plane

		// check snowball is *roughly* from C's tile, while allowing for drive-by/moving while the projectile spawns
		if (cEngineerWP.distanceTo2D(projectileWP) <= 2){
			lastSnowballTriggerTick = currentTick;
			soundEngine.playClip(SNOWBALL_SOUNDS[random.nextInt(SNOWBALL_SOUNDS.length)], executor, Duration.ofSeconds(10));
		}
	}

	@Subscribe
	public void onAnimationChanged(AnimationChanged animationChanged) {
		if (cEngineerPlayer == null)
			return;

		Actor actor = animationChanged.getActor();
		if (!C_ENGINEER.equals(actor.getName()))
			return;

		int actorAnimationId = actor.getAnimation();
		if (actorAnimationId == -1)
			return;

		emoteTriggers.runTriggers(actorAnimationId);
	}

	private boolean isAchievementDiaryCompleted(int diary, int value) {
		switch (diary) {
			case DIARY_KARAMJA_EASY:
			case DIARY_KARAMJA_MEDIUM:
			case DIARY_KARAMJA_HARD:
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
