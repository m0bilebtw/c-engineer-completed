package com.github.m0bilebtw.player;

import net.runelite.api.Client;
import net.runelite.api.Player;
import net.runelite.client.util.Text;

import java.util.Optional;

public class LocalPlayer {
    private LocalPlayer () {}

    public static Optional<String> getLowerJagexName(Client client) {
        Player localPlayer = client.getLocalPlayer();
        if (localPlayer == null)
            return Optional.empty();

        String localName = localPlayer.getName();
        if (localName == null)
            return Optional.empty();

        String lowerJagexName = Text.toJagexName(localName).toLowerCase();
        return Optional.of(lowerJagexName);
    }
}
