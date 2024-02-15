package com.github.dappermickie.odablock.sounds;

import com.github.dappermickie.odablock.OdablockConfig;
import com.github.dappermickie.odablock.OdablockPlugin;
import com.github.dappermickie.odablock.Sound;
import com.github.dappermickie.odablock.SoundEngine;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.MenuAction;
import net.runelite.api.Player;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.util.Text;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.concurrent.ScheduledExecutorService;

@Singleton
@Slf4j
public class PetDog
{

	@Inject
	private Client client;

	@Inject
	private OdablockConfig config;

	@Inject
	private SoundEngine soundEngine;

	@Inject
	private ScheduledExecutorService executor;
	private final String petOption = "Pet";
	private final String messageByClient = "who's a good doggy!";

	private int lastPetDogTick = -1;

	public boolean onChatMessage(ChatMessage chatMessage)
	{
		int currentTick = client.getTickCount();
		Player local = client.getLocalPlayer();
		// Petting the dog sound, only play sound when message is sent by local player within 15ticks of petting the dog
		if (config.petDog() &&
			// pet dog message
			Text.standardize(chatMessage.getMessage()).equals(messageByClient) &&
			// sent by current local user
			Text.standardize(chatMessage.getName()).equals(Text.standardize(local.getName())))
		{
			// Check if last pet dog click is within 15 ticks
			if (lastPetDogTick != -1 && currentTick - lastPetDogTick < 15)
			{
				soundEngine.playClip(Sound.PETTING_DOG, executor);
				return true;
			}
		}
		return false;
	}

	public void onMenuOptionClicked(MenuOptionClicked menuOptionClicked)
	{
		int currentTick = client.getTickCount();

		String option = menuOptionClicked.getMenuOption();
		MenuAction action = menuOptionClicked.getMenuAction();


		// Petting the dog
		if (config.petDog() && menuOptionClicked.getId() == 23766 && option.equals(petOption))
		{
			lastPetDogTick = currentTick;
		}
	}

	public void cleanupTicks(int currentTick)
	{
		if (lastPetDogTick != currentTick &&
			currentTick - lastPetDogTick > 15)
		{
			lastPetDogTick = -1;
		}
	}
}
