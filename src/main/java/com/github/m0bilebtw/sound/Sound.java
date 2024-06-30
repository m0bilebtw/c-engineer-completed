package com.github.m0bilebtw.sound;

import java.util.Random;

public enum Sound {
    LEVEL_UP("LevelUpCompleted_r1.wav"),
    QUEST("QuestCompleted_r1.wav"),
    COLLECTION_LOG_SLOT("ColLogSlotCompleted_r1.wav"),
    COMBAT_TASK("CombatTaskCompleted_r1.wav"),
    ACHIEVEMENT_DIARY("AchieveDiaryCompleted_r1.wav"),
    DEATH("DyingHCIMCompleted_r1.wav"),
    DEATH_TO_C_ENGINEER("Sit_r1.wav"),

    QOL_NON_PARCH_INFERNAL("Parched_Infernal_r1.wav"),

    EASTER_EGG_STAIRCASE("Staircase_r1.wav"),
    EASTER_EGG_STRAYDOG_BONE("ILoveYou_r1.wav"),
    EASTER_EGG_TWISTED_BOW_1GP("TwistedBow1GP_r1.wav"),
    EASTER_EGG_ZULRAH_PB("ZulrahPB_r1.wav"),

    ESCAPE_CRYSTAL("Escape_Crystal_r1.wav", true),

    SNOWBALL_V4_1("", true), // todo rest of new snowball sounds

    SNOWBALL_GAUNTLET_LOBBY("", true),

    SNOWBALL_EQUIPPING_BUCKET_HELM_G("", true),
    SNOWBALL_EQUIPPING_GIANT_BOOT("", true),
    SNOWBALL_EQUIPPING_SAGACIOUS_SPECTACLES("", true),
    SNOWBALL_EQUIPPING_MASK_OF_REBIRTH("", true),

    EMOTE_TROLL_AKKHA("Akkha_r1.wav", true),

    EMOTE_TROLL_BABA_1("", true),
    EMOTE_TROLL_BABA_2("", true),
    EMOTE_TROLL_BABA_3("", true),

    EMOTE_TROLL_WE("Emote_We_r1.wav", true),
    EMOTE_TROLL_EL("Emote_El_r1.wav", true),
    EMOTE_TROLL_AF("", true),

    CHAT_TROLL_SKILL_SPECS_HELLO("", true),
    CHAT_TROLL_SKILL_SPECS_OK("", true),
    CHAT_TROLL_SKILL_SPECS_OOPS("", true),

    TOB_GREEN_BALL("", true),

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
            Sound.SNOWBALL_V4_1, // todo rest of snowball sounds in array (except certain item equipped)
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
