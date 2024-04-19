package com.github.m0bilebtw;

import org.junit.Test;

import static org.junit.Assert.assertFalse;

public class SoundTest {

    @Test
    public void values() {
        for (Sound sound : Sound.values()) {
            String message = "Sound " + sound.name() + " should have a non-blank resource name";
            assertFalse(message, sound.getResourceName().isBlank());
        }
    }
}
