package com.github.dappermickie.odablock.sounds;

import com.github.dappermickie.odablock.OdablockConfig;
import com.github.dappermickie.odablock.Sound;
import com.github.dappermickie.odablock.SoundEngine;
import java.util.concurrent.ScheduledExecutorService;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.events.VarbitChanged;

@Singleton
@Slf4j
public class Vengeance
{

	@Inject
	private Client client;

	@Inject
	private OdablockConfig config;

	@Inject
	private SoundEngine soundEngine;

	@Inject
	private ScheduledExecutorService executor;


	public void onVarbitChanged(VarbitChanged event)
	{
		final int varbitId = event.getVarbitId();
		final int varpId = event.getVarpId();
		final int value = event.getValue();

		if (config.vengeance() && varbitId == 2450 && varpId == 439 && value == 1)
		{
			soundEngine.playClip(Sound.VENGEANCE, executor);
		}
	}
}
