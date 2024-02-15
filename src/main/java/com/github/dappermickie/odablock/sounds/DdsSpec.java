package com.github.dappermickie.odablock.sounds;

import com.github.dappermickie.odablock.*;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.Player;
import net.runelite.api.events.SoundEffectPlayed;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.concurrent.ScheduledExecutorService;

import static com.github.dappermickie.odablock.OdablockPlugin.ODABLOCK;

@Singleton
@Slf4j
public class DdsSpec extends TimedSoundBase
{

	@Inject
	private Client client;

	@Inject
	private OdablockConfig config;

	@Inject
	private SoundEngine soundEngine;

	@Inject
	private ScheduledExecutorService executor;

	private static final String message = "ZEW ZEW.";

	DdsSpec()
	{
		super(2);
	}

	public void onTick(int currentTick, Player local)
	{
		if (config.ddsSpec())
		{
			boolean ddsSpecced = local.hasSpotAnim(AnimationIds.DDS_SPEC.Id);
			if (ddsSpecced)
			{
				if (canPlaySound(currentTick))
				{
					if (config.showChatMessages())
					{
						//TODO: Add different chat message
						client.addChatMessage(ChatMessageType.PUBLICCHAT, ODABLOCK, message, null);
					}
					soundEngine.playClip(Sound.DDS_SPEC, executor);
					setLastPlayedTickTick(currentTick);
				}
			}
		}
	}

	public void onSoundEffectPlayed(SoundEffectPlayed event)
	{
		int soundId = event.getSoundId();
		if (config.ddsSpec())
		{
			if (soundId == SoundIds.DDS_SPEC.Id)
			{
				event.consume();

				//TODO FIX for not you
				if (!config.ownPlayerOnly() && canPlaySound(client.getTickCount()))
				{
					soundEngine.playClip(Sound.DDS_SPEC, executor);
				}
				return;
			}
		}
	}
}
