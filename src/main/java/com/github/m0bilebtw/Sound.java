package com.github.m0bilebtw;

import java.util.Random;

public enum Sound { // todo fill in proper sound files
    LEVEL_UP("LevelUpCompleted_r1.wav"),
    QUEST("QuestCompleted_r1.wav"),
    COLLECTION_LOG_SLOT("ColLogSlotCompleted_r1.wav"),
    COMBAT_TASK("CombatTaskCompleted_r1.wav"),
    ACHIEVEMENT_DIARY("AchieveDiaryCompleted_r1.wav"),
    DEATH("DyingHCIMCompleted_r1.wav"),
    DEATH_TO_C_ENGINEER(""),

    QOL_NON_PARCH_INFERNAL(""),

    EASTER_EGG_STAIRCASE("Staircase_r1.wav"),
    EASTER_EGG_STRAYDOG_BONE("ILoveYou_r1.wav"),
    EASTER_EGG_TWISTED_BOW_1GP("TwistedBow1GP_r1.wav"),
    EASTER_EGG_ZULRAH_PB("ZulrahPB_r1.wav"),
    EASTER_EGG_HAIRCUT("Haircut_r1.wav"),

    ESCAPE_CRYSTAL("", true),

    SNOWBALL_V3_1("", true),
    SNOWBALL_V3_2("", true),
    SNOWBALL_V3_3("", true),
    SNOWBALL_V3_4("", true),
    SNOWBALL_V3_5("", true),
    SNOWBALL_V3_6("", true),
    SNOWBALL_V3_7("", true),
    SNOWBALL_V3_8("", true),

    EMOTE_TROLL_AKKHA("", true),

    EMOTE_TROLL_WE("", true),
    EMOTE_TROLL_VE("", true),
    EMOTE_TROLL_IR("", true),
    EMOTE_TROLL_EL("", true),
    EMOTE_TROLL_AN("", true),

    STAT_SPY_SOUP("StatSpy_Soup_r1.wav", true),
    ;

    private final String resourceName;
    private final boolean isStreamerTroll;

    Sound(String resNam) {
        this(resNam, false);
    }

    Sound(String resNam, boolean streamTroll) {
        resourceName = resNam;
        isStreamerTroll = streamTroll;
    }

    String getResourceName() {
        return resourceName;
    }

    boolean isStreamerTroll() {
        return isStreamerTroll;
    }

    private static final Random random = new Random();

    public static Sound randomSnowballSound() {
        return SNOWBALL_SOUNDS[random.nextInt(SNOWBALL_SOUNDS.length)];
    }

    private static final Sound[] SNOWBALL_SOUNDS = new Sound[] {
            Sound.SNOWBALL_V3_1,
            Sound.SNOWBALL_V3_2,
            Sound.SNOWBALL_V3_3,
            Sound.SNOWBALL_V3_4,
            Sound.SNOWBALL_V3_5,
            Sound.SNOWBALL_V3_6,
            Sound.SNOWBALL_V3_7,
            Sound.SNOWBALL_V3_8,
    };
}
