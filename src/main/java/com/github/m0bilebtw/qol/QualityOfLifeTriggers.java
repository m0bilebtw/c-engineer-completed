package com.github.m0bilebtw.qol;

import com.github.m0bilebtw.CEngineerCompletedConfig;
import com.github.m0bilebtw.player.CEngineerPlayer;
import com.github.m0bilebtw.player.LoggedInState;
import com.github.m0bilebtw.sound.Sound;
import com.github.m0bilebtw.sound.SoundEngine;
import net.runelite.api.Actor;
import net.runelite.api.Client;
import net.runelite.api.GameObject;
import net.runelite.api.GameState;
import net.runelite.api.ItemContainer;
import net.runelite.api.Player;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.ActorDeath;
import net.runelite.api.events.GameObjectDespawned;
import net.runelite.api.events.GameObjectSpawned;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.VarbitChanged;
import net.runelite.api.gameval.InventoryID;
import net.runelite.api.gameval.ItemID;
import net.runelite.api.gameval.ObjectID;
import net.runelite.api.gameval.VarbitID;
import net.runelite.client.eventbus.Subscribe;

import javax.inject.Inject;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;

public class QualityOfLifeTriggers {
    private static final int INFERNAL_PARCHMENT_WARN_COOLDOWN = 36;
    private static final Set<Integer> BOUNTY_HUNTER_REGIONS = Set.of(13374, 13375, 13376, 13630, 13631, 13632, 13886, 13887, 13888);

    /**
     * A crashed star is a single object that swaps to the next object id every time its current layer is mined out,
     * counting down from size 9 to size 1, so the size doubles up as the layer we are currently mining.
     */
    private static final Map<Integer, Integer> STAR_OBJECT_ID_TO_SIZE = Map.of(
            ObjectID.STAR_SIZE_NINE_STAR, 9,
            ObjectID.STAR_SIZE_EIGHT_STAR, 8,
            ObjectID.STAR_SIZE_SEVEN_STAR, 7,
            ObjectID.STAR_SIZE_SIX_STAR, 6,
            ObjectID.STAR_SIZE_FIVE_STAR, 5,
            ObjectID.STAR_SIZE_FOUR_STAR, 4,
            ObjectID.STAR_SIZE_THREE_STAR, 3,
            ObjectID.STAR_SIZE_TWO_STAR, 2,
            ObjectID.STAR_SIZE_ONE_STAR, 1
    );

    /** Only announce for a star we are actually standing at, rather than one that happens to be in view. */
    private static final int STAR_NEARBY_TILES = 5;

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

    private static final String GEMSTONE_CRAB_ACTOR_NAME = "Gemstone Crab";

    private int lastInfernalParchmentWarningTick = -1;

    private WorldPoint knownStarLocation = null;
    private int knownStarSize = -1;
    private boolean knownStarDespawned = false;

    @Subscribe
    public void onVarbitChanged(VarbitChanged varbitChanged) {
        if (varbitChanged.getVarbitId() == VarbitID.INSIDE_WILDERNESS && varbitChanged.getValue() == 1) {
            checkAndWarnForUnparchmentedInfernal();
        }
    }

    @Subscribe
    public void onGameStateChanged(GameStateChanged gameStateChanged) {
        if (gameStateChanged.getGameState() != GameState.LOGGED_IN) {
            forgetKnownStar();
        }
    }

    @Subscribe
    public void onGameObjectSpawned(GameObjectSpawned gameObjectSpawned) {
        GameObject gameObject = gameObjectSpawned.getGameObject();
        Integer size = STAR_OBJECT_ID_TO_SIZE.get(gameObject.getId());
        if (size == null)
            return;

        WorldPoint location = gameObject.getWorldLocation();

        // Mining out a layer despawns the old star object and spawns the next one down within the same tick, so a
        // smaller star appearing where we already knew of a bigger one means its layer was just mined through.
        if (location.equals(knownStarLocation) && size < knownStarSize && playerIsAtStar(location)) {
            announceStarLayerMined();
        }

        knownStarLocation = location;
        knownStarSize = size;
        knownStarDespawned = false;
    }

    @Subscribe
    public void onGameObjectDespawned(GameObjectDespawned gameObjectDespawned) {
        GameObject gameObject = gameObjectDespawned.getGameObject();
        if (STAR_OBJECT_ID_TO_SIZE.containsKey(gameObject.getId()) && gameObject.getWorldLocation().equals(knownStarLocation)) {
            knownStarDespawned = true;
        }
    }

    @Subscribe
    public void onGameTick(GameTick gameTick) {
        // The replacement star spawns in the same tick it despawned, so anything still despawned by now is a star that
        // has fully depleted or that we have walked away from, and is no longer ours to compare against.
        if (knownStarDespawned) {
            forgetKnownStar();
        }
    }

    private void announceStarLayerMined() {
        if (!config.announceShootingStarLayerMined())
            return;

        cEngineer.sendChatIfEnabled("Shooting star layer: mined.");
        soundEngine.playClip(Sound.QOL_SHOOTING_STAR_LAYER_MINED, executor);
    }

    private boolean playerIsAtStar(WorldPoint starLocation) {
        Player player = client.getLocalPlayer();
        return player != null && player.getWorldLocation().distanceTo(starLocation) <= STAR_NEARBY_TILES;
    }

    private void forgetKnownStar() {
        knownStarLocation = null;
        knownStarSize = -1;
        knownStarDespawned = false;
    }

    @Subscribe
    public void onActorDeath(ActorDeath actorDeath) {
        if (!config.announceGemstoneCrabMovement())
            return;

        Actor actor = actorDeath.getActor();
        if (client.getLocalPlayer().getInteracting() == actor && GEMSTONE_CRAB_ACTOR_NAME.equals(actor.getName())) {
            cEngineer.sendChatIfEnabled("The gem crab has moved!");
            soundEngine.playClip(Sound.QOL_GEM_CRAB_MOVED, executor);
        }
    }

    private void checkAndWarnForUnparchmentedInfernal() {
        if (!config.announceNonTrouverInfernal())
            return;

        if (loggedInState.isLoggedOut())
            return;

        if (lastInfernalParchmentWarningTick != -1 && client.getTickCount() - lastInfernalParchmentWarningTick < INFERNAL_PARCHMENT_WARN_COOLDOWN)
            return;

        if (atBountyHunter())
            return;

        ItemContainer equipment = client.getItemContainer(InventoryID.WORN);
        boolean warnForEquip = equipment != null &&
                (equipment.contains(ItemID.INFERNAL_CAPE) || equipment.contains(ItemID.SKILLCAPE_MAX_INFERNALCAPE_DUMMY));
        ItemContainer inventory = client.getItemContainer(InventoryID.INV);
        boolean warnForInvent = inventory != null &&
                (inventory.contains(ItemID.INFERNAL_CAPE) || inventory.contains(ItemID.SKILLCAPE_MAX_INFERNALCAPE_DUMMY));

        if (warnForEquip || warnForInvent) {
            lastInfernalParchmentWarningTick = client.getTickCount();
            cEngineer.sendChatIfEnabled("Your infernal cape is not parched!");
            soundEngine.playClip(Sound.QOL_NON_PARCH_INFERNAL, executor);
        }
    }

    private boolean atBountyHunter() {
        Player player = client.getLocalPlayer();
        if (player == null)
            return false;

        int regionId = player.getWorldLocation().getRegionID();
        return BOUNTY_HUNTER_REGIONS.contains(regionId);
    }
}
