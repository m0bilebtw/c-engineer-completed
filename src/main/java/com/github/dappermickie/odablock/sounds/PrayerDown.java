package com.github.dappermickie.odablock.sounds;

import com.github.dappermickie.odablock.OdablockConfig;
import com.github.dappermickie.odablock.Sound;
import com.github.dappermickie.odablock.SoundEngine;
import com.github.dappermickie.odablock.SoundIds;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.Player;
import net.runelite.api.Skill;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.SoundEffectPlayed;
import net.runelite.client.eventbus.Subscribe;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.concurrent.ScheduledExecutorService;

@Singleton
@Slf4j
public class PrayerDown
{

	@Inject
	private Client client;

	@Inject
	private OdablockConfig config;

	@Inject
	private SoundEngine soundEngine;

	@Inject
	private ScheduledExecutorService executor;

	@Inject
	RedemptionProc redemptionProc;

	private int previousPrayerValue = -1;
	private boolean notifyPrayer = false;
	private int lastLoginTick = -1;

	public void setLastLoginTick(int tick)
	{
    	lastLoginTick = tick;
	}

	public void onGameTick(GameTick event)
	{
		if (config.prayerMessage() && checkLowPrayer() && client.getTickCount() - lastLoginTick > 2 && lastLoginTick != -1)
		{
			soundEngine.playClip(Sound.SMITED_NO_PRAYER, executor);
		}
	}

	private boolean checkLowPrayer()
	{
		int currentPrayerValue = client.getBoostedSkillLevel(Skill.PRAYER);
		if (previousPrayerValue != currentPrayerValue)
		{
			previousPrayerValue = currentPrayerValue;
		}
		else
		{
			return false;
		}
		if (currentPrayerValue <= 0)
		{
			if (!notifyPrayer &&
				!redemptionProc.HasRedemptionProcced())
			{
				notifyPrayer = true;
				return true;
			}
		}
		else
		{
			notifyPrayer = false;
		}
		return false;
	}

	public void onSoundEffectPlayed(SoundEffectPlayed event)
	{
		int soundId = event.getSoundId();

		if (config.prayerMessage())
		{
			//if(!config.ownPlayerOnly() || (config.ownPlayerOnly() && local == event.getSource())) {
			if (soundId == SoundIds.PRAYER_DOWN.Id)
			{
				event.consume();
				return;
			}
			//}
		}
	}
}
