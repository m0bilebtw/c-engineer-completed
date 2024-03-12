package com.github.dappermickie.odablock;

import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Singleton
@Slf4j
public class SoundEngine
{

	@Inject
	private OdablockConfig config;

	private static final long CLIP_MTIME_UNLOADED = -2;

	private long lastClipMTime = CLIP_MTIME_UNLOADED;
	private Clip clip = null;

	private boolean loadClip(Sound sound)
	{
		try (InputStream stream = new BufferedInputStream(SoundFileManager.getSoundStream(sound));
			 AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(stream))
		{
			clip.open(audioInputStream); // liable to error with pulseaudio, works on windows, one user informs me mac works
			return true;
		}
		catch (UnsupportedAudioFileException | IOException | LineUnavailableException e)
		{
			log.warn("Failed to load Odablock sound " + sound, e);
		}
		return false;
	}

	public void playClip(Sound[] sounds, Executor executor)
	{
		Sound sound = RandomSoundUtility.getRandomSound(sounds);
		playClip(sound, executor);
	}

	public void playClip(Sound sound, Executor executor)
	{
		executor.execute(() -> playClip(sound));
	}

	public void playClip(Sound sound, ScheduledExecutorService executor, Duration initialDelay)
	{
		executor.schedule(() -> playClip(sound), initialDelay.toMillis(), TimeUnit.MILLISECONDS);
	}

	private void playClip(Sound sound)
	{
		if (SoundFileManager.getIsUpdating())
		{
			return;
		}
		long currentMTime = System.currentTimeMillis();
		if (clip == null || currentMTime != lastClipMTime || !clip.isOpen())
		{
			if (clip != null && clip.isOpen())
			{
				clip.close();
			}

			try
			{
				clip = AudioSystem.getClip();
			}
			catch (LineUnavailableException e)
			{
				lastClipMTime = CLIP_MTIME_UNLOADED;
				log.warn("Failed to get clip for C Engineer sound " + sound, e);
				return;
			}

			lastClipMTime = currentMTime;
			if (!loadClip(sound))
			{
				return;
			}
		}

		// User configurable volume
		FloatControl volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
		float gain = 20f * (float) Math.log10(config.announcementVolume() / 100f);
		gain = Math.min(gain, volume.getMaximum());
		gain = Math.max(gain, volume.getMinimum());
		volume.setValue(gain);

		// From RuneLite base client Notifier class:
		// Using loop instead of start + setFramePosition prevents the clip
		// from not being played sometimes, presumably a race condition in the
		// underlying line driver
		clip.loop(0);
	}

	public void close()
	{
		if (clip != null && clip.isOpen())
		{
			clip.close();
		}
	}
}