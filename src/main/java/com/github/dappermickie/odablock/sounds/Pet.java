package com.github.dappermickie.odablock.sounds;

import com.github.dappermickie.odablock.OdablockConfig;
import com.github.dappermickie.odablock.Sound;
import com.github.dappermickie.odablock.SoundEngine;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.events.ChatMessage;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.concurrent.ScheduledExecutorService;

@Singleton
@Slf4j
public class Pet
{

	@Inject
	private Client client;

	@Inject
	private OdablockConfig config;

	@Inject
	private SoundEngine soundEngine;

	@Inject
	private ScheduledExecutorService executor;

	public int receivedPetTick = -1;
	private final String normalPetMessage = "You have a funny feeling like you're being followed.";
	private final String backpackPetMessage = "You feel something weird sneaking into your backpack.";
	private final String wouldHavePetMessage = "You have a funny feeling like you would have been followed...";

	public boolean onChatMessage(ChatMessage chatMessage)
	{
		if (!config.receivedPet())
		{
			return false;
		}
		String message = chatMessage.getMessage();
		// Make sure it's one of the pet messages and not sent by a user to troll
		if ((message.equals(normalPetMessage) || message.equals(backpackPetMessage) || message.equals(wouldHavePetMessage)) &&
			chatMessage.getName().equals(""))
		{
			receivedPetTick = client.getTickCount();
			soundEngine.playClip(Sound.NEW_PET, executor);
			return true;
		}

		return false;
	}

}
