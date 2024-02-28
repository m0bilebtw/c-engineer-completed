package com.github.dappermickie.odablock.sounds;

import com.github.dappermickie.odablock.OdablockConfig;
import static com.github.dappermickie.odablock.OdablockPlugin.ODABLOCK;
import com.github.dappermickie.odablock.RandomSoundUtility;
import com.github.dappermickie.odablock.Sound;
import com.github.dappermickie.odablock.SoundEngine;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ScheduledExecutorService;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.Varbits;
import static net.runelite.api.Varbits.DIARY_KARAMJA_EASY;
import static net.runelite.api.Varbits.DIARY_KARAMJA_HARD;
import static net.runelite.api.Varbits.DIARY_KARAMJA_MEDIUM;
import net.runelite.api.annotations.Varbit;
import net.runelite.api.events.VarbitChanged;

@Singleton
@Slf4j
public class AchievementDiaries
{

	@Inject
	private Client client;

	@Inject
	private OdablockConfig config;

	@Inject
	private SoundEngine soundEngine;

	@Inject
	private ScheduledExecutorService executor;
	private static final Random random = new Random();

	private int lastLoginTick = -1;

	private final int[] varbitsAchievementDiaries = {
		Varbits.DIARY_ARDOUGNE_EASY, Varbits.DIARY_ARDOUGNE_MEDIUM, Varbits.DIARY_ARDOUGNE_HARD, Varbits.DIARY_ARDOUGNE_ELITE,
		Varbits.DIARY_DESERT_EASY, Varbits.DIARY_DESERT_MEDIUM, Varbits.DIARY_DESERT_HARD, Varbits.DIARY_DESERT_ELITE,
		Varbits.DIARY_FALADOR_EASY, Varbits.DIARY_FALADOR_MEDIUM, Varbits.DIARY_FALADOR_HARD, Varbits.DIARY_FALADOR_ELITE,
		Varbits.DIARY_KANDARIN_EASY, Varbits.DIARY_KANDARIN_MEDIUM, Varbits.DIARY_KANDARIN_HARD, Varbits.DIARY_KANDARIN_ELITE,
		DIARY_KARAMJA_EASY, DIARY_KARAMJA_MEDIUM, DIARY_KARAMJA_HARD, Varbits.DIARY_KARAMJA_ELITE,
		Varbits.DIARY_KOUREND_EASY, Varbits.DIARY_KOUREND_MEDIUM, Varbits.DIARY_KOUREND_HARD, Varbits.DIARY_KOUREND_ELITE,
		Varbits.DIARY_LUMBRIDGE_EASY, Varbits.DIARY_LUMBRIDGE_MEDIUM, Varbits.DIARY_LUMBRIDGE_HARD, Varbits.DIARY_LUMBRIDGE_ELITE,
		Varbits.DIARY_MORYTANIA_EASY, Varbits.DIARY_MORYTANIA_MEDIUM, Varbits.DIARY_MORYTANIA_HARD, Varbits.DIARY_MORYTANIA_ELITE,
		Varbits.DIARY_VARROCK_EASY, Varbits.DIARY_VARROCK_MEDIUM, Varbits.DIARY_VARROCK_HARD, Varbits.DIARY_VARROCK_ELITE,
		Varbits.DIARY_WESTERN_EASY, Varbits.DIARY_WESTERN_MEDIUM, Varbits.DIARY_WESTERN_HARD, Varbits.DIARY_WESTERN_ELITE,
		Varbits.DIARY_WILDERNESS_EASY, Varbits.DIARY_WILDERNESS_MEDIUM, Varbits.DIARY_WILDERNESS_HARD, Varbits.DIARY_WILDERNESS_ELITE
	};

	private final Map<Integer, Integer> oldAchievementDiaries = new HashMap<>();

	public void onVarbitChanged(VarbitChanged event)
	{
		// As we can't listen to specific varbits, we get a tonne of events BEFORE the game has even set the player's
		// diary varbits correctly, meaning it assumes every diary is on 0, then suddenly every diary that has been
		// completed gets updated to the true value and tricks the plugin into thinking they only just finished it.
		// To avoid this behaviour, we make sure the current tick count is sufficiently high that we've already passed
		// the initial wave of varbit changes from logging in.
		if (lastLoginTick == -1 || client.getTickCount() - lastLoginTick < 8)
		{
			return; // Ignoring varbit change as only just logged in
		}

		// Apparently I can't check if it's a particular varbit using the names from Varbits enum, so this is the way
		for (@Varbit int diary : varbitsAchievementDiaries)
		{
			int newValue = client.getVarbitValue(diary);
			int previousValue = oldAchievementDiaries.getOrDefault(diary, -1);
			oldAchievementDiaries.put(diary, newValue);
			if (config.announceAchievementDiary() && previousValue != -1 && previousValue != newValue && isAchievementDiaryCompleted(diary, newValue))
			{
				// value was not unknown (we know the previous value), value has changed, and value indicates diary is completed now
				if (config.showChatMessages())
				{
					client.addChatMessage(ChatMessageType.PUBLICCHAT, ODABLOCK, "Achievement diary: completed.", null);
				}
				soundEngine.playClip(Sound.ACHIEVEMENT_DIARY_SOUNDS, executor);
			}
		}
	}

	public void setLastLoginTick(int tick)
	{
		lastLoginTick = tick;
	}

	private boolean isAchievementDiaryCompleted(int diary, int value)
	{
		switch (diary)
		{
			case DIARY_KARAMJA_EASY:
			case DIARY_KARAMJA_MEDIUM:
			case DIARY_KARAMJA_HARD:
				return value == 2; // jagex, why?
			default:
				return value == 1;
		}
	}

	public void clearOldAchievementDiaries()
	{
		oldAchievementDiaries.clear();
	}

	public void setOldAchievementDiaries()
	{
		for (@Varbit int diary : varbitsAchievementDiaries)
		{
			int value = client.getVarbitValue(diary);
			oldAchievementDiaries.put(diary, value);
		}
	}
}
