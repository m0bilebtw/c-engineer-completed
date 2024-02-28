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
public class AgsSpec extends TimedSoundBase
{

	@Inject
	private Client client;

	@Inject
	private OdablockConfig config;

	@Inject
	private SoundEngine soundEngine;

	@Inject
	private ScheduledExecutorService executor;

	private final String message = "OGS!";

	AgsSpec()
	{
		super(4);
	}

	public void onTick(int currentTick, Player local)
	{
		if (config.agsSpec())
		{
			boolean agsSpecced = local.hasSpotAnim(AnimationIds.AGS_SPEC.Id);
			if (agsSpecced)
			{
				if (canPlaySound(currentTick))
				{
					if (config.showChatMessages())
					{
						client.addChatMessage(ChatMessageType.PUBLICCHAT, ODABLOCK, message, null);
					}
					soundEngine.playClip(Sound.AGS_SPEC_SOUNDS, executor);
				}
			}
		}
	}

	public void onSoundEffectPlayed(SoundEffectPlayed event)
	{
		int soundId = event.getSoundId();
		final Player local = client.getLocalPlayer();

		if (config.agsSpec())
		{
			if (soundId == SoundIds.AGS_SPEC.Id)
			{
				if (local == event.getSource())
				{
					event.consume();
					return;
				}
				else if (!config.ownPlayerOnly())
				{
					soundEngine.playClip(Sound.AGS_SPEC_SOUNDS, executor);
					event.consume();
					return;
				}
			}
		}
	}
}
