package com.github.m0bilebtw.sound;

/**
 * How much C Engineer wants to be heard saying a given line, when two of them land at the same moment.
 * Ordered least to most important, so a natural {@link Enum#compareTo} decides who wins.
 */
public enum SoundPriority {
    /** Things that happen often enough that missing one costs nothing: level ups, quality of life nudges, trolls. */
    LOW,
    /** The everyday announcements: tasks, keys, contracts. */
    NORMAL,
    /** Rare or costly moments you would be annoyed to have talked over: collection log slots, quests, deaths. */
    HIGH,
}
