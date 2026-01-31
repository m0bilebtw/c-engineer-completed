package com.github.m0bilebtw.sound;

import lombok.Getter;

import java.util.Random;

public enum Sound {
    LEVEL_UP("LevelUpCompleted_r2.wav"),
    QUEST("QuestCompleted_r2.wav"),
    COLLECTION_LOG_SLOT("ColLogSlotCompleted_r1.wav"),
    COMBAT_TASK("CombatTaskCompleted_r1.wav"),
    ACHIEVEMENT_DIARY("AchieveDiaryCompleted_r1.wav"),
    SLAYER_TASK("slayer_task_r2.wav"),
    HUNTER_RUMOUR("hunter_rumour_r2.wav"),
    HUNTER_RUMOUR_NOT_COMPLETED("hunter_rumour_not_completed_r1.wav"),
    FARMING_CONTRACT("farming_contract_r2.wav"),
    LEAGUES_TASK("leagues_task_r2.wav"),
    GRID_TASK("grid_task_r1.wav"),
    GRUBBY_KEY("grubby_key_r1.wav"),
    LARRANS_KEY("larrans_key_r1.wav"),
    BRIMSTONE_KEY("brimstone_key_r1.wav"),
    DEATH("DyingHCIMCompleted_r1.wav"),
    DEATH_TO_C_ENGINEER("Sit_r1.wav"),

    QOL_NON_PARCH_INFERNAL("Parched_Infernal_r1.wav"),
    QOL_GEM_CRAB_MOVED("gem_crab_moved_r1.wav"),

    EASTER_EGG_STAIRCASE("Staircase_r1.wav"),
    EASTER_EGG_STRAYDOG_BONE("ILoveYou_r2.wav"),
    EASTER_EGG_TWISTED_BOW_1GP("TwistedBow1GP_r1.wav"),
    EASTER_EGG_ZULRAH_PB("ZulrahPB_r1.wav"),

    ESCAPE_CRYSTAL("Escape_Crystal_r1.wav", true),

    SNOWBALL_CONSOLIDATED_V1_1("SnowballTrollV2_steamPings_r1.wav", true),
    SNOWBALL_CONSOLIDATED_V1_2("SnowballTrollV2_farts_r1.wav", true),
    SNOWBALL_CONSOLIDATED_V1_3("SnowballV3_Notif_r1.wav", true),
    SNOWBALL_CONSOLIDATED_V1_4("SnowballTrollV2_epicNotifSpam_r1.wav", true),
    SNOWBALL_CONSOLIDATED_V1_5("SnowballV3_Sus_r1.wav", true),
    SNOWBALL_CONSOLIDATED_V1_6("SnowballV4_7_r1.wav", true),
    SNOWBALL_CONSOLIDATED_V1_7("SnowballV3_Bits_r1.wav", true),
    SNOWBALL_CONSOLIDATED_V1_8("SnowballV3_Battery_r1.wav", true),
    SNOWBALL_CONSOLIDATED_V1_9("SnowballV4_6_r1.wav", true),
    SNOWBALL_CONSOLIDATED_V1_10("SnowballTroll3_r1.wav", true),
    SNOWBALL_CONSOLIDATED_V1_11("SnowballTroll1_r1.wav", true),
    SNOWBALL_CONSOLIDATED_V1_12("SnowballV3_Knock_r1.wav", true),
    SNOWBALL_CONSOLIDATED_V1_13("SnowballTroll8_r1.wav", true),

    SNOWBALL_GAUNTLET_LOBBY("SnowballV4_NR_Gauntlet_r1.wav", true),

    SNOWBALL_EQUIPPING_BUCKET_HELM_G_OR_FUNNY_FEEL("SnowballV4_NR_BucketHelmG_r1.wav", true),
    SNOWBALL_EQUIPPING_GIANT_BOOT("SnowballV4_NR_GiantBoot_r1.wav", true),
    SNOWBALL_EQUIPPING_SAGACIOUS_SPECTACLES("SnowballV4_NR_SagaciousSpectacles_r1.wav", true),
    SNOWBALL_EQUIPPING_MASK_OF_REBIRTH("SnowballV4_NR_MaskOfRebirth_r1.wav", true),

    EMOTE_TROLL_AKKHA("Akkha_r1.wav", true),

    EMOTE_TROLL_BABA_1("BaBa_1_r1.wav", true),
    EMOTE_TROLL_BABA_2("BaBa_2_r1.wav", true),
    EMOTE_TROLL_BABA_3("BaBa_3_r1.wav", true),

    EMOTE_TROLL_WE("Emote_We_r1.wav", true),
    EMOTE_TROLL_EL("Emote_El_r2.wav", true),
    EMOTE_TROLL_AF("AFriend2_Bye_r1.wav", true),

    ATTACK_TROLL_IB("attack_troll_IB_r1.wav", true),
    ATTACK_TROLL_DC("attack_troll_DC_r2.wav", true),

    CHAT_TROLL_SKILL_SPECS_HELLO("SkillSpecs_Hello_r1.wav", true),
    CHAT_TROLL_SKILL_SPECS_OK("SkillSpecs_OK_r1.wav", true),
    CHAT_TROLL_SKILL_SPECS_OOPS("SkillSpecs_Oops_r1.wav", true),

    TOB_GREEN_BALL("GreenBall_r1.wav", true),

    STAT_SPY_SOUP("StatSpy_Soup_r1.wav", true),
    ;

    @Getter
    private final String resourceName;
    private final boolean isStreamerTroll;

    Sound(String resNam) {
        this(resNam, false);
    }

    Sound(String resNam, boolean streamTroll) {
        resourceName = resNam;
        isStreamerTroll = streamTroll;
    }

    boolean isStreamerTroll() {
        return isStreamerTroll;
    }

    @Override
    public String toString() {
        return resourceName;
    }

    private static final Random random = new Random();

    public static Sound randomSnowballSoundNotFromEquippedItem() {
        return SNOWBALL_SOUNDS_NOT_FROM_EQUIPPED_ITEMS[random.nextInt(SNOWBALL_SOUNDS_NOT_FROM_EQUIPPED_ITEMS.length)];
    }

    private static final Sound[] SNOWBALL_SOUNDS_NOT_FROM_EQUIPPED_ITEMS = new Sound[] {
            SNOWBALL_CONSOLIDATED_V1_1,
            SNOWBALL_CONSOLIDATED_V1_2,
            SNOWBALL_CONSOLIDATED_V1_3,
            SNOWBALL_CONSOLIDATED_V1_4,
            SNOWBALL_CONSOLIDATED_V1_5,
            SNOWBALL_CONSOLIDATED_V1_6,
            SNOWBALL_CONSOLIDATED_V1_7,
            SNOWBALL_CONSOLIDATED_V1_8,
            SNOWBALL_CONSOLIDATED_V1_9,
            SNOWBALL_CONSOLIDATED_V1_10,
            SNOWBALL_CONSOLIDATED_V1_11,
            SNOWBALL_CONSOLIDATED_V1_12,
            SNOWBALL_CONSOLIDATED_V1_13
    };

    public static Sound randomBabaEmoteSound() {
        return BABA_EMOTE_SOUNDS[random.nextInt(BABA_EMOTE_SOUNDS.length)];
    }

    private static final Sound[] BABA_EMOTE_SOUNDS = new Sound[] {
            Sound.EMOTE_TROLL_BABA_1,
            Sound.EMOTE_TROLL_BABA_2,
            Sound.EMOTE_TROLL_BABA_3,
    };
}
