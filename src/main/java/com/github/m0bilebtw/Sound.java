package com.github.m0bilebtw;

public enum Sound {
    LEVEL_UP("LevelUpCompleted_r1.wav"),
    QUEST("QuestCompleted_r1.wav"),
    COLLECTION_LOG_SLOT("ColLogSlotCompleted_r1.wav"),
    COMBAT_TASK("CombatTaskCompleted_r1.wav"),
    ACHIEVEMENT_DIARY("AchieveDiaryCompleted_r1.wav"),
    DEATH("DyingHCIMCompleted_r1.wav"),
    EASTER_EGG_STAIRCASE("Staircase_r1.wav"),
    EASTER_EGG_STRAYDOG_BONE("ILoveYou_r1.wav"),
    EASTER_EGG_TWISTED_BOW_1GP("TwistedBow1GP_r1.wav"),
    EASTER_EGG_ZULRAH_PB("ZulrahPB_r1.wav"),
    EASTER_EGG_HAIRCUT("Haircut_r1.wav"),
    BOND_OFFER_1("BondTroll1_r1.wav", true),
    BOND_OFFER_2("BondTroll2_r1.wav", true),
    BOND_OFFER_3("BondTroll3_r1.wav", true),
    SNOWBALL_1("SnowballTroll1_r1.wav", true),
    SNOWBALL_2("SnowballTroll2_r1.wav", true),
    SNOWBALL_3("SnowballTroll3_r1.wav", true),
    SNOWBALL_4("SnowballTroll4_r1.wav", true),
    SNOWBALL_5("SnowballTroll5_r1.wav", true),
    SNOWBALL_6("SnowballTroll6_r1.wav", true),
    SNOWBALL_7("SnowballTroll7_r1.wav", true),
    SNOWBALL_8("SnowballTroll8_r1.wav", true),
    SNOWBALL_9("SnowballTroll9_r1.wav", true);

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

    public static final Sound[] BOND_OFFER_SOUNDS = new Sound[] {
            Sound.BOND_OFFER_1,
            Sound.BOND_OFFER_2,
            Sound.BOND_OFFER_3
    };

    public static final Sound[] SNOWBALL_SOUNDS = new Sound[] {
            Sound.SNOWBALL_1,
            Sound.SNOWBALL_2,
            Sound.SNOWBALL_3,
            Sound.SNOWBALL_4,
            Sound.SNOWBALL_5,
            Sound.SNOWBALL_6,
            Sound.SNOWBALL_7,
            Sound.SNOWBALL_8,
            Sound.SNOWBALL_9,
    };
}
