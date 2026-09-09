package com.github.m0bilebtw.sound;

import com.github.m0bilebtw.CEngineerCompletedConfig;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.audio.AudioPlayer;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Singleton
@Slf4j
public class SoundEngine {

    @Inject
    private CEngineerCompletedConfig config;

    @Inject
    private AudioPlayer audioPlayer;

    /**
     * The same executor the triggers hand over, injected here as well because the queue has to schedule work of its
     * own: closing the window it collects announcements in, and coming back when a sound has finished playing.
     */
    @Inject
    private ScheduledExecutorService executor;

    private final Map<Sound, Long> clipLengths = new ConcurrentHashMap<>();

    private volatile AnnouncementQueue queue;

    public void playClip(Sound sound, Executor executor) {
        if (!config.avoidOverlappingAnnouncements()) {
            executor.execute(() -> playClip(sound));
            return;
        }

        queue().request(sound);
    }

    public void playClip(Sound sound, ScheduledExecutorService executor, Duration initialDelay) {
        executor.schedule(() -> playClip(sound, executor), initialDelay.toMillis(), TimeUnit.MILLISECONDS);
    }

    /**
     * Plays without waiting for anything else, for the config panel's sound picker: the whole point of choosing a sound
     * there is to hear that sound, right then.
     */
    public void playClipImmediately(Sound sound, Executor executor) {
        executor.execute(() -> playClip(sound));
    }

    private AnnouncementQueue queue() {
        AnnouncementQueue existing = queue;
        if (existing != null)
            return existing;

        synchronized (this) {
            if (queue == null) {
                queue = new AnnouncementQueue(
                        sound -> executor.execute(() -> playClip(sound)),
                        System::currentTimeMillis,
                        (delayMillis, task) -> executor.schedule(task, delayMillis, TimeUnit.MILLISECONDS),
                        this::clipLengthMillis
                );
            }
            return queue;
        }
    }

    private long clipLengthMillis(Sound sound) {
        Long known = clipLengths.get(sound);
        if (known != null)
            return known;

        long measured = SoundDuration.millisFor(SoundFileManager.getSoundFile(sound));
        if (measured > SoundDuration.UNKNOWN)
            clipLengths.put(sound, measured); // a sound that is not downloaded yet is worth measuring again later

        return measured;
    }

    private void playClip(Sound sound) {
        float gain = 20f * (float) Math.log10(config.announcementVolume() / 100f);

        try {
            audioPlayer.play(SoundFileManager.getSoundFile(sound), gain);
        } catch (Exception e) {
            log.warn("Failed to load C Engineer sound {}", sound, e);
        }
    }
}
