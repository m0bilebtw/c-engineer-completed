package com.github.m0bilebtw.eastereggs;

import com.github.m0bilebtw.CEngineerCompletedConfig;
import com.github.m0bilebtw.player.CEngineerPlayer;
import com.github.m0bilebtw.player.LoggedInState;
import com.github.m0bilebtw.sound.Sound;
import com.github.m0bilebtw.sound.SoundEngine;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GrandExchangeOffer;
import net.runelite.api.GrandExchangeOfferState;
import net.runelite.api.ItemID;
import net.runelite.api.Scene;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GrandExchangeOfferChanged;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.client.eventbus.Subscribe;

import javax.inject.Inject;
import java.util.concurrent.ScheduledExecutorService;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Easter eggs that normal accounts can trigger in normal gameplay.
 * These are separate to the trolls that C Engineer can trigger.
 */
public class EasterEggTriggers {
    private static final int ID_OBJECT_LUMCASTLE_GROUND_LEVEL_STAIRCASE = 16671;
    private static final int WORLD_POINT_LUMCASTLE_STAIRCASE_NORTH_X = 3204;
    private static final int WORLD_POINT_LUMCASTLE_STAIRCASE_NORTH_Y = 3229;

    private static final String ZULRAH = "Zulrah";
    private static final int ZULRAH_KILL_TO_PB_MESSAGE_LENIENCY = 1;
    // Killcount and new pb patterns originally from runelite/ChatCommandsPlugin
    private static final Pattern KILLCOUNT_PATTERN = Pattern.compile("Your (?:completion count for |subdued |completed )?(.+?) (?:(?:kill|harvest|lap|completion) )?(?:count )?is: <col=ff0000>(\\d+)</col>");
    private static final Pattern NEW_PB_PATTERN = Pattern.compile("(?i)(?:(?:Fight |Lap |Challenge |Corrupted challenge )?duration:|Subdued in) <col=[0-9a-f]{6}>(?<pb>[0-9:]+(?:\\.[0-9]+)?)</col> \\(new personal best\\)");
    private static final Pattern STRAY_DOG_GIVEN_BONES_REGEX = Pattern.compile("You give the dog some nice.*bones.*");

    @Inject
    private Client client;

    @Inject
    private CEngineerCompletedConfig config;

    @Inject
    private ScheduledExecutorService executor;

    @Inject
    private SoundEngine soundEngine;

    @Inject
    private CEngineerPlayer cEngineer;

    @Inject
    private LoggedInState loggedInState;

    private int lastGEOfferTick = -1;
    private int lastZulrahKillTick = -1;

    @Subscribe
    public void onMenuOptionClicked(MenuOptionClicked menuOptionClicked) {
        if (!config.easterEggs())
            return;

        if (menuOptionClicked.getId() == ID_OBJECT_LUMCASTLE_GROUND_LEVEL_STAIRCASE && menuOptionClicked.getMenuOption().equals("Climb-up")) {
            Scene scene = client.getWorldView(menuOptionClicked.getMenuEntry().getWorldViewId()).getScene();
            LocalPoint lp = LocalPoint.fromScene(menuOptionClicked.getParam0(), menuOptionClicked.getParam1(), scene);
            WorldPoint wp = WorldPoint.fromLocal(client, lp);
            if (wp.getX() == WORLD_POINT_LUMCASTLE_STAIRCASE_NORTH_X && wp.getY() == WORLD_POINT_LUMCASTLE_STAIRCASE_NORTH_Y) {
                // Now we know this is the northern staircase only in Lumbridge castle ground floor
                cEngineer.sendChatIfEnabled("Please do not use the northern staircase, use the southern one instead.");
                soundEngine.playClip(Sound.EASTER_EGG_STAIRCASE, executor);
            }
        }
    }

    @Subscribe
    public void onGrandExchangeOfferChanged(GrandExchangeOfferChanged offerEvent) {
        if (!config.easterEggs())
            return;

        if (loggedInState.isLoggedOut() || loggedInState.onlyJustLoggedIn(3)) {
            return; // Ignoring offer change as likely simply because user just logged in
        }

        GrandExchangeOffer offer = offerEvent.getOffer();
        if (offer.getItemId() == ItemID.TWISTED_BOW &&
                offer.getPrice() == 1 &&
                offer.getState() == GrandExchangeOfferState.SELLING &&
                // check ticks to avoid double detection because we get sent each offer twice
                (lastGEOfferTick == -1 || client.getTickCount() - lastGEOfferTick > 4)) {
            cEngineer.sendChatIfEnabled("Are you stupid? Did you just try to sell a twisted bow for 1gp?");
            soundEngine.playClip(Sound.EASTER_EGG_TWISTED_BOW_1GP, executor);
        }

        // save tick so that next time we get an offer, we can check it isn't the duplicate of this offer
        lastGEOfferTick = client.getTickCount();
    }

    @Subscribe
    public void onChatMessage(ChatMessage chatMessage) {
        if (chatMessage.getType() != ChatMessageType.GAMEMESSAGE && chatMessage.getType() != ChatMessageType.SPAM)
            return;

        if (!config.easterEggs())
            return;

        if (STRAY_DOG_GIVEN_BONES_REGEX.matcher(chatMessage.getMessage()).matches()) {
            cEngineer.sendChatIfEnabled("I love you.");
            soundEngine.playClip(Sound.EASTER_EGG_STRAYDOG_BONE, executor);
        } else {
            checkForZulrahPB(chatMessage);
        }
    }

    private void checkForZulrahPB(ChatMessage chatMessage) {
        Matcher kcMatcher = KILLCOUNT_PATTERN.matcher(chatMessage.getMessage());
        if (kcMatcher.find() && ZULRAH.equals(kcMatcher.group(1))) {
            lastZulrahKillTick = client.getTickCount();
            return;
        }

        Matcher pbMatcher = NEW_PB_PATTERN.matcher(chatMessage.getMessage());
        if (pbMatcher.find() && client.getTickCount() - lastZulrahKillTick <= ZULRAH_KILL_TO_PB_MESSAGE_LENIENCY) {
            // Player just got pb, and last zulrah kill was within a tick from now
            cEngineer.sendChatIfEnabled("Gz on the new personal best! Last time I got a pb here, I died on my HCIM!");
            soundEngine.playClip(Sound.EASTER_EGG_ZULRAH_PB, executor);
        }
    }
}
