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
	REPORT_PLAYER("ReportPlayer.wav"),//#10
	SPEC_HIT_ZERO("SpecHitZero.wav"),//#11
	ZEBAK_ROAR("ZebakRoar.wav"),//#12
	RUBY_PROC("RubyProc.wav"),//#13
	HIT_BY_KEPHRI("HitByKephri.wav"),//#14
	COMBAT_TASK("CombatTaskCompleted_r1.wav"),//#15
	DDS_SPEC("DdsSpec.wav"),//#16
	ACCEPTED_TRADE("AcceptTrade.wav"),//#17
	GETTING_PURPLE("GettingPurple.wav"),//#18
	KILLING_RAT_OR_SCURRIUS("KillingRatOrScurrius.wav"),//#18
	DISMISSING_RANDOM_EVENT("DismissingRandomEvent.wav"),//#20
	TYPING_IN_BANKPIN("TypingInBankpin.wav"),//#21
	CLIENT_DISCONNECTS("ClientDisconnects.wav"),//#22
	TOA_CHEST_OPENS("ToaChestOpens.wav"),//#23
	WHITE_LIGHT_AFTER_RAID("WhiteLightAfterRaid.wav"),//#24
	TURNING_ON_RUN("TurningOnRun.wav"),//#25
	CLICKING_PK_LOOT_CHEST("ClickingPkLootChest.wav"),//#26
	ACB_SPEC("AcbSpec.wav"),//#27
	AGS_SPEC("AgsSpec.wav"),//#28
	DH_AXE_CHOP("DhAxe_1.wav"),//#29
	DH_AXE_HACK("DhAxe_2.wav"),//#29
	DH_AXE_BLOCK("DhAxe_3.wav"),//#29
	DH_AXE_SMASH("DhAxe_4.wav"),//#29

	LEVEL_UP("LevelUpCompleted_r1.wav"),
	QUEST("QuestCompleted_r1.wav"),
	ACHIEVEMENT_DIARY("AchieveDiaryCompleted_r1.wav"),
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
	STAT_SPY_TORVESTA("StatSpy_Torvesta_r1.wav", true);

	private final String resourceName;
	private final boolean isStreamerTroll;

	Sound(String resNam)
	{
		this(resNam, false);
	}

	Sound(String resNam, boolean streamTroll)
	{
		resourceName = resNam;
		isStreamerTroll = streamTroll;
	}


	String getResourceName()
	{
		return resourceName;
	}

	boolean isStreamerTroll()
	{
		return isStreamerTroll;
	}

	public static final Sound[] BOND_OFFER_SOUNDS = new Sound[]{
		Sound.BOND_OFFER_1,
		Sound.BOND_OFFER_2,
		Sound.BOND_OFFER_3
	};

	public static final Sound[] SNOWBALL_SOUNDS = new Sound[]{
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

	public static final Sound[] STAT_SPY_SOUNDS = new Sound[]{
		Sound.STAT_SPY_1,
		Sound.STAT_SPY_2,
		Sound.STAT_SPY_3
	};

	public static final Sound[] PLAYER_KILLING_SOUNDS = new Sound[]{
		Sound.KILLING_SOMEONE_1,
		Sound.KILLING_SOMEONE_2,
		Sound.KILLING_SOMEONE_3,
		Sound.KILLING_SOMEONE_4
	};
}
