package com.github.dappermickie.odablock.sounds;


import com.github.dappermickie.odablock.DhAxeStyles;
import com.github.dappermickie.odablock.OdablockConfig;
import com.github.dappermickie.odablock.OdablockVarbitValues;
import com.github.dappermickie.odablock.OdablockVarbits;
import com.github.dappermickie.odablock.RandomSoundUtility;
import com.github.dappermickie.odablock.Sound;
import com.github.dappermickie.odablock.SoundEngine;
import java.util.Random;
import java.util.concurrent.ScheduledExecutorService;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.EquipmentInventorySlot;
import net.runelite.api.InventoryID;
import net.runelite.api.Item;
import net.runelite.api.ItemContainer;
import net.runelite.api.ItemID;
import net.runelite.api.events.VarbitChanged;
import net.runelite.client.callback.ClientThread;

@Singleton
@Slf4j
public class DhAxe extends TimedSoundBase
{

	@Inject
	private Client client;

	@Inject
	private OdablockConfig config;

	@Inject
	private SoundEngine soundEngine;

	@Inject
	private ScheduledExecutorService executor;
	@Getter(AccessLevel.PACKAGE)
	@Inject
	private ClientThread clientThread;

	private static final Random random = new Random();

	private int current43 = -1;
	private int current46 = -1;
	private int current843 = 0;
	private DhAxeStyles dhAxeStyle;
	private final int[] dhAxeIds = new int[]{ItemID.DHAROKS_GREATAXE, ItemID.DHAROKS_GREATAXE_100, ItemID.DHAROKS_GREATAXE_75, ItemID.DHAROKS_GREATAXE_50, ItemID.DHAROKS_GREATAXE_25, ItemID.SOULREAPER_AXE, ItemID.SOULREAPER_AXE_28338};

	DhAxe()
	{
		super(5);
	}

	public void onVarbitChanged(VarbitChanged varbitChanged)
	{
		final int varpId = varbitChanged.getVarpId();
		final int value = varbitChanged.getValue();

		// Always set current values because if you switch weapons and the values stay the same, this method won't get triggered
		if (varpId == OdablockVarbits.COMBAT_STYLE_43.VarpId)
		{
			current43 = value;
		}
		else if (varpId == OdablockVarbits.COMBAT_STYLE_46.VarpId)
		{
			current46 = value;
		}
		else if (varpId == OdablockVarbits.IS_WEARING_WEAPON.VarpId)
		{
			current843 = value;
		}
		else
		{
			return;
		}

		if (!config.dhAxe())
		{
			return;
		}
		clientThread.invokeLater(this::playSound);
	}

	public void playSound()
	{
		// Only necessary to do any checks if we're wearing the dh axe
		// And if we're not depositing worn items to the bank
		if (!isUsingDhAxe())
		{
			return;
		}

		Sound sound = getSoundForDhAxeStyle();
		if (sound == null)
		{
			return;
		}
		final int currentTick = client.getTickCount();
		if (canPlaySound(currentTick))
		{
			soundEngine.playClip(sound, executor);

			setLastPlayedTickTick(currentTick);
		}
	}

	private boolean isUsingDhAxe()
	{
		if (current843 == 0)
		{
			return false;
		}

		final ItemContainer itemContainer = client.getItemContainer(InventoryID.EQUIPMENT);
		if (itemContainer == null)
		{
			return false;
		}
		final Item item = itemContainer.getItem(EquipmentInventorySlot.WEAPON.getSlotIdx());
		if (item == null)
		{
			return false;
		}
		final int itemId = item.getId();

		for (int id : dhAxeIds)
		{
			if (id == itemId)
			{
				return true;
			}
		}

		return false;
	}

	private Sound getSoundForDhAxeStyle()
	{
		Sound sound = null;
		if (current43 == OdablockVarbitValues.COMBAT_STYLE_43_0.Value &&
			current46 == OdablockVarbitValues.COMBAT_STYLE_46_1.Value)
		{
			sound = RandomSoundUtility.getRandomSound(Sound.DH_AXE_CHOP_SOUNDS);
		}
		else if (current43 == OdablockVarbitValues.COMBAT_STYLE_43_1.Value &&
			current46 == OdablockVarbitValues.COMBAT_STYLE_46_2.Value)
		{
			sound = RandomSoundUtility.getRandomSound(Sound.DH_AXE_HACK_SOUNDS);
		}
		else if (current43 == OdablockVarbitValues.COMBAT_STYLE_43_2.Value &&
			current46 == OdablockVarbitValues.COMBAT_STYLE_46_2.Value)
		{
			sound = RandomSoundUtility.getRandomSound(Sound.DH_AXE_SMASH_SOUNDS);
		}
		else if (current43 == OdablockVarbitValues.COMBAT_STYLE_43_3.Value &&
			current46 == OdablockVarbitValues.COMBAT_STYLE_46_3.Value)
		{
			sound = RandomSoundUtility.getRandomSound(Sound.DH_AXE_BLOCK_SOUNDS);
		}
		return sound;
	}
}
