package com.github.dappermickie.odablock.sounds;


import com.github.dappermickie.odablock.OdablockConfig;
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

	// PK Patterns
	private static final Pattern PK_PATTERN_1 = Pattern.compile("you were clearly a better fighter than.*");
	private static final Pattern PK_PATTERN_2 = Pattern.compile("can anyone defeat you\\? certainly not .*");
	private static final Pattern PK_PATTERN_3 = Pattern.compile("with a crushing blow you finish.*");
	private static final Pattern PK_PATTERN_4 = Pattern.compile("what an emberrassing performance by.*");
	private static final Pattern PK_PATTERN_5 = Pattern.compile(".*falls before your might.*");
	private static final Pattern PK_PATTERN_6 = Pattern.compile("what was.*thinking challenging you.*");
	private static final Pattern PK_PATTERN_7 = Pattern.compile("rip.*");
	private static final Pattern PK_PATTERN_8 = Pattern.compile(".*was no match for you.*");
	private static final Pattern PK_PATTERN_9 = Pattern.compile("you have defeated.*");
	private static final Pattern PK_PATTERN_10 = Pattern.compile("a humiliating defeat for.*");
	private static final Pattern PK_PATTERN_11 = Pattern.compile(".*didn’t stand a chance against you.*");

	public void onChatMessage(ChatMessage chatMessage)
	{
		Player local = client.getLocalPlayer();
		String standardized = Text.standardize(chatMessage.getMessage());
		// Player Kill message checks
		if (config.playerKilling() &&
			(PK_PATTERN_1.matcher(standardized).matches() ||
				PK_PATTERN_2.matcher(standardized).matches() ||
				PK_PATTERN_3.matcher(standardized).matches() ||
				PK_PATTERN_4.matcher(standardized).matches() ||
				PK_PATTERN_5.matcher(standardized).matches() ||
				PK_PATTERN_6.matcher(standardized).matches() ||
				PK_PATTERN_7.matcher(standardized).matches() ||
				PK_PATTERN_8.matcher(standardized).matches() ||
				PK_PATTERN_9.matcher(standardized).matches() ||
				PK_PATTERN_10.matcher(standardized).matches() ||
				PK_PATTERN_11.matcher(standardized).matches()))
		{

			if (config.showChatMessages())
			{
				client.addChatMessage(ChatMessageType.PUBLICCHAT, ODABLOCK, message, null);
			}
			soundEngine.playClip(Sound.PLAYER_KILLING_SOUNDS, executor);
		}
	}
}
