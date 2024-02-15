package com.github.dappermickie.odablock.sounds;

import com.github.dappermickie.odablock.OdablockConfig;
import com.github.dappermickie.odablock.Sound;
import com.github.dappermickie.odablock.SoundEngine;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.events.MenuOptionClicked;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.concurrent.ScheduledExecutorService;

@Singleton
@Slf4j
public class DismissRandomEvent
{

	@Inject
	private Client client;

	@Inject
	private OdablockConfig config;

	@Inject
	private SoundEngine soundEngine;

	@Inject
	private ScheduledExecutorService executor;

	private final String optionText = "Dismiss";

	public void onMenuOptionClicked(MenuOptionClicked menuOptionClicked)
	{
		String option = menuOptionClicked.getMenuOption();
		// Dismiss random event
		if (config.dismissRandomEvent() && option.equals(optionText))
		{
			soundEngine.playClip(Sound.DISMISSING_RANDOM_EVENT, executor);
		}
	}
}
