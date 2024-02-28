package com.github.dappermickie.odablock.sounds;

import com.github.dappermickie.odablock.OdablockConfig;
import com.github.dappermickie.odablock.Sound;
import com.github.dappermickie.odablock.SoundEngine;
import java.util.concurrent.ScheduledExecutorService;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.events.WidgetLoaded;
import net.runelite.api.widgets.Widget;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.util.Text;

@Singleton
@Slf4j
public class PkChest
{

	@Inject
	private Client client;

	@Inject
	private OdablockConfig config;

	@Inject
	private SoundEngine soundEngine;

	@Inject
	private ScheduledExecutorService executor;

	@Getter(AccessLevel.PACKAGE)
	@Inject
	private ClientThread clientThread;

	private static final int GROUP_ID = 742;
	private static final int CHILD_ID = 6;

	public void onWidgetLoaded(WidgetLoaded event)
	{
		if (event.getGroupId() == GROUP_ID)
		{
			clientThread.invokeLater(this::checkWidgetAndPlaySound);
		}
	}

	private void checkWidgetAndPlaySound()
	{
		if (!config.pkChest())
		{
			return;
		}
		Widget widget = client.getWidget(GROUP_ID, CHILD_ID);
		if (widget != null)
		{
			if (Text.standardize(widget.getText()).startsWith("value in chest: "))
			{
				soundEngine.playClip(Sound.CLICKING_PK_LOOT_CHEST, executor);
			}
		}
	}
}
