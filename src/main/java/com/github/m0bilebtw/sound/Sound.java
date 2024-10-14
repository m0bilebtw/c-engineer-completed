package com.github.m0bilebtw.sound;

import java.util.Random;

public enum Sound {
    LEVEL_UP("LevelUpCompleted_r1.wav"),
    QUEST("QuestCompleted_r1.wav"),
    COLLECTION_LOG_SLOT("ColLogSlotCompleted_r1.wav"),
    COMBAT_TASK("CombatTaskCompleted_r1.wav"),
    ACHIEVEMENT_DIARY("AchieveDiaryCompleted_r1.wav"),
    SLAYER_TASK("slayer_task_r1.wav"),
    HUNTER_RUMOUR("hunter_rumour_r1.wav"),
    FARMING_CONTRACT("farming_contract_r1.wav"),

    // DROPS
    GRUBBY_KEY("grubby_key_r1.wav"),
    LARRANS_KEY("larrans_key_r1.wav"),
    BRIMSTONE_KEY("brimstone_key_r1.wav"),
    ANCIENT_SHARD("ancient_shard_r1.wav"),
    CLUE_BEGINNER("clue_beginner_r1.wav"),
    CLUE_EASY("clue_easy_r1.wav"),
    CLUE_MEDIUM("clue_medium_r1.wav"),
    CLUE_HARD("clue_hard_r1.wav"),
    CLUE_ELITE("clue_elite_r1.wav"),
    CLUE_MASTER("clue_master_r1.wav"),

    DEATH("DyingHCIMCompleted_r1.wav"),
    DEATH_TO_C_ENGINEER("Sit_r1.wav"),

    QOL_NON_PARCH_INFERNAL("Parched_Infernal_r1.wav"),

    EASTER_EGG_STAIRCASE("Staircase_r1.wav"),
    EASTER_EGG_STRAYDOG_BONE("ILoveYou_r1.wav"),
    EASTER_EGG_TWISTED_BOW_1GP("TwistedBow1GP_r1.wav"),
    EASTER_EGG_ZULRAH_PB("ZulrahPB_r1.wav"),

    ESCAPE_CRYSTAL("Escape_Crystal_r1.wav", true),

    SNOWBALL_V4_1("SnowballV4_1_r1.wav", true),
    SNOWBALL_V4_2("SnowballV4_2_r1.wav", true),
    SNOWBALL_V4_3("SnowballV4_3_r1.wav", true),
    SNOWBALL_V4_4("SnowballV4_4_r1.wav", true),
    SNOWBALL_V4_5("SnowballV4_5_r1.wav", true),
    SNOWBALL_V4_6("SnowballV4_6_r1.wav", true),
    SNOWBALL_V4_7("SnowballV4_7_r1.wav", true),

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
    EMOTE_TROLL_EL("Emote_El_r1.wav", true),
    EMOTE_TROLL_AF("AFriend2_Bye_r1.wav", true),

    ATTACK_TROLL_IB("attack_troll_IB_r1.wav", true),
    ATTACK_TROLL_DC("attack_troll_DC_r1.wav", true),

    CHAT_TROLL_SKILL_SPECS_HELLO("SkillSpecs_Hello_r1.wav", true),
    CHAT_TROLL_SKILL_SPECS_OK("SkillSpecs_OK_r1.wav", true),
    CHAT_TROLL_SKILL_SPECS_OOPS("SkillSpecs_Oops_r1.wav", true),

    TOB_GREEN_BALL("GreenBall_r1.wav", true),

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

    public String getResourceName() {
        return resourceName;
    }

    boolean isStreamerTroll() {
        return isStreamerTroll;
    }

    private static final Random random = new Random();

    public static Sound randomSnowballSoundNotFromEquippedItem() {
        return SNOWBALL_SOUNDS_NOT_FROM_EQUIPPED_ITEMS[random.nextInt(SNOWBALL_SOUNDS_NOT_FROM_EQUIPPED_ITEMS.length)];
    }

    private static final Sound[] SNOWBALL_SOUNDS_NOT_FROM_EQUIPPED_ITEMS = new Sound[] {
            SNOWBALL_V4_1,
            SNOWBALL_V4_2,
            SNOWBALL_V4_3,
            SNOWBALL_V4_4,
            SNOWBALL_V4_5,
            SNOWBALL_V4_6,
            SNOWBALL_V4_7,
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
