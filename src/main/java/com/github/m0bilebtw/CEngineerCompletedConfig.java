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
            name = "Level Up SFX",
            description = "Should a jingle play when you level up a skill? (headphone users beware)",
            position = 3
    )
    default boolean announceLevelUp() {
        return true;
    }

    @ConfigItem(
            keyName = "announceLevelUpIncludesVirtual",
            name = "Include virtual level ups?",
            description = "Should C Engineer announce when you gain a virtual (>99) level in a skill?",
            position = 1
    )
    default boolean announceLevelUpIncludesVirtual() {
        return false;
    }

    @ConfigItem(
            keyName = "announceQuestCompletion",
            name = "Quest Completion SFX",
            description = "Should a jingle play upon completing a quest?",
            position = 6
    )
    default boolean announceQuestCompletion() {
        return true;
    }

    @ConfigItem(
            keyName = "announceCollectionLog",
            name = "Collection Log Entry SFX",
            description = "Should a jingle play when you receive a new collection log item? This one relies on you having chat messages (included with the popup option) enabled in game settings!",
            position = 5
    )
    default boolean announceCollectionLog() {
        return true;
    }

    @ConfigItem(
            keyName = "announceAchievementDiary",
            name = "Achievement Diary Completion SFX",
            description = "Should a jingle play when you complete a new achievement diary?",
            position = 7
    )
    default boolean announceAchievementDiary() {
        return true;
    }

    @ConfigItem(
            keyName = "announceCombatAchievement",
            name = "Combat Achievement SFX",
            description = "Should a jingle play when you complete a combat task?",
            position = 4
    )
    default boolean announceCombatAchievement() {
        return true;
    }

    @ConfigItem(
            keyName = "announceDeath",
            name = "Death SFX",
            description = "Should a jingle play when you die?",
            position = 2
    )
    default boolean announceDeath() {
        return true;
    }

    @ConfigItem(
            keyName = "showChatMessages",
            name = "Show fake chat message",
            description = "(only you will see it) Should C Engineer announce your achievements in game chat as well as audibly?",
            position = 8
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
            name = "SFX Volume",
            description = "Adjust SFX Volume",
            position = 0
    )
    default int announcementVolume() {
        return 100;
    }

    @ConfigItem(
            keyName = "easterEggs",
            name = "Easter Eggs",
            description = "Should C Engineer comment on your gameplay?",
            position = 20
    )
    default boolean easterEggs() {
        return true;
    }

    @ConfigItem(
            keyName = "downloadStreamerSounds",
            name = "Include Stream Troll Sounds",
            description = "Restart plugin to take effect! If disabled, will remove and no longer download sounds that are streamer trolls",
            position = 21
    )
    default boolean downloadStreamerTrolls() { return true; }



}
