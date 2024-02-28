package com.github.dappermickie.odablock;

public enum Sound
{
	REDEMPTION_PROC("RedemptionProc.wav"),//#1
	DEATH("DyingHCIMCompleted_r1.wav"),//#2
	COLLECTION_LOG_SLOT("ColLogSlotCompleted_r1.wav"),//#3
	SMITED_NO_PRAYER("SmitedNoPrayer.wav"),//#4
	NEW_PET("NewPet.wav"),//#5
	PETTING_DOG("PettingDog.wav"),//#6
	DECLINE_TRADE("DeclineTrade.wav"),//#7
	VENGEANCE("Vengeance.wav"),//#8
	KILLING_SOMEONE_1("KillingSomeone_r1.wav"),//#9
	KILLING_SOMEONE_2("KillingSomeone_r2.wav"),//#9
	KILLING_SOMEONE_3("KillingSomeone_r3.wav"),//#9
	KILLING_SOMEONE_4("KillingSomeone_r4.wav"),//#9
	REPORT_PLAYER_1("ReportPlayer_r1.wav"),//#10
	REPORT_PLAYER_2("ReportPlayer_r2.wav"),//#10
	REPORT_PLAYER_3("ReportPlayer_r3.wav"),//#10

	ZEBAK_ROAR("ZebakRoar.wav"),//#12
	RUBY_PROC("RubyProc.wav"),//#13
	HIT_BY_KEPHRI("HitByKephri.wav"),//#14

	COMBAT_TASK("CombatTaskCompleted_r1.wav"),//#15
	COMBAT_TASK_2("CombatTaskCompleted_r2.wav"),//#15
	COMBAT_TASK_3("CombatTaskCompleted_r3.wav"),//#15
	COMBAT_TASK_4("CombatTaskCompleted_r4.wav"),//#15

	DDS_SPEC("DdsSpec_r1.wav"),//#16
	DDS_SPEC_2("DdsSpec_r2.wav"),//#16
	DDS_SPEC_3("DdsSpec_r3.wav"),//#16

	ACCEPTED_TRADE("AcceptTrade.wav"),//#17

	GETTING_PURPLE_1("GettingPurple_r1.wav"),//#18
	GETTING_PURPLE_2("GettingPurple_r2.wav"),//#18


	KILLING_RAT_OR_SCURRIUS_1("KillingRatOrScurrius_r1.wav"),//#18
	KILLING_RAT_OR_SCURRIUS_2("KillingRatOrScurrius_r2.wav"),//#18

	DISMISSING_RANDOM_EVENT("DismissingRandomEvent.wav"),//#20
	TYPING_IN_BANKPIN("TypingInBankpin.wav"),//#21
	CLIENT_DISCONNECTS("ClientDisconnects.wav"),//#22
	TOA_CHEST_OPENS("ToaChestOpens.wav"),//#23
	WHITE_LIGHT_AFTER_RAID("WhiteLightAfterRaid.wav"),//#24
	TURNING_ON_RUN("TurningOnRun.wav"),//#25
	CLICKING_PK_LOOT_CHEST("ClickingPkLootChest.wav"),//#26

	ACB_SPEC("AcbSpec_r1.wav"),//#27
	ACB_SPEC_2("AcbSpec_r2.wav"),//#27
	ACB_SPEC_3("AcbSpec_r3.wav"),

	AGS_SPEC("AgsSpec_r1.wav"),//#28
	AGS_SPEC_2("AgsSpec_r2.wav"),//#28
	AGS_SPEC_3("AgsSpec_r3.wav"),//#28

	DH_AXE_CHOP("DhAxe_chop_r1.wav"),//#29
	DH_AXE_CHOP_2("DhAxe_chop_r2.wav"),

	DH_AXE_HACK("DhAxe_hack_r1.wav"),//#29
	DH_AXE_HACK_2("DhAxe_hack_r2.wav"),//#29

	DH_AXE_BLOCK("DhAxe_block_r1.wav"),//#29
	DH_AXE_BLOCK_2("DhAxe_block_r2.wav"),//#29
	DH_AXE_BLOCK_3("DhAxe_block_r3.wav"),

