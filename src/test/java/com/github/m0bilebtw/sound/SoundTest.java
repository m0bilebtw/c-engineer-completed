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

    /**
     * Bash snippets to also help in identifying and automatically fixing such files.
     * It's better to run these in the sounds folder in .runelite rather than the sounds branch of the repo,
     * because then you automatically ignore any previous revisions of a sound.
     * <p>
     * Identify sounds that will cut off:
     * <p>
     * {@code for f in *.wav; do duration=$(ffprobe -v error -show_entries format=duration -of default=noprint_wrappers=1:nokey=1 "$f"); echo "$f $duration"; done}
     * <p>
     * Create new sounds that are padded to prevent cut off where needed:
     * <p>
     * {@code for f in *.wav; do duration=$(ffprobe -v error -show_entries format=duration -of default=noprint_wrappers=1:nokey=1 "$f"); if (( $(echo "$duration >= 1.491 && $duration < 2" | bc -l) )); then padding=$(echo "2 - $duration" | bc -l); ffmpeg -i "$f" -af "apad=pad_dur=0$padding" -y "${f%.wav}_PADDED.wav"; fi; done}
     */
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
