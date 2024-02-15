package com.github.dappermickie.odablock.sounds;

import com.github.dappermickie.odablock.OdablockConfig;
import com.github.dappermickie.odablock.Sound;
import com.github.dappermickie.odablock.SoundEngine;
import java.time.Duration;
import java.util.Random;
import java.util.concurrent.ScheduledExecutorService;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Actor;
import net.runelite.api.Client;
import net.runelite.api.Player;
import net.runelite.api.Projectile;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.PlayerDespawned;
import net.runelite.api.events.PlayerSpawned;
import net.runelite.api.events.ProjectileMoved;

@Singleton
@Slf4j
public class SnowBalled
{
	@Inject
	private Client client;

	@Inject
	private OdablockConfig config;

	@Inject
	private SoundEngine soundEngine;

	@Inject
	private ScheduledExecutorService executor;

	private int lastSnowballTriggerTick = -1;
	private final static String odablockRSN = "";

	private Player odablockPlayer = null;
	private Random random = new Random();


	public void onProjectileMoved(ProjectileMoved projectileMoved)
	{
		if (odablockPlayer == null)
		{
			return;
		}

		Projectile projectile = projectileMoved.getProjectile();
		if (projectile.getId() != /*snowball*/ 861)
		{
			return;
		}

		int currentTick = client.getTickCount();
		if (currentTick - lastSnowballTriggerTick < 50) // 30s cool down
		{
			return;
		}

		Actor myself = client.getLocalPlayer();
		if (myself == null)
		{
			return;
		}

		Actor projectileInteracting = projectile.getInteracting();
		if (!myself.equals(projectileInteracting))
		{
			return;
		}

		if (!config.easterEggs())
		{
			return;
		}

		WorldPoint odablockWP = odablockPlayer.getWorldLocation();
		WorldPoint projectileWP = WorldPoint.fromLocal(client, projectile.getX1(), projectile.getY1(), odablockWP.getPlane()); // we don't care about plane

		// check snowball is *roughly* from O's tile, while allowing for drive-by/moving while the projectile spawns
		if (odablockWP.distanceTo2D(projectileWP) <= 2)
		{
			lastSnowballTriggerTick = currentTick;
			soundEngine.playClip(Sound.SNOWBALL_SOUNDS[random.nextInt(Sound.SNOWBALL_SOUNDS.length)], executor, Duration.ofSeconds(10));
		}
	}

	public void onPlayerSpawned(PlayerSpawned playerSpawned)
	{
		Player player = playerSpawned.getPlayer();

		if (odablockRSN.equals(player.getName()))
		{
			odablockPlayer = player;
		}
	}

	public void onPlayerDespawned(PlayerDespawned playerDespawned)
	{
		Player player = playerDespawned.getPlayer();

		if (odablockRSN.equals(player.getName()))
		{
			odablockPlayer = null;
		}
	}
}