	DH_AXE_SMASH("DhAxe_smash_r1.wav"),//#29
	DH_AXE_SMASH_2("DhAxe_smash_r2.wav"),//#29
	DH_AXE_SMASH_3("DhAxe_smash_r3.wav"),//#29

	LEVEL_UP("LevelUpCompleted_r1.wav"),
	QUEST("QuestCompleted_r1.wav"),
	ACHIEVEMENT_DIARY("AchievementDiary_r1.wav"),
	ACHIEVEMENT_DIARY_2("AchievementDiary_r2.wav"),
	EASTER_EGG_STRAYDOG_BONE("GiveBone.wav"),

	HAIRDRESSER_SOUND_1("Hairdresser_r1.wav"),
	HAIRDRESSER_SOUND_2("Hairdresser_r2.wav"),
	HAIRDRESSER_SOUND_3("Hairdresser_r3.wav"),

	SNOWBALL_1("Snowball_r1.wav"),
	SNOWBALL_2("Snowball_r2.wav");

	private final String resourceName;

	Sound(String resNam)
	{
		resourceName = resNam;
	}


	String getResourceName()
	{
		return resourceName;
	}

	public static final Sound[] SNOWBALL_SOUNDS = new Sound[]{
		SNOWBALL_1,
		SNOWBALL_2
	};

	public static final Sound[] PLAYER_KILLING_SOUNDS = new Sound[]{
		KILLING_SOMEONE_1,
		KILLING_SOMEONE_2,
		KILLING_SOMEONE_3,
		KILLING_SOMEONE_4
	};

	public static final Sound[] DH_AXE_CHOP_SOUNDS = new Sound[]{
		DH_AXE_CHOP,
		DH_AXE_CHOP_2
	};

	public static final Sound[] DH_AXE_HACK_SOUNDS = new Sound[]{
		DH_AXE_HACK,
		DH_AXE_HACK_2
	};

	public static final Sound[] DH_AXE_SMASH_SOUNDS = new Sound[]{
		DH_AXE_SMASH,
		DH_AXE_SMASH_2,
		DH_AXE_SMASH_3
	};

	public static final Sound[] DH_AXE_BLOCK_SOUNDS = new Sound[]{
		DH_AXE_BLOCK,
		DH_AXE_BLOCK_2,
		DH_AXE_BLOCK_3
	};

	public static final Sound[] ACHIEVEMENT_DIARY_SOUNDS = new Sound[]{
		ACHIEVEMENT_DIARY,
		ACHIEVEMENT_DIARY_2
	};

	public static final Sound[] ACB_SPEC_SOUNDS = new Sound[]{
		ACB_SPEC,
		ACB_SPEC_2,
		ACB_SPEC_3
	};

	public static final Sound[] HAIRDRESSER_SOUNDS = new Sound[]{
		HAIRDRESSER_SOUND_1,
		HAIRDRESSER_SOUND_2,
		HAIRDRESSER_SOUND_3
	};

	public static final Sound[] KILLING_RAT_OR_SCURRIUS_SOUNDS = new Sound[]{
		KILLING_RAT_OR_SCURRIUS_1,
		KILLING_RAT_OR_SCURRIUS_2
	};

	public static final Sound[] REPORT_PLAYER_SOUNDS = new Sound[]{
		REPORT_PLAYER_1,
		REPORT_PLAYER_2,
		REPORT_PLAYER_3
	};

	public static final Sound[] DDS_SPEC_SOUNDS = new Sound[]{
		DDS_SPEC,
		DDS_SPEC_2,
		DDS_SPEC_3
	};

	public static final Sound[] GETTING_PURPLE_SOUNDS = new Sound[]{
		GETTING_PURPLE_1,
		GETTING_PURPLE_2
	};

	public static final Sound[] AGS_SPEC_SOUNDS = new Sound[]{
		AGS_SPEC,
		AGS_SPEC_2,
		AGS_SPEC_3
	};

	public static final Sound[] COMBAT_TASK_SOUNDS = new Sound[]{
		COMBAT_TASK,
		COMBAT_TASK_2,
		COMBAT_TASK_3,
		COMBAT_TASK_4
	};
}
