package com.github.dappermickie.odablock.sounds;


import com.github.dappermickie.odablock.OdablockConfig;
import com.github.dappermickie.odablock.PlayerKillLineManager;
import com.github.dappermickie.odablock.Sound;
import com.github.dappermickie.odablock.SoundEngine;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.Player;
import net.runelite.api.events.ChatMessage;
import net.runelite.client.util.Text;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Random;
import java.util.concurrent.ScheduledExecutorService;
import java.util.regex.Pattern;

import static com.github.dappermickie.odablock.OdablockPlugin.ODABLOCK;

@Singleton
@Slf4j
public class KillingPlayer
{

	@Inject
	private Client client;

	@Inject
	private OdablockConfig config;

	@Inject
	private SoundEngine soundEngine;

	@Inject
	private ScheduledExecutorService executor;

	private final String message = "Good shit ma brotha don't forget your key!";



	public boolean onChatMessage(ChatMessage chatMessage)
	{
		Player local = client.getLocalPlayer();
		String standardized = Text.standardize(chatMessage.getMessage());
		// Player Kill message checks
		if (config.playerKilling() &&
			chatMessage.getName().equals(""))
		{
			Pattern[] patterns = PlayerKillLineManager.getPatterns();
			for (Pattern pattern : patterns)
			{
				if (pattern.matcher(standardized).matches())
				{
					if (config.showChatMessages())
					{
						client.addChatMessage(ChatMessageType.PUBLICCHAT, ODABLOCK, message, null);
					}
					soundEngine.playClip(Sound.PLAYER_KILLING_SOUNDS, executor);
					return true;
				}
			}
		}
		return false;
	}
}
