package com.github.m0bilebtw.player;

import com.github.m0bilebtw.CEngineerCompletedConfig;
import com.github.m0bilebtw.emote.EmoteTriggers;
import com.github.m0bilebtw.projectile.ProjectileID;
import com.github.m0bilebtw.sound.Sound;
import com.github.m0bilebtw.sound.SoundEngine;
import net.runelite.api.Actor;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.HitsplatID;
import net.runelite.api.Player;
import net.runelite.api.Projectile;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.ActorDeath;
import net.runelite.api.events.AnimationChanged;
import net.runelite.api.events.HitsplatApplied;
import net.runelite.api.events.InteractingChanged;
import net.runelite.api.events.PlayerDespawned;
import net.runelite.api.events.PlayerSpawned;
import net.runelite.api.events.ProjectileMoved;
import net.runelite.client.eventbus.Subscribe;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.time.Duration;
import java.util.Random;
import java.util.concurrent.ScheduledExecutorService;

@Singleton
public class CEngineerPlayer {
    public static final String RSN = "C Engineer";

    private static final int SNOWBALL_COOLDOWN_TICKS = 50;
    private static final Duration SNOWBALL_DELAY_SOUNDS = Duration.ofSeconds(20);

    private static final int FIGHT_INTERACT_OR_DAMAGE_COOLDOWN = 8;

    private static final Random random = new Random();

    @Inject
    private Client client;

    @Inject
    private ScheduledExecutorService executor;

    @Inject
    private CEngineerCompletedConfig config;

    @Inject
    private EmoteTriggers emoteTriggers;

    @Inject
    private SoundEngine soundEngine;

    private Player player = null;
    private int lastSnowballTriggerTick = -1;
    private int lastTickOfFightIncludingCEngi = -1;

    @Subscribe
    public void onPlayerSpawned(PlayerSpawned playerSpawned) {
        Player spawnedPlayer = playerSpawned.getPlayer();
        if (RSN.equals(spawnedPlayer.getName())) {
            this.player = spawnedPlayer;
        }
    }

    @Subscribe
    public void onPlayerDespawned(PlayerDespawned playerDespawned) {
        Player despawnedPlayer = playerDespawned.getPlayer();
        if (RSN.equals(despawnedPlayer.getName())) {
            this.player = null;
        }
    }

    @Subscribe
    public void onAnimationChanged(AnimationChanged animationChanged) {
        if (isOutOfRenderDistance())
            return;

        Actor actor = animationChanged.getActor();
        if (!actorEquals(actor))
            return;

        if (!config.easterEggs())
            return;

        int actorAnimationId = actor.getAnimation();
        if (actorAnimationId == -1)
            return;

        emoteTriggers.runTriggers(actorAnimationId);
    }

    @Subscribe
    public void onProjectileMoved(ProjectileMoved projectileMoved) {
        if (isOutOfRenderDistance())
            return;

        Projectile projectile = projectileMoved.getProjectile();
        if (projectile.getId() != ProjectileID.SNOWBALL)
            return;

        int currentTick = client.getTickCount();
        if (currentTick - lastSnowballTriggerTick < SNOWBALL_COOLDOWN_TICKS)
            return;

        Actor myself = client.getLocalPlayer();
        if (myself == null)
            return;

        Actor projectileInteracting = projectile.getInteracting();
        if (!myself.equals(projectileInteracting))
            return;

        if (!config.easterEggs())
            return;

        WorldPoint cEngineerWP = player.getWorldLocation();
        WorldPoint projectileWP = WorldPoint.fromLocal(client, projectile.getX1(), projectile.getY1(), cEngineerWP.getPlane()); // we don't care about plane

        // check snowball is *roughly* from C's tile, while allowing for drive-by/moving while the projectile spawns
        if (cEngineerWP.distanceTo2D(projectileWP) <= 2) {
            lastSnowballTriggerTick = currentTick;
            soundEngine.playClip(Sound.randomSnowballSound(), executor, SNOWBALL_DELAY_SOUNDS);
        }
    }

    @Subscribe
    public void onInteractingChanged(InteractingChanged interactingChanged) {
        if (isOutOfRenderDistance())
            return;

        if (actorEquals(interactingChanged.getSource()) && interactingChanged.getTarget() == client.getLocalPlayer()) {
            lastTickOfFightIncludingCEngi = client.getTickCount();
        }
    }

    @Subscribe
    public void onHitsplatApplied(HitsplatApplied hitsplatApplied) {
        if (isOutOfRenderDistance())
            return;

        if (hitsplatApplied.getActor() != client.getLocalPlayer())
            return;

        int hitType = hitsplatApplied.getHitsplat().getHitsplatType();
        boolean isRelevantHitType = hitType == HitsplatID.DAMAGE_ME
                || hitType == HitsplatID.DAMAGE_ME_ORANGE
                || hitType == HitsplatID.DAMAGE_MAX_ME
                || hitType == HitsplatID.DAMAGE_MAX_ME_ORANGE
                || hitType == HitsplatID.POISON
                || hitType == HitsplatID.VENOM;
        if (isRelevantHitType && client.getTickCount() - lastTickOfFightIncludingCEngi <= FIGHT_INTERACT_OR_DAMAGE_COOLDOWN) {
            // We want to keep tracking even if the hitsplats aren't from C anymore so we can still play the sound for a kill C contributed to
            lastTickOfFightIncludingCEngi = client.getTickCount();
        }
    }

    @Subscribe
    public void onActorDeath(ActorDeath actorDeath) {
        if (actorDeath.getActor() != client.getLocalPlayer())
            return;

        if (config.easterEggs() && client.getTickCount() - lastTickOfFightIncludingCEngi <= FIGHT_INTERACT_OR_DAMAGE_COOLDOWN) {
            if (random.nextBoolean()) {
                sendChatIfEnabled("Dying to " + CEngineerPlayer.RSN + ": completed.");
            } else {
                sendChatIfEnabled("Sit");
            }
            soundEngine.playClip(Sound.DEATH_TO_C_ENGINEER, executor);
        }
    }

    public boolean isOutOfRenderDistance() {
        return player == null;
    }

    public boolean actorEquals(Actor other) {
        return player == other;
    }

    public void sendChatIfEnabled(String message) {
        if (config.showChatMessages()) {
            client.addChatMessage(ChatMessageType.PUBLICCHAT, RSN, message, null);
        }
    }
}
