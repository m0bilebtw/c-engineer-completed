package com.github.m0bilebtw.emote;

import com.github.m0bilebtw.sound.Sound;
import com.github.m0bilebtw.sound.SoundEngine;
import net.runelite.api.Client;
import net.runelite.api.Player;
import net.runelite.api.coords.WorldArea;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.util.Text;

import javax.inject.Inject;
import java.util.concurrent.ScheduledExecutorService;

import static com.github.m0bilebtw.emote.EmoteAnimationID.PREMIER_SHIELD;
import static com.github.m0bilebtw.emote.EmoteAnimationID.SMOOTH_DANCE;
import static com.github.m0bilebtw.emote.EmoteAnimationID.TRICK;

public class EmoteTriggers {

    @Inject
    private Client client;

    @Inject
    private SoundEngine soundEngine;

    @Inject
    private ScheduledExecutorService executor;

    private static final WorldArea AKKHA_ROOM = new WorldArea(new WorldPoint(3671, 5398, 1), 29, 20);
    private static final WorldArea BABA_PUZZLE_ROOM = new WorldArea(new WorldPoint(3788, 5264, 0), 42, 31);

    public void runTriggers(int animationId) {
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
        Player localPlayer = client.getLocalPlayer();
        if (localPlayer == null) return;
        String localName = localPlayer.getName();
        if (localName == null) return;

        String lowerJagexName = Text.toJagexName(localName).toLowerCase();
        switch (lowerJagexName) {
            case "hmic andeey":
                soundEngine.playClip(Sound.EMOTE_TROLL_AN, executor);
                break;
            case "taint licka":
                soundEngine.playClip(Sound.EMOTE_TROLL_EL, executor);
                break;
            case "itsvevisk":
                soundEngine.playClip(Sound.EMOTE_TROLL_VE, executor);
                break;
            case "ironrhapsody":
                soundEngine.playClip(Sound.EMOTE_TROLL_IR, executor);
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
