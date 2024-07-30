package com.github.m0bilebtw.player;

import com.github.m0bilebtw.CEngineerCompletedConfig;
import net.runelite.api.Actor;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.HitsplatID;
import net.runelite.api.ItemID;
import net.runelite.api.Player;
import net.runelite.api.Projectile;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.HitsplatApplied;
import net.runelite.api.events.InteractingChanged;
import net.runelite.api.events.PlayerDespawned;
import net.runelite.api.events.PlayerSpawned;
import net.runelite.client.eventbus.Subscribe;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Arrays;
import java.util.List;

@Singleton
public class CEngineerPlayer {
    public static final String RSN = "C Engineer";

    private static final int ITEM_IDS_START_FROM = 512;
    private static final int FIGHT_INTERACT_OR_DAMAGE_COOLDOWN = 8;

    private static final List<Integer> AHRIMS_HOODS = List.of(ItemID.AHRIMS_HOOD, ItemID.AHRIMS_HOOD_100, ItemID.AHRIMS_HOOD_75, ItemID.AHRIMS_HOOD_50, ItemID.AHRIMS_HOOD_25, ItemID.AHRIMS_HOOD_0);
    private static final List<Integer> AHRIMS_TOPS = List.of(ItemID.AHRIMS_ROBETOP, ItemID.AHRIMS_ROBETOP_100, ItemID.AHRIMS_ROBETOP_75, ItemID.AHRIMS_ROBETOP_50, ItemID.AHRIMS_ROBETOP_25, ItemID.AHRIMS_ROBETOP_0);
    private static final List<Integer> AHRIMS_BOTTOMS = List.of(ItemID.AHRIMS_ROBESKIRT, ItemID.AHRIMS_ROBESKIRT_100, ItemID.AHRIMS_ROBESKIRT_75, ItemID.AHRIMS_ROBESKIRT_50, ItemID.AHRIMS_ROBESKIRT_25, ItemID.AHRIMS_ROBESKIRT_0);

    @Inject
    private Client client;

    @Inject
    private CEngineerCompletedConfig config;

    @Inject
    private CEngineerChatTrolls cEngineerChatTrolls;

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
    public void onChatMessage(ChatMessage chatMessage) {
        if (!config.easterEggs())
            return;

        if (isOutOfRenderDistance() ||
                chatMessage.getType() != ChatMessageType.PUBLICCHAT ||
                !RSN.equals(chatMessage.getName()))
            return;

        cEngineerChatTrolls.runTriggers(chatMessage);
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

    public boolean isWearing(int itemId) {
        if (player == null)
            return false;

        int[] equipmentIds = player.getPlayerComposition().getEquipmentIds();
        return Arrays.stream(equipmentIds)
                .filter(i -> i > ITEM_IDS_START_FROM)
                .map(i -> i - ITEM_IDS_START_FROM)
                .anyMatch(i -> i == itemId);
    }

    public boolean isWearingAttackTrollRequirements() {
        return isWearing(ItemID._3RD_AGE_AMULET) &&
                isWearing(ItemID.DRAGON_CLAWS) &&
                AHRIMS_HOODS.stream().anyMatch(this::isWearing) &&
                AHRIMS_TOPS.stream().anyMatch(this::isWearing) &&
                AHRIMS_BOTTOMS.stream().anyMatch(this::isWearing);
    }

    public boolean isFollowingMe() {
        return isInteracting(client.getLocalPlayer());
    }

    public boolean isInteracting(Actor actor) {
        if (actor == null)
            return false;

        if (isOutOfRenderDistance())
            return false;

        Actor cEngineerInteractTarget = player.getInteracting();
        return cEngineerInteractTarget == actor;
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

    public int tilesFrom(Actor actor) {
        if (actor == null || player == null)
            return Integer.MAX_VALUE;

        WorldPoint cEngineerWP = player.getWorldLocation();
        WorldPoint actorWP = actor.getWorldLocation();

        return cEngineerWP.distanceTo2D(actorWP);
    }

    public void sendChatIfEnabled(String message) {
        if (config.showChatMessages()) {
            client.addChatMessage(ChatMessageType.PUBLICCHAT, RSN, message, null);
        }
    }
}
