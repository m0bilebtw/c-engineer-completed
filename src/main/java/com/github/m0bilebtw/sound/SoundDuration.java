package com.github.m0bilebtw.sound;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Works out how long a sound lasts by reading the wav header, so an announcement can be held back until the previous
 * one has actually finished speaking.
 * <p>
 * This reads the header by hand rather than asking javax.sound, to keep those imports in the tests where they already
 * live. A wav header is a chunk list: the "fmt " chunk gives the bytes per second, the "data" chunk gives how many
 * bytes of audio follow, and one divided by the other is the length.
 */
@Slf4j
class SoundDuration {

    /** Returned when the length cannot be worked out, e.g. the sound has not been downloaded yet. */
    static final long UNKNOWN = 0;

    static long millisFor(File file) {
        try (DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(file)))) {
            if (!"RIFF".equals(readChunkId(in)))
                return UNKNOWN;

            skipFully(in, 4); // total file size, which we do not need
            if (!"WAVE".equals(readChunkId(in)))
                return UNKNOWN;

            long bytesPerSecond = UNKNOWN;
            while (true) {
                String chunkId = readChunkId(in);
                long chunkSize = readLittleEndianInt(in);
                if (chunkId == null || chunkSize < 0)
                    return UNKNOWN;

                if ("fmt ".equals(chunkId)) {
                    skipFully(in, 8); // encoding, channel count and sample rate, all implied by the byte rate below
                    bytesPerSecond = readLittleEndianInt(in);
                    skipFully(in, chunkSize - 12);
                } else if ("data".equals(chunkId)) {
                    return bytesPerSecond > 0 ? Math.round(chunkSize * 1000d / bytesPerSecond) : UNKNOWN;
                } else {
                    skipFully(in, chunkSize);
                }

                if (chunkSize % 2 != 0)
                    skipFully(in, 1); // chunks are padded to an even number of bytes
            }
        } catch (Exception e) {
            log.debug("Could not read the length of {}", file, e);
            return UNKNOWN;
        }
    }

    private static String readChunkId(DataInputStream in) throws IOException {
        byte[] id = new byte[4];
        in.readFully(id);
        return new String(id, StandardCharsets.US_ASCII);
    }

    private static long readLittleEndianInt(DataInputStream in) throws IOException {
        return (in.readUnsignedByte())
                | ((long) in.readUnsignedByte() << 8)
                | ((long) in.readUnsignedByte() << 16)
                | ((long) in.readUnsignedByte() << 24);
    }

    private static void skipFully(DataInputStream in, long bytes) throws IOException {
        long remaining = bytes;
        while (remaining > 0) {
            long skipped = in.skip(remaining);
            if (skipped <= 0) {
                if (in.read() < 0)
                    throw new IOException("Unexpected end of file with " + remaining + " bytes left to skip");
                skipped = 1;
            }
            remaining -= skipped;
        }
    }
}
