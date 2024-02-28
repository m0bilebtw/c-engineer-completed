package com.github.dappermickie.odablock.sounds;

import com.github.dappermickie.odablock.OdablockConfig;
import static com.github.dappermickie.odablock.OdablockPlugin.ODABLOCK;
import com.github.dappermickie.odablock.SnowballUserManager;
import com.github.dappermickie.odablock.Sound;
import com.github.dappermickie.odablock.SoundEngine;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Actor;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.Player;
import net.runelite.api.Projectile;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.PlayerDespawned;
import net.runelite.api.events.PlayerSpawned;
import net.runelite.api.events.ProjectileMoved;
import net.runelite.client.util.Text;

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

	private Random random = new Random();

	private HashMap<String, Player> playerDictionary = new HashMap<>();


	public void onProjectileMoved(ProjectileMoved projectileMoved)
	{
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

		if (!config.snowballed())
		{
			return;
		}

		for (Player player : playerDictionary.values())
		{
			WorldPoint playerWp = player.getWorldLocation();
			WorldPoint projectileWP = WorldPoint.fromLocal(client, projectile.getX1(), projectile.getY1(), playerWp.getPlane()); // we don't care about plane

			// check snowball is *roughly* from the players tile, while allowing for drive-by/moving while the projectile spawns
			if (playerWp.distanceTo2D(projectileWP) <= 2)
			{
				lastSnowballTriggerTick = currentTick;
				soundEngine.playClip(Sound.SNOWBALL_SOUNDS[random.nextInt(Sound.SNOWBALL_SOUNDS.length)], executor);
				if (config.showChatMessages())
				{
					client.addChatMessage(ChatMessageType.PUBLICCHAT, ODABLOCK, player.getName() + " already kicked you!", null);
				}
			}
		}
	}

	public void onPlayerSpawned(PlayerSpawned playerSpawned)
	{
		Player player = playerSpawned.getPlayer();
		String[] users = SnowballUserManager.getUsers();
		for (String user : users)
		{
			if (user.equals(Text.standardize(player.getName())))
			{
				playerDictionary.put(user, player);
				return;
			}
		}
	}

	public void onPlayerDespawned(PlayerDespawned playerDespawned)
	{
		Player player = playerDespawned.getPlayer();
		String[] users = SnowballUserManager.getUsers();
		for (String user : users)
		{
			if (user.equals(Text.standardize(player.getName())))
			{
				playerDictionary.remove(user);
			}
		}
	}
}
