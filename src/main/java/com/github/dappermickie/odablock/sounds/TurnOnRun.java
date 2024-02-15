package com.github.dappermickie.odablock.sounds;

import com.github.dappermickie.odablock.*;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.MenuAction;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.events.VarbitChanged;
import net.runelite.client.eventbus.Subscribe;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.concurrent.ScheduledExecutorService;

import static com.github.dappermickie.odablock.OdablockPlugin.ODABLOCK;

@Singleton
@Slf4j
public class TurnOnRun extends TimedSoundBase
{

	@Inject
	private Client client;

	@Inject
	private OdablockConfig config;

	@Inject
	private SoundEngine soundEngine;

	@Inject
	private ScheduledExecutorService executor;

	private boolean isRunning = false;

	private final String message = "FAST! I said FAST!";
	private final String runOption = "Toggle Run";

	TurnOnRun()
	{
		super(10);
	}

	public void onVarbitChanged(VarbitChanged varbitChanged)
	{
		// Running varbit
		if (varbitChanged.getVarbitId() == OdablockVarbits.RUNNING.Id && varbitChanged.getVarpId() == OdablockVarbits.RUNNING.VarpId)
		{
			isRunning = varbitChanged.getValue() == OdablockVarbitValues.RUNNING_TRUE.Value;
		}
	}

	public void onMenuOptionClicked(MenuOptionClicked menuOptionClicked)
	{
		String option = menuOptionClicked.getMenuOption();
		MenuAction action = menuOptionClicked.getMenuAction();

		// Turning on run
		if (config.turnOnRun() && option.equals(runOption))
		{
			int currentTick = client.getTickCount();
			if (!isRunning && canPlaySound(currentTick))
			{
				if (config.showChatMessages())
				{
					client.addChatMessage(ChatMessageType.PUBLICCHAT, ODABLOCK, message, null);
				}
				soundEngine.playClip(Sound.TURNING_ON_RUN, executor);
				setLastPlayedTickTick(currentTick);
			}
		}
	}
}
