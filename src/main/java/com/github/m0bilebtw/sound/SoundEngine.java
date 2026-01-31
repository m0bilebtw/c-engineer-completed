package com.github.m0bilebtw.sound;

import com.github.m0bilebtw.CEngineerCompletedConfig;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.audio.AudioPlayer;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.time.Duration;
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

    public void playClip(Sound sound, Executor executor) {
        executor.execute(() -> playClip(sound));
    }

    public void playClip(Sound sound, ScheduledExecutorService executor, Duration initialDelay) {
        executor.schedule(() -> playClip(sound), initialDelay.toMillis(), TimeUnit.MILLISECONDS);
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
