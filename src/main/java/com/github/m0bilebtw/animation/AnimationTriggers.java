package com.github.m0bilebtw.animation;

import com.github.m0bilebtw.player.CEngineerPlayer;
import com.github.m0bilebtw.player.LocalPlayer;
import com.github.m0bilebtw.sound.Sound;
import com.github.m0bilebtw.sound.SoundEngine;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.coords.WorldArea;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.chat.ChatColorType;
import net.runelite.client.chat.ChatMessageBuilder;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.chat.QueuedMessage;

import javax.inject.Inject;
import java.util.Optional;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.github.m0bilebtw.animation.AnimationID.GENERIC_CHEST_OPEN;
import static com.github.m0bilebtw.animation.AnimationID.PREMIER_SHIELD;
import static com.github.m0bilebtw.animation.AnimationID.SMOOTH_DANCE;
import static com.github.m0bilebtw.animation.AnimationID.TRICK;

public class AnimationTriggers {

    @Inject
    private Client client;

    @Inject
    private SoundEngine soundEngine;

    @Inject
    private ScheduledExecutorService executor;

    @Inject
    private ChatMessageManager chatMessageManager;

    @Inject
    private CEngineerPlayer cEngineer;

    private static final WorldArea AKKHA_ROOM = new WorldArea(new WorldPoint(3671, 5398, 1), 29, 20);
    private static final WorldArea BABA_PUZZLE_ROOM = new WorldArea(new WorldPoint(3788, 5264, 0), 42, 31);

    private static final WorldArea TOA_CHEST_ROOM = new WorldArea(new WorldPoint(3673, 5138, 0), 15, 13);
    private static final WorldArea TOB_CHEST_ROOM = new WorldArea(new WorldPoint(3225, 4320, 0), 18, 14);

    public void runTriggersForLocalPlayerAnimation(int animationId) {
        checkFunnyFeelingTrollTrigger(animationId);
    }

    private void checkFunnyFeelingTrollTrigger(int animationId) {
        if (animationId != GENERIC_CHEST_OPEN)
            return;

        if (!cEngineer.isFollowingMe())
            return;

        WorldPoint currentLocation = WorldPoint.fromLocalInstance(client, client.getLocalPlayer().getLocalLocation());
        if (TOA_CHEST_ROOM.contains(currentLocation) || TOB_CHEST_ROOM.contains(currentLocation)) {
            executor.schedule(this::funnyFeelingTroll, 600, TimeUnit.MILLISECONDS);
        }
    }

    private void funnyFeelingTroll() {
        String highlightedMessage = new ChatMessageBuilder()
                .append(ChatColorType.HIGHLIGHT)
                .append("You have a funny feeling like you're being followed.")
                .build();

        chatMessageManager.queue(QueuedMessage.builder()
                .type(ChatMessageType.CONSOLE)
                .runeLiteFormattedMessage(highlightedMessage)
                .build());

        soundEngine.playClip(Sound.SNOWBALL_EQUIPPING_BUCKET_HELM_G_OR_FUNNY_FEEL, executor);
    }

    public void runTriggersForCEngiAnimation(int animationId) {
        if (animationId == TRICK) {
            akkhaTroll();
        } else if (animationId == PREMIER_SHIELD) {
            babaTroll();
        } else if (animationId == SMOOTH_DANCE) {
            smoothDanceTrolls();
        }
    }

    private void akkhaTroll() {
        WorldPoint currentLocation = WorldPoint.fromLocalInstance(client, client.getLocalPlayer().getLocalLocation());
        if (AKKHA_ROOM.contains(currentLocation)) {
            soundEngine.playClip(Sound.EMOTE_TROLL_AKKHA, executor);
        }
    }

    private void babaTroll() {
        WorldPoint currentLocation = WorldPoint.fromLocalInstance(client, client.getLocalPlayer().getLocalLocation());
        if (BABA_PUZZLE_ROOM.contains(currentLocation)) {
            soundEngine.playClip(Sound.randomBabaEmoteSound(), executor);
        }
    }

    private void smoothDanceTrolls() {
        Optional<String> standardisedNameOpt = LocalPlayer.getStandardisedName(client);
        if (standardisedNameOpt.isEmpty()) return;

        String standardisedName = standardisedNameOpt.get();
        switch (standardisedName) {
            case "a friend 2":
                soundEngine.playClip(Sound.EMOTE_TROLL_AF, executor);
                break;
            case "taint licka":
                soundEngine.playClip(Sound.EMOTE_TROLL_EL, executor);
                break;
            case "westham":
            case "debiedobies":
            case "saund":
                soundEngine.playClip(Sound.EMOTE_TROLL_WE, executor);
                break;
            case "scoutzone": // C's alt for testing
                soundEngine.playClip(Sound.QUEST, executor);
                break;
            default: break;
        }
    }
}
