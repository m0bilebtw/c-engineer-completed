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
import net.runelite.api.GameObject;
import net.runelite.api.events.GameObjectDespawned;
import net.runelite.api.events.GameObjectSpawned;

@Singleton
@Slf4j
public class CoxSounds
{

	@Inject
	private Client client;

	@Inject
	private OdablockConfig config;

	@Inject
	private SoundEngine soundEngine;

	@Inject
	private ScheduledExecutorService executor;

	private Random random = new Random();

	// YOINK from https://github.com/AnkouOSRS/cox-light-colors/blob/master/src/main/java/com/coxlightcolors/CoxLightColorsPlugin.java#L82

	private static final int LIGHT_OBJECT_ID = 28848;
	private GameObject lightObject;
	private static Integer currentLightType;
	private static final int VARBIT_LIGHT_TYPE = 5456;

	public void onGameObjectSpawned(GameObjectSpawned event)
	{
		if(!config.coxSounds()) {
			return;
		}
		GameObject obj = event.getGameObject();
		if (LIGHT_OBJECT_ID == obj.getId())
		{
			log.info("Light gameObject spawned");
			lightObject = obj;
			playSound();
		}
	}

	public void onGameObjectDespawned(GameObjectDespawned event)
	{
		if (LIGHT_OBJECT_ID == event.getGameObject().getId())
		{
			log.info("Light gameObject despawned");
			lightObject = null;
		}
	}

	private void playSound()
	{
		if (lightObject != null)
		{
			currentLightType = client.getVarbitValue(VARBIT_LIGHT_TYPE);
			if (currentLightType != 0)
			{
				soundEngine.playClip(Sound.GETTING_PURPLE_SOUNDS, executor);
			}
			else
			{
				soundEngine.playClip(Sound.WHITE_LIGHT_AFTER_RAID, executor);
			}
		}
	}
}
