package com.github.m0bilebtw.qol;

import com.github.m0bilebtw.CEngineerCompletedConfig;
import com.github.m0bilebtw.player.CEngineerPlayer;
import com.github.m0bilebtw.player.LoggedInState;
import com.github.m0bilebtw.sound.Sound;
import com.github.m0bilebtw.sound.SoundEngine;
import net.runelite.api.Client;
import net.runelite.api.ItemContainer;
import net.runelite.api.Player;
import net.runelite.api.events.VarbitChanged;
import net.runelite.api.gameval.InventoryID;
import net.runelite.api.gameval.ItemID;
import net.runelite.api.gameval.VarbitID;
import net.runelite.client.eventbus.Subscribe;

import javax.inject.Inject;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;

public class QualityOfLifeTriggers {
    private static final int INFERNAL_PARCHMENT_WARN_COOLDOWN = 36;
    private static final Set<Integer> BOUNTY_HUNTER_REGIONS = Set.of(13374, 13375, 13376, 13630, 13631, 13632, 13886, 13887, 13888);

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

    private int lastInfernalParchmentWarningTick = -1;

    @Subscribe
    public void onVarbitChanged(VarbitChanged varbitChanged) {
        if (varbitChanged.getVarbitId() == VarbitID.INSIDE_WILDERNESS && varbitChanged.getValue() == 1) {
            checkAndWarnForUnparchmentedInfernal();
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
