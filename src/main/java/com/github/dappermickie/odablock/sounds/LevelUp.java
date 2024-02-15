package com.github.dappermickie.odablock.sounds;

import com.github.dappermickie.odablock.OdablockConfig;
import com.github.dappermickie.odablock.Sound;
import com.github.dappermickie.odablock.SoundEngine;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.Experience;
import net.runelite.api.Skill;
import net.runelite.api.events.StatChanged;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;

import static com.github.dappermickie.odablock.OdablockPlugin.ODABLOCK;

@Singleton
@Slf4j
public class LevelUp
{
	@Inject
	private Client client;

	@Inject
	private OdablockConfig config;

	@Inject
	private SoundEngine soundEngine;

	@Inject
	private ScheduledExecutorService executor;

	private static final String message = "Level up: completed.";

	private final Map<Skill, Integer> oldExperience = new EnumMap<>(Skill.class);

	public void onStatChanged(StatChanged statChanged)
	{
		final Skill skill = statChanged.getSkill();

		// Modified from Nightfirecat's virtual level ups plugin as this info isn't (yet?) built in to statChanged event
		final int xpAfter = client.getSkillExperience(skill);
		final int levelAfter = Experience.getLevelForXp(xpAfter);
		final int xpBefore = oldExperience.getOrDefault(skill, -1);
		final int levelBefore = xpBefore == -1 ? -1 : Experience.getLevelForXp(xpBefore);

		oldExperience.put(skill, xpAfter);

		// Do not proceed if any of the following are true:
		//  * xpBefore == -1              (don't fire when first setting new known value)
		//  * xpAfter <= xpBefore         (do not allow 200m -> 200m exp drops)
		//  * levelBefore >= levelAfter   (stop if if we're not actually reaching a new level)
		//  * levelAfter > MAX_REAL_LEVEL && config says don't include virtual (level is virtual and config ignores virtual)
		if (xpBefore == -1 || xpAfter <= xpBefore || levelBefore >= levelAfter ||
			(levelAfter > Experience.MAX_REAL_LEVEL && !config.announceLevelUpIncludesVirtual()))
		{
			return;
		}

		// If we get here, 'skill' was leveled up!
		if (config.announceLevelUp())
		{
			if (config.showChatMessages())
			{
				client.addChatMessage(ChatMessageType.PUBLICCHAT, ODABLOCK, message, null);
			}
			soundEngine.playClip(Sound.LEVEL_UP, executor);
		}
	}

	public void clear()
	{
		oldExperience.clear();
	}

	public void setOldExperience()
	{
		for (final Skill skill : Skill.values())
		{
			oldExperience.put(skill, client.getSkillExperience(skill));
		}
	}
}
