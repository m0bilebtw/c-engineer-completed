package com.github.dappermickie.odablock.sounds;

import com.github.dappermickie.odablock.OdablockConfig;
import com.github.dappermickie.odablock.Sound;
import com.github.dappermickie.odablock.SoundEngine;
import java.util.concurrent.ScheduledExecutorService;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.Model;
import net.runelite.api.WallObject;
import net.runelite.api.events.WallObjectSpawned;

@Singleton
@Slf4j
public class ToaChestLight
{
	@Inject
	private Client client;

	@Inject
	private OdablockConfig config;

	@Inject
	private SoundEngine soundEngine;

	@Inject
	private ScheduledExecutorService executor;

	private static final int VARBIT_VALUE_CHEST_KEY = 2;
	private static final int VARBIT_ID_SARCOPHAGUS = 14373;
	private static final int WALL_OBJECT_ID_SARCOPHAGUS = 46221;

	private static final int[] VARBIT_MULTILOC_IDS_CHEST = new int[]{
		14356, 14357, 14358, 14359, 14360, 14370, 14371, 14372
	};

	private boolean sarcophagusIsPurple;
	private boolean purpleIsMine = true;

	public void onWallObjectSpawned(final WallObjectSpawned event)
	{
		final WallObject wallObject = event.getWallObject();

		if (!config.enableToaChest() || wallObject.getId() != WALL_OBJECT_ID_SARCOPHAGUS)
		{
			return;
		}

		parseVarbits();

		if (sarcophagusIsPurple)
		{
			// TODO: Maybe change the sound if it's not your purple?
			if (purpleIsMine)
			{
				soundEngine.playClip(Sound.GETTING_PURPLE, executor);
			}
			else
			{
				soundEngine.playClip(Sound.GETTING_PURPLE, executor);
			}
		}
		else
		{
			soundEngine.playClip(Sound.WHITE_LIGHT_AFTER_RAID, executor);
		}
	}

	private void parseVarbits()
	{
		sarcophagusIsPurple = client.getVarbitValue(VARBIT_ID_SARCOPHAGUS) % 2 != 0;
		purpleIsMine = true;

		for (final int varbitId : VARBIT_MULTILOC_IDS_CHEST)
		{
			if (client.getVarbitValue(varbitId) == VARBIT_VALUE_CHEST_KEY)
			{
				purpleIsMine = false;
				break;
			}
		}
	}
}
