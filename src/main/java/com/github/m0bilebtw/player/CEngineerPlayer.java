package com.github.m0bilebtw.player;

import com.github.m0bilebtw.CEngineerCompletedConfig;
import com.github.m0bilebtw.sound.SoundEngine;
import net.runelite.api.Actor;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.HitsplatID;
import net.runelite.api.Player;
import net.runelite.api.Projectile;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.HitsplatApplied;
import net.runelite.api.events.InteractingChanged;
import net.runelite.api.events.PlayerDespawned;
import net.runelite.api.events.PlayerSpawned;
import net.runelite.client.eventbus.Subscribe;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.concurrent.ScheduledExecutorService;

@Singleton
public class CEngineerPlayer {
    public static final String RSN = "C Engineer";

    private static final int FIGHT_INTERACT_OR_DAMAGE_COOLDOWN = 8;

    @Inject
    private Client client;

    @Inject
    private ScheduledExecutorService executor;

    @Inject
    private CEngineerCompletedConfig config;

    @Inject
    private SoundEngine soundEngine;

    private Player player = null;
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
        if (isRelevantHitType && wasFightingMeRecently()) {
            // We want to keep tracking even if the hitsplats aren't from C anymore so we can still play the sound for a kill C contributed to
            lastTickOfFightIncludingCEngi = client.getTickCount();
        }
    }

    public boolean wasFightingMeRecently() {
        return client.getTickCount() - lastTickOfFightIncludingCEngi <= FIGHT_INTERACT_OR_DAMAGE_COOLDOWN;
    }

    public boolean isOutOfRenderDistance() {
        return player == null;
    }

    public boolean actorEquals(Actor other) {
        return player == other;
    }

    public boolean couldHaveThrownProjectileFrom(Projectile projectile) {
        WorldPoint cEngineerWP = player.getWorldLocation();
        WorldPoint projectileWP = WorldPoint.fromLocal(player.getWorldView(), projectile.getX1(), projectile.getY1(), cEngineerWP.getPlane());

        // check projectile is *roughly* from C's tile, while allowing for drive-by/moving while the projectile spawns
        return cEngineerWP.distanceTo2D(projectileWP) <= 2;
    }

    public void sendChatIfEnabled(String message) {
        if (config.showChatMessages()) {
            client.addChatMessage(ChatMessageType.PUBLICCHAT, RSN, message, null);
        }
    }
}
