package com.github.dappermickie.odablock.sounds;

import com.github.dappermickie.odablock.OdablockConfig;
import com.github.dappermickie.odablock.Sound;
import com.github.dappermickie.odablock.SoundEngine;
import com.github.dappermickie.odablock.SoundIds;
import java.util.concurrent.ScheduledExecutorService;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.events.SoundEffectPlayed;

@Singleton
@Slf4j
public class ZebakRoar extends TimedSoundBase
{
	ZebakRoar()
	{
		super(10);
	}

	@Inject
	private Client client;

	@Inject
	private OdablockConfig config;

	@Inject
	private SoundEngine soundEngine;

	@Inject
	private ScheduledExecutorService executor;

	public void onSoundEffectPlayed(SoundEffectPlayed event)
	{
		int soundId = event.getSoundId();
		int currentTick = client.getTickCount();
		if (config.zebakRoar())
		{
			if (soundId == 5829)
			{
				event.consume();
				if (canPlaySound(currentTick))
				{
					setLastPlayedTickTick(currentTick);
					soundEngine.playClip(Sound.ZEBAK_ROAR, executor);
				}
			}
		}
	}
}
