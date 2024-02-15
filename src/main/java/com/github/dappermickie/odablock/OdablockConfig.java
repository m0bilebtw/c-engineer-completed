package com.github.dappermickie.odablock;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;
import net.runelite.client.config.Range;

@ConfigGroup(OdablockConfig.CONFIG_GROUP)
public interface OdablockConfig extends Config
{
	String CONFIG_GROUP = "odablockplugin";

	@ConfigSection(
		name = "Tombs of Amascut",
		description = "All the configurations regarding Tombs of Amascut.",
		position = 100
	)
	String TOA_SECTION = "toaSection";

	@ConfigItem(
		keyName = "enableToaChest",
		name = "Toa chest appears",
		description = "When enabled, Odablock will say 'Please GAGECK' if your party gets a purple or '' if it's a white light.",
		section = TOA_SECTION,
		position = 101
	)
	default boolean enableToaChest()
	{
		return true;
	}

	@ConfigItem(
		keyName = "enableToaPurpleChestOpens",
		name = "Opening The Chest",
		description = "When enabled, Odablock will say 'Please GAGECK' whenever someone in your party opens the purple chest at TOA.",
		section = TOA_SECTION,
		position = 102
	)
	default boolean enableToaPurpleChestOpens()
	{
		return true;
	}

	@ConfigItem(
		keyName = "rubyBoltProc",
		name = "Ruby Bolt SLAAA",
		description = "Should the ruby bolt proc be replaced with oda's SLAAA?",
		position = 0
	)
	default boolean rubyBoltProc()
	{
		return true;
	}

	@ConfigItem(
		keyName = "playerKilling",
		name = "Player Killing",
		description = "Should Odablock tell you something when you kill a player? (only works if you're still close to the player when he dies)",
		position = 0
	)
	default boolean playerKilling()
	{
		return true;
	}

	@ConfigItem(
		keyName = "onlyForOwnPlayer",
		name = "Only Own Player",
		description = "Should Odablock sounds play for your player only? (ags, dds spec ...)",
		position = 0
	)
	default boolean ownPlayerOnly()
	{
		return true;
	}

	@ConfigItem(
		keyName = "ddsSpec",
		name = "DDS Spec",
		description = "Should Odablock sounds play for your dds spec?",
		position = 0
	)
	default boolean ddsSpec()
	{
		return true;
	}

	@ConfigItem(
		keyName = "agsSpec",
		name = "AGS Spec",
		description = "Should Odablock sounds play for ags spec?",
		position = 0
	)
	default boolean agsSpec()
	{
		return true;
	}

	@ConfigItem(
		keyName = "acbSpec",
		name = "ACB Spec",
		description = "Should Odablock sounds play for acb spec? (only works with soudns on)",
		position = 0
	)
	default boolean acbSpec()
	{
		return true;
	}

	@ConfigItem(
		keyName = "bankPin",
		name = "Bank Pin",
		description = "Should Odablock make the 'ai ai ai ai' sound when you type in your bank pin?",
		position = 0
	)
	default boolean bankPin()
	{
		return true;
	}

	@ConfigItem(
		keyName = "turnOnRun",
		name = "Turn on run",
		description = "Should Odablock say 'FAST! I said FAST!' sound when you turn your run on?",
		position = 0
	)
	default boolean turnOnRun()
	{
		return true;
	}

	@ConfigItem(
		keyName = "petDog",
		name = "Pet the Dog",
		description = "Should Stella say 'Who's a good little zoggy!' when you pet the dog?",
		position = 0
	)
	default boolean petDog()
	{
		return true;
	}

	@ConfigItem(
		keyName = "dismissRandomEvent",
		name = "Dismiss random event",
		description = "Should Odablock say 'No sanks!' when you dismiss a random event?",
		position = 0
	)
	default boolean dismissRandomEvent()
	{
		return true;
	}

	@ConfigItem(
		keyName = "declineTrade",
		name = "Decline Trade",
		description = "Should Odablock say 'No Sanks!' when you decline a trade?",
		position = 0
	)
	default boolean declineTrade()
	{
		return true;
	}


