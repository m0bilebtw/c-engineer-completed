package com.github.dappermickie.odablock.sounds;

import com.github.dappermickie.odablock.OdablockConfig;
import com.github.dappermickie.odablock.RandomSoundUtility;
import com.github.dappermickie.odablock.Sound;
import com.github.dappermickie.odablock.SoundEngine;
import java.util.Random;
import java.util.concurrent.ScheduledExecutorService;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameTick;
import net.runelite.client.util.Text;

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

	private static final Pattern SPECIAL_DROP_MESSAGE = Pattern.compile("(.+) - (.+)");
	private int endedRaidTick = -1;
	private boolean isWhiteLight = true;

	public boolean onChatMessage(ChatMessage chatMessage)
	{
		if (!config.coxSounds())
		{
			return false;
		}
		if (client.getLocalPlayer() == null || client.getLocalPlayer().getName() == null)
		{
			return false;
		}

		if (chatMessage.getType() == ChatMessageType.FRIENDSCHATNOTIFICATION)
		{
			String message = Text.removeTags(chatMessage.getMessage());

			if (message.contains("your raid is complete!"))
			{
				isWhiteLight = true;
				endedRaidTick = client.getTickCount();
				return true;
			}

			Matcher matcher = SPECIAL_DROP_MESSAGE.matcher(message);

			if (matcher.find())
			{
				final String dropReceiver = Text.sanitize(matcher.group(1)).trim();
				// Maybe we can play a different sound if it's a twisted bow?
				final String dropName = matcher.group(2).trim();

				// We might want to play a different sound if you're the one receiving the purple
				if (dropReceiver.equals(Text.sanitize(client.getLocalPlayer().getName())))
				{
					isWhiteLight = false;
				}
				else
				{
					isWhiteLight = false;
				}
				return true;
			}
		}

		return false;
	}

	public void onGameTick(GameTick event)
	{
		if (endedRaidTick != -1 && client.getTickCount() - endedRaidTick == 2)
		{
			if (isWhiteLight)
			{
				soundEngine.playClip(Sound.WHITE_LIGHT_AFTER_RAID, executor);
			}
			else
			{
				soundEngine.playClip(Sound.PLAYER_KILLING_SOUNDS, executor);
			}
		}
	}
}
