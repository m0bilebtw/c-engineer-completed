package com.github.m0bilebtw.sound;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import static org.junit.Assert.assertEquals;

public class SoundDurationTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    private static final int BYTES_PER_SECOND = 88200; // 44.1kHz, 16 bit, stereo

    @Test
    public void readsTheLengthFromTheWavHeader() throws IOException {
        File twoAndAHalfSeconds = wavOf((int) (BYTES_PER_SECOND * 2.5), false);

        assertEquals(2500, SoundDuration.millisFor(twoAndAHalfSeconds));
    }

    @Test
    public void readsPastChunksItDoesNotCareAbout() throws IOException {
        File withExtraChunk = wavOf(BYTES_PER_SECOND, true);

        assertEquals(1000, SoundDuration.millisFor(withExtraChunk));
    }

    @Test
    public void reportsUnknownForAFileThatIsNotAWav() throws IOException {
        File notAWav = folder.newFile("nonsense.wav");
        Files.write(notAWav.toPath(), "this is not a wav file at all".getBytes(StandardCharsets.US_ASCII));

        assertEquals(SoundDuration.UNKNOWN, SoundDuration.millisFor(notAWav));
    }

    @Test
    public void reportsUnknownForAMissingFile() {
        assertEquals(SoundDuration.UNKNOWN, SoundDuration.millisFor(new File(folder.getRoot(), "never_downloaded.wav")));
    }

    /**
     * A minimal PCM wav: a RIFF/WAVE wrapper, a "fmt " chunk carrying the byte rate, and a "data" chunk whose declared
     * size is what the length is worked out from. An odd sized chunk in between checks the padding is honoured.
     */
    private File wavOf(int dataBytes, boolean includeAnOddSizedExtraChunk) throws IOException {
        ByteArrayOutputStream body = new ByteArrayOutputStream();

        body.write("WAVE".getBytes(StandardCharsets.US_ASCII));

        body.write("fmt ".getBytes(StandardCharsets.US_ASCII));
        writeLittleEndianInt(body, 16);
        writeLittleEndianShort(body, 1); // PCM
        writeLittleEndianShort(body, 2); // channels
        writeLittleEndianInt(body, 44100);
        writeLittleEndianInt(body, BYTES_PER_SECOND);
        writeLittleEndianShort(body, 4); // block align
        writeLittleEndianShort(body, 16); // bits per sample

        if (includeAnOddSizedExtraChunk) {
            body.write("LIST".getBytes(StandardCharsets.US_ASCII));
            writeLittleEndianInt(body, 3);
            body.write(new byte[]{'a', 'b', 'c'});
            body.write(0); // padding byte
        }

        body.write("data".getBytes(StandardCharsets.US_ASCII));
        writeLittleEndianInt(body, dataBytes);
        // the audio itself is never read, only its declared size, so it does not need to be written out

        ByteArrayOutputStream wav = new ByteArrayOutputStream();
        wav.write("RIFF".getBytes(StandardCharsets.US_ASCII));
        writeLittleEndianInt(wav, body.size() + dataBytes);
        body.writeTo(wav);

        File file = folder.newFile(dataBytes + (includeAnOddSizedExtraChunk ? "-extra" : "") + ".wav");
        Files.write(file.toPath(), wav.toByteArray());
        return file;
    }

    private static void writeLittleEndianInt(ByteArrayOutputStream out, int value) {
        out.write(value & 0xFF);
        out.write((value >> 8) & 0xFF);
        out.write((value >> 16) & 0xFF);
        out.write((value >> 24) & 0xFF);
    }

    private static void writeLittleEndianShort(ByteArrayOutputStream out, int value) {
        out.write(value & 0xFF);
        out.write((value >> 8) & 0xFF);
    }
}
