package com.github.m0bilebtw.emote;

import com.github.m0bilebtw.Sound;
import com.github.m0bilebtw.SoundEngine;
import net.runelite.api.Client;
import net.runelite.api.coords.WorldArea;
import net.runelite.api.coords.WorldPoint;

import javax.inject.Inject;

import java.util.concurrent.ScheduledExecutorService;

import static com.github.m0bilebtw.emote.EmoteAnimationID.TRICK;

public class EmoteTriggers {

    @Inject
    private Client client;

    @Inject
    private SoundEngine soundEngine;

    @Inject
    private ScheduledExecutorService executor;

    private static final WorldArea AKKHA_ROOM = new WorldArea(new WorldPoint(3671, 5398, 1), 29, 20);

    public void runTriggers(int animationId) {
        if (animationId == TRICK) {
            akkhaTroll();
        }
    }

    private void akkhaTroll() {
        WorldPoint currentLocation = WorldPoint.fromLocalInstance(client, client.getLocalPlayer().getLocalLocation());
        if (AKKHA_ROOM.contains(currentLocation)) {
            soundEngine.playClip(Sound.EASTER_EGG_TWISTED_BOW_1GP, executor); // todo use proper sound
        }
    }
}
