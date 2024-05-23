package com.github.m0bilebtw.player;

import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.GameStateChanged;
import net.runelite.client.eventbus.Subscribe;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class LoggedInState {
    private static final int LOGGED_OUT = -1;

    @Inject
    private Client client;

    private int lastLoggedInTick = LOGGED_OUT;

    @Subscribe
    public void onGameStateChanged(GameStateChanged event) {
        setForCurrentGameState(event.getGameState());
    }

    public void setForCurrentGameState(GameState gameState) {
        if (gameState == GameState.LOGGED_IN) {
            setLastLoginTick(client.getTickCount());
        } else {
            setLoggedOut();
        }
    }

    public boolean isLoggedOut() {
        return this.lastLoggedInTick == LOGGED_OUT;
    }

    public boolean onlyJustLoggedIn(int tickWindow) {
        return client.getTickCount() - this.lastLoggedInTick < tickWindow;
    }

    public void setLoggedOut() {
        this.lastLoggedInTick = LOGGED_OUT;
    }

    public void setLastLoginTick(int lastLoggedInTick) {
        this.lastLoggedInTick = lastLoggedInTick;
    }
}
