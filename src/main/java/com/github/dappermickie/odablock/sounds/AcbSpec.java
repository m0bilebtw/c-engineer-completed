package com.github.dappermickie.odablock.sounds;

import com.github.dappermickie.odablock.OdablockConfig;
import com.github.dappermickie.odablock.RandomSoundUtility;
import com.github.dappermickie.odablock.Sound;
import com.github.dappermickie.odablock.SoundEngine;
import com.github.dappermickie.odablock.SoundIds;
import java.util.Random;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.Player;
import net.runelite.api.events.AreaSoundEffectPlayed;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.concurrent.ScheduledExecutorService;

import static com.github.dappermickie.odablock.OdablockPlugin.ODABLOCK;
import net.runelite.api.events.SoundEffectPlayed;

@Singleton
@Slf4j
public class AcbSpec
{

	@Inject
	private Client client;

	@Inject
	private OdablockConfig config;

	@Inject
	private SoundEngine soundEngine;

	@Inject
	private ScheduledExecutorService executor;
	private final String message = "ACB SPECCLE!";

	public void onAreaSoundEffectPlayed(AreaSoundEffectPlayed event)
	{
		int soundId = event.getSoundId();
		if (config.acbSpec())
		{
			if (soundId == SoundIds.ACB_SPEC.Id)
			{
				event.consume();
				soundEngine.playClip(Sound.ACB_SPEC_SOUNDS, executor);
				if (config.showChatMessages())
				{
					client.addChatMessage(ChatMessageType.PUBLICCHAT, ODABLOCK, message, null);
				}
				return;
			}
		}
	}

	public void onSoundEffectPlayed(SoundEffectPlayed event)
	{
		int soundId = event.getSoundId();
		final Player local = client.getLocalPlayer();

		if (config.acbSpec())
		{
			if (soundId == SoundIds.ACB_SPEC.Id)
			{
				event.consume();
				soundEngine.playClip(Sound.ACB_SPEC_SOUNDS, executor);
				if (config.showChatMessages())
				{
					client.addChatMessage(ChatMessageType.PUBLICCHAT, ODABLOCK, message, null);
				}
				return;
			}
		}
	}
}
