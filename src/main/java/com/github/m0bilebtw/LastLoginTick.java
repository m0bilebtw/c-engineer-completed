package com.github.m0bilebtw;

import javax.inject.Singleton;

@Singleton
public class LastLoginTick {
    private static final int UNSET = -1;

    private int lastLoggedInTick = UNSET;

    public boolean isUnset() {
        return this.lastLoggedInTick == UNSET;
    }

    public void unset() {
        this.lastLoggedInTick = UNSET;
    }

    public int get() {
        return this.lastLoggedInTick;
    }

    public void set(int lastLoggedInTick) {
        this.lastLoggedInTick = lastLoggedInTick;
    }
}
