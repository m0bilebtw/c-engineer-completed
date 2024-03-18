package com.github.dappermickie.odablock.sounds;

// Special thanks to https://github.com/Maurits825/tob-light-colors

import com.github.dappermickie.odablock.OdablockConfig;
import com.github.dappermickie.odablock.Sound;
import com.github.dappermickie.odablock.SoundEngine;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.Varbits;
import net.runelite.api.events.GameObjectDespawned;
import net.runelite.api.events.GameObjectSpawned;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.VarbitChanged;
import net.runelite.client.callback.ClientThread;

@Singleton
@Slf4j
public class TobChestLight
{

	@Inject
	private Client client;

	@Inject
	private OdablockConfig config;

	@Inject
	private SoundEngine soundEngine;

	@Inject
	private ScheduledExecutorService executor;

	@Inject
	private ClientThread clientThread;

	private static final int YOUR_TOB_CHEST_PURPLE_OBJ = 32993;
	private static final int YOUR_TOB_CHEST_NORMAL_OBJ = 32992;
	private static final int OTHER_TOB_CHEST_PURPLE_OBJ = 32991;

	private static final List<Integer> REWARD_CHEST_IDS = Arrays.asList(33086, 33087, 33088, 33089, 33090);

	private boolean isPurple = false;
	private boolean isMine = false;
	private boolean inRaid = false;
	private boolean loadedPlayers = false;
	private int playerCount = 0;
	private int loadedObjectCount = 0;


	public static final int THEATRE_RAIDERS_VARP = 330;
	public static final int MAX_RAIDERS = 5;

	public static final int STATE_NO_PARTY = 0;
	public static final int STATE_IN_PARTY = 1;

	int raidState;

	public void onGameObjectSpawned(GameObjectSpawned event)
	{
		if (!config.tobPurpleChest() && !config.tobWhiteChest())
		{
			return;
		}

		int objId = event.getGameObject().getId();
		if (REWARD_CHEST_IDS.contains(objId))
		{
			int impostorId = client.getObjectDefinition(objId).getImpostor().getId();

			if (impostorId == YOUR_TOB_CHEST_PURPLE_OBJ)
			{
				isPurple = true;
				isMine = true;
			}
			else if (impostorId == OTHER_TOB_CHEST_PURPLE_OBJ)
			{
				isPurple = true;
				isMine = false;
			}
			else if (impostorId == YOUR_TOB_CHEST_NORMAL_OBJ)
			{
				isMine = false;
			}
			loadedObjectCount++;

			if (loadedObjectCount == playerCount)
			{
				if (isPurple)
				{
					if (config.tobPurpleChest())
					{
						// TODO: Maybe change sound if it's yours
						if (isMine)
						{
							soundEngine.playClip(Sound.GETTING_PURPLE_SOUNDS, executor);
						}
						else
						{
							soundEngine.playClip(Sound.GETTING_PURPLE_SOUNDS, executor);
						}
					}
				}
				else if (config.tobWhiteChest())
				{
					soundEngine.playClip(Sound.WHITE_LIGHT_AFTER_RAID, executor);
				}
			}
		}
	}

	public void onGameTick(GameTick event)
	{
		if (inRaid && !loadedPlayers)
		{
			Map<Integer, Object> varcmap = client.getVarcMap();
			for (int i = 0; i < MAX_RAIDERS; i++)
			{
				Integer playervarp = THEATRE_RAIDERS_VARP + i;
				if (varcmap.containsKey(playervarp) && !varcmap.get(playervarp).equals(""))
				{
					playerCount++;
				}
			}

			loadedPlayers = true;
		}
	}

	// Yoinked from https://github.com/Adam-/runelite-plugins/blob/tob-drop-chance/src/main/java/com/tobdropchance/TobDropChancePlugin.java
	public void onVarbitChanged(VarbitChanged event)
	{
		int nextState = client.getVarbitValue(Varbits.THEATRE_OF_BLOOD);
		if (raidState != nextState)
		{
			if (nextState == STATE_NO_PARTY || nextState == STATE_IN_PARTY)
			{ // Player is not in a raid.
				reset();
				raidState = nextState;
			}
			else
			{ // Player has entered the theatre.
				if (raidState == STATE_IN_PARTY)
				{ // Player was in a party. They are a raider.
					reset();
					inRaid = true;
				}

				raidState = nextState;
			}
		}
	}

	private void reset()
	{
		isPurple = false;
		isMine = false;
		inRaid = false;
		loadedPlayers = false;
		playerCount = 0;
		loadedObjectCount = 0;
	}

	public void onGameObjectDespawned(GameObjectDespawned event)
	{
		int objId = event.getGameObject().getId();
		if (REWARD_CHEST_IDS.contains(objId))
		{
			isMine = false;
			isPurple = false;
			loadedObjectCount--;
		}
	}
}