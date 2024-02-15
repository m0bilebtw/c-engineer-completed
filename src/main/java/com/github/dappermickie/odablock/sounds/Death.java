package com.github.dappermickie.odablock.sounds;

import com.github.dappermickie.odablock.OdablockConfig;
import com.github.dappermickie.odablock.Sound;
import com.github.dappermickie.odablock.SoundEngine;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.events.ActorDeath;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.concurrent.ScheduledExecutorService;

import static com.github.dappermickie.odablock.OdablockPlugin.ODABLOCK;

@Singleton
@Slf4j
public class Death
{

	@Inject
	private Client client;

	@Inject
	private OdablockConfig config;

	@Inject
	private SoundEngine soundEngine;

	@Inject
	private ScheduledExecutorService executor;


	public void onActorDeath(ActorDeath actorDeath)
	{
		if (config.announceDeath() && actorDeath.getActor() == client.getLocalPlayer())
		{
			if (config.showChatMessages())
			{
				//TODO: Add different chat message
				client.addChatMessage(ChatMessageType.PUBLICCHAT, ODABLOCK, "Dying on my HCIM: completed.", null);
			}
			soundEngine.playClip(Sound.DEATH, executor);
		}
	}
}
