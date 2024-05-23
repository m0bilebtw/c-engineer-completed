package com.github.m0bilebtw.announce;

import com.github.m0bilebtw.CEngineerCompletedConfig;
import com.github.m0bilebtw.player.CEngineerPlayer;
import com.github.m0bilebtw.sound.Sound;
import com.github.m0bilebtw.sound.SoundEngine;
import net.runelite.api.Client;
import net.runelite.api.events.ActorDeath;
import net.runelite.client.eventbus.Subscribe;

import javax.inject.Inject;
import java.util.Random;
import java.util.concurrent.ScheduledExecutorService;

public class AnnouncementTriggers { // todo move announcement triggers/tracking here?
    private static final Random random = new Random();

    @Inject
    private Client client;

    @Inject
    private ScheduledExecutorService executor;

    @Inject
    private CEngineerCompletedConfig config;

    @Inject
    private SoundEngine soundEngine;

    @Inject
    private CEngineerPlayer cEngineer;

    @Subscribe
    public void onActorDeath(ActorDeath actorDeath) {
        if (actorDeath.getActor() != client.getLocalPlayer())
            return;

        // Death easter egg and normal announcement kept together to make it one or the other, never both
        if (config.easterEggs() && cEngineer.wasFightingMeRecently()) {
            diedToCEngineer();
        } else if (config.announceDeath()) {
            diedToAnythingElse();
        }
    }

    private void diedToCEngineer() {
        if (random.nextBoolean()) {
            cEngineer.sendChatIfEnabled("Dying to " + CEngineerPlayer.RSN + ": completed.");
        } else {
            cEngineer.sendChatIfEnabled("Sit");
        }
        soundEngine.playClip(Sound.DEATH_TO_C_ENGINEER, executor);
    }

    private void diedToAnythingElse() {
        cEngineer.sendChatIfEnabled("Dying on my HCIM: completed.");
        soundEngine.playClip(Sound.DEATH, executor);
    }
}
