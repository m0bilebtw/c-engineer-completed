package com.github.m0bilebtw;

import org.junit.Test;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class SoundTest {

    @Test
    public void valuesPopulated() {
        for (Sound sound : Sound.values()) {
            String message = "Sound " + sound.name() + " should have a non-blank resource name";
            assertFalse(message, sound.getResourceName().isBlank());
        }
    }

    @Test
    public void noDuplicateValues() {
        Set<String> uniqueResourceNames = Arrays.stream(Sound.values())
                .map(Sound::getResourceName)
                .collect(Collectors.toSet());

        assertEquals(Sound.values().length, uniqueResourceNames.size());
    }
}
