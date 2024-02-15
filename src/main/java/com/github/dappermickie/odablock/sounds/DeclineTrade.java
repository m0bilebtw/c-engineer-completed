package com.github.dappermickie.odablock.sounds;

// Special thanks to: https://github.com/while-loop/runelite-plugins/tree/runewatch

import com.github.dappermickie.odablock.OdablockConfig;
import com.github.dappermickie.odablock.OdablockPlugin;
import com.github.dappermickie.odablock.Sound;
import com.github.dappermickie.odablock.SoundEngine;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.MenuAction;
import net.runelite.api.events.MenuOptionClicked;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;

@Singleton
@Slf4j
public class DeclineTrade
{

	@Inject
	private Client client;

	@Inject
	private OdablockConfig config;

	@Inject
	private SoundEngine soundEngine;

	@Inject
	private ScheduledExecutorService executor;

	private static final int PLAYER_TRADE_OFFER_GROUP_ID = 335;
	private static final int PLAYER_TRADE_CONFIRMATION_GROUP_ID = 334;
	private static final String DECLINE_MSG = "Decline";
	private static final List<Integer> TRADE_SCREEN_GROUP_IDS = Arrays.asList(
		PLAYER_TRADE_OFFER_GROUP_ID,
		PLAYER_TRADE_CONFIRMATION_GROUP_ID
	);

	public void onMenuOptionClicked(MenuOptionClicked menuOptionClicked)
	{
		int groupId = OdablockPlugin.TO_GROUP(menuOptionClicked.getParam1());
		String option = menuOptionClicked.getMenuOption();
		MenuAction action = menuOptionClicked.getMenuAction();

		if (config.declineTrade() && TRADE_SCREEN_GROUP_IDS.contains(groupId))
		{

			// Decline trade
			if (option.equals(DECLINE_MSG))
			{
				soundEngine.playClip(Sound.DECLINE_TRADE, executor);
			}
		}
	}
}
