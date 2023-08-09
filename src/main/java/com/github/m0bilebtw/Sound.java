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
    SNOWBALL_V2_1("SnowballTrollV2_USA_EAS_alarm_r1.wav", true),
    SNOWBALL_V2_2("SnowballTrollV2_bluetooth_r1.wav", true),
    SNOWBALL_V2_3("SnowballTrollV2_epicNotifSpam_r1.wav", true),
    SNOWBALL_V2_4("SnowballTrollV2_farts_r1.wav", true),
    SNOWBALL_V2_5("SnowballTrollV2_iphoneAlarm_r1.wav", true),
    SNOWBALL_V2_6("SnowballTrollV2_knocking_r1.wav", true),
    SNOWBALL_V2_7("SnowballTrollV2_steamPings_r1.wav", true),
    SNOWBALL_V2_8("SnowballTrollV2_technicalDifficulties_r1.wav", true),
    SNOWBALL_V2_9("SnowballTrollV2_win10Notifs_r1.wav", true),
    SNOWBALL_V2_10("SnowballTrollV2_win10USB_r1.wav", true),
    SNOWBALL_V2_11("SnowballTrollV2_twitchDonation_r1.wav", true),
    STAT_SPY_1("StatSpy_Settled_r1.wav", true),
    STAT_SPY_2("StatSpy_SirPugger_r1.wav", true),
    STAT_SPY_3("StatSpy_Soup_r1.wav", true),
    STAT_SPY_TORVESTA("StatSpy_Torvesta_r1.wav", true),
    PET_SKILL_DROP("PetSkillDrop.wav"),
    PET_BOSS_DROP("PetBossDrop.wav");

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
            Sound.SNOWBALL_V2_1,
            Sound.SNOWBALL_V2_2,
            Sound.SNOWBALL_V2_3,
            Sound.SNOWBALL_V2_4,
            Sound.SNOWBALL_V2_5,
            Sound.SNOWBALL_V2_6,
            Sound.SNOWBALL_V2_7,
            Sound.SNOWBALL_V2_8,
            Sound.SNOWBALL_V2_9,
            Sound.SNOWBALL_V2_10,
            Sound.SNOWBALL_V2_11,
    };

    public static final Sound[] STAT_SPY_SOUNDS = new Sound[] {
            Sound.STAT_SPY_1,
            Sound.STAT_SPY_2,
            Sound.STAT_SPY_3
    };
}
