package com.github.m0bilebtw;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Range;

@ConfigGroup(CEngineerCompletedConfig.GROUP)
public interface CEngineerCompletedConfig extends Config {
    String GROUP = "cengineercompleted";

    @ConfigItem(
            keyName = "announceLevelUp",
            name = "Level ups",
            description = "Should C Engineer announce when you gain a level in a skill?",
            position = 0
    )
    default boolean announceLevelUp() {
        return true;
    }

    @ConfigItem(
            keyName = "announceLevelUpIncludesVirtual",
            name = "Include virtual level ups",
            description = "Should C Engineer announce when you gain a virtual (>99) level in a skill?",
            position = 1
    )
    default boolean announceLevelUpIncludesVirtual() {
        return false;
    }

    @ConfigItem(
            keyName = "announceQuestCompletion",
            name = "Quest completions",
            description = "Should C Engineer announce when you complete a quest?",
            position = 2
    )
    default boolean announceQuestCompletion() {
        return true;
    }

    @ConfigItem(
            keyName = "announceCollectionLog",
            name = "New collection log entry",
            description = "Should C Engineer announce when you fill in a new slot in your collection log? This one relies on you having chat messages (included with the popup option) enabled in game settings!",
            position = 3
    )
    default boolean announceCollectionLog() {
        return true;
    }

    @ConfigItem(
            keyName = "announceAchievementDiary",
            name = "Completed achievement diaries",
            description = "Should C Engineer announce when you complete a new achievement diary?",
            position = 4
    )
    default boolean announceAchievementDiary() {
        return true;
    }

    @ConfigItem(
            keyName = "announceCombatAchievement",
            name = "Completed combat achievement tasks",
            description = "Should C Engineer announce when you complete a new combat achievement task?",
            position = 5
    )
    default boolean announceCombatAchievement() {
        return true;
    }

    @ConfigItem(
            keyName = "announceDeath",
            name = "When you die",
            description = "Should C Engineer relive his PvP HCIM death when you die?",
            position = 6
    )
    default boolean announceDeath() {
        return true;
    }

    @ConfigItem(
            keyName = "showChatMessages",
            name = "Show fake public chat message (only you will see it)",
            description = "Should C Engineer announce your achievements in game chat as well as audibly?",
            position = 7
    )
    default boolean showChatMessages() {
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
            position = 8
    )
    default int announcementVolume() {
        return 100;
    }

    @ConfigItem(
            keyName = "easterEggs",
            name = "Easter eggs",
            description = "Should C Engineer comment on your gameplay?",
            position = 20
    )
    default boolean easterEggs() {
        return true;
    }

    @ConfigItem(
            keyName = "downloadStreamerSounds",
            name = "Include streamer troll sounds (requires plugin restart)",
            description = "Restart plugin to take effect! If disabled, will remove and no longer download sounds that are streamer trolls",
            position = 21
    )
    default boolean downloadStreamerTrolls() { return true; }
}
