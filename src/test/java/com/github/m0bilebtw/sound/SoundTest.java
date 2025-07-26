package com.github.m0bilebtw.sound;

import org.junit.Test;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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

    @Test
    public void soundsAvoidDurationRangeThatPipeWireCutsOff() throws UnsupportedAudioFileException, IOException {
        // bounds found by manually testing to 3 d.p. - both 1.492 and 1.999 (and all in-between) get cut off
        final double lowerBoundExc = 1.491;
        final double upperBoundExc = 2.0;

        Set<Sound> cutOffSounds = new HashSet<>();
        for (Sound sound : Sound.values()) {
            final double duration = getDurationInSeconds(sound);
            if (duration > lowerBoundExc && duration < upperBoundExc) {
                cutOffSounds.add(sound);
            }
        }

        assertTrue(
                cutOffSounds.size() + " sounds will get cut off on PipeWire\n" +
                cutOffSounds.stream().map(s -> s.name() + ": " + s.getResourceName()).collect(Collectors.joining("\n")) + "\n",
                cutOffSounds.isEmpty()
        );
    }

    private double getDurationInSeconds(Sound sound) throws UnsupportedAudioFileException, IOException {
        File file = SoundFileManager.getSoundFile(sound);
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
        AudioFormat format = audioInputStream.getFormat();
        long frames = audioInputStream.getFrameLength();
        return (double) frames / format.getFrameRate();
    }
}
