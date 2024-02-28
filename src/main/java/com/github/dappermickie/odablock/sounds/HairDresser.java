package com.github.dappermickie.odablock.sounds;

import com.github.dappermickie.odablock.OdablockConfig;
import com.github.dappermickie.odablock.RandomSoundUtility;
import com.github.dappermickie.odablock.Sound;
import com.github.dappermickie.odablock.SoundEngine;
import java.util.Random;
import java.util.concurrent.ScheduledExecutorService;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.coords.WorldArea;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.WidgetLoaded;

@Singleton
@Slf4j
public class HairDresser
{
	@Inject
	private Client client;

	@Inject
	private OdablockConfig config;

	@Inject
	private SoundEngine soundEngine;

	@Inject
	private ScheduledExecutorService executor;

	private static final WorldArea FALADOR_HAIRDRESSER = new WorldArea(new WorldPoint(2942, 3377, 0), 8, 12);
	private static final int FALADOR_HAIRCUT_WIDGET_GROUP_ID = 516;

	public void onWidgetLoaded(WidgetLoaded event)
	{
		if (!config.hairDresser())
		{
			return;
		}

		if (event.getGroupId() == FALADOR_HAIRCUT_WIDGET_GROUP_ID)
		{
			// getting the haircut widget via IDs etc seems overly difficult, so just check location
			WorldPoint currentLocation = client.getLocalPlayer().getWorldLocation();
			if (FALADOR_HAIRDRESSER.contains(currentLocation))
			{
				soundEngine.playClip(Sound.HAIRDRESSER_SOUNDS, executor);
			}
			return;
		}
	}
}