	@ConfigItem(
		keyName = "acceptTrade",
		name = "Accept Trade",
		description = "Should Odablock say 'Oda the generous strikes again!' when you accept a trade?",
		position = 0
	)
	default boolean acceptTrade()
	{
		return true;
	}

	@ConfigItem(
		keyName = "sendReport",
		name = "Send Report",
		description = "Should Odablock say 'Reported for salutations!' when you report someone?",
		position = 0
	)
	default boolean sendReport()
	{
		return true;
	}

	@ConfigItem(
		keyName = "announceLevelUp",
		name = "Level ups",
		description = "Should C Engineer announce when you gain a level in a skill?",
		position = 1
	)
	default boolean announceLevelUp()
	{
		return true;
	}

	@ConfigItem(
		keyName = "announceLevelUpIncludesVirtual",
		name = "Include virtual level ups",
		description = "Should C Engineer announce when you gain a virtual (>99) level in a skill?",
		position = 2
	)
	default boolean announceLevelUpIncludesVirtual()
	{
		return false;
	}

	@ConfigItem(
		keyName = "announceQuestCompletion",
		name = "Quest completions",
		description = "Should C Engineer announce when you complete a quest?",
		position = 3
	)
	default boolean announceQuestCompletion()
	{
		return true;
	}

	@ConfigItem(
		keyName = "prayerMessage",
		name = "Prayer Message",
		description = "Should Odablock let you know when you run out of prayer?",
		position = 4
	)
	default boolean prayerMessage()
	{
		return true;
	}

	@ConfigItem(
		keyName = "redemptionMessage",
		name = "Redemption Message",
		description = "Should Odablock let you know when you proc a redemption?",
		position = 5
	)
	default boolean redemptionMessage()
	{
		return true;
	}

	@ConfigItem(
		keyName = "announceCollectionLog",
		name = "New collection log entry",
		description = "Should C Engineer announce when you fill in a new slot in your collection log? This one relies on you having chat messages (included with the popup option) enabled in game settings!",
		position = 6
	)
	default boolean announceCollectionLog()
	{
		return true;
	}

	@ConfigItem(
		keyName = "announceAchievementDiary",
		name = "Completed achievement diaries",
		description = "Should C Engineer announce when you complete a new achievement diary?",
		position = 7
	)
	default boolean announceAchievementDiary()
	{
		return true;
	}

	@ConfigItem(
		keyName = "announceCombatAchievement",
		name = "Completed combat achievement tasks",
		description = "Should C Engineer announce when you complete a new combat achievement task?",
		position = 8
	)
	default boolean announceCombatAchievement()
	{
		return true;
	}

	@ConfigItem(
		keyName = "announceDeath",
		name = "When you die",
		description = "Should C Engineer relive his PvP HCIM death when you die?",
		position = 9
	)
	default boolean announceDeath()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showChatMessages",
		name = "Show fake public chat message (only you will see it)",
		description = "Should C Engineer announce your achievements in game chat as well as audibly?",
		position = 10
	)
	default boolean showChatMessages()
	{
		return true;
	}

	@Range(
		min = 0,
		max = 200
	)
	@ConfigItem(
		keyName = "announcementVolume",
		name = "Announcement volume",
		description = "Adjust how loud the audio announcements are played!",
		position = 11
	)
	default int announcementVolume()
	{
		return 100;
	}

	@ConfigItem(
		keyName = "easterEggs",
		name = "Easter eggs",
		description = "Should C Engineer comment on your gameplay?",
		position = 20
	)
	default boolean easterEggs()
	{
		return true;
	}

	@ConfigItem(
		keyName = "downloadStreamerSounds",
		name = "Include streamer troll sounds (requires plugin restart)",
		description = "Restart plugin to take effect! If disabled, will remove and no longer download sounds that are streamer trolls",
		position = 21
	)
	default boolean downloadStreamerTrolls()
	{
		return true;
	}
}
