package com.github.dappermickie.odablock.sounds;

import com.github.dappermickie.odablock.OdablockConfig;
import com.github.dappermickie.odablock.Sound;
import com.github.dappermickie.odablock.SoundEngine;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Actor;
import net.runelite.api.Client;
import net.runelite.api.NPC;
import net.runelite.api.Player;
import net.runelite.api.annotations.Varbit;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.InteractingChanged;
import net.runelite.api.events.NpcDespawned;
import net.runelite.client.game.NpcUtil;
import net.runelite.client.util.Text;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.concurrent.ScheduledExecutorService;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Singleton
@Slf4j
public class KillingRat extends TimedSoundBase
{

	@Inject
	private Client client;

	@Inject
	private OdablockConfig config;

	@Inject
	private SoundEngine soundEngine;

	@Inject
	private ScheduledExecutorService executor;

	@Inject
	private NpcUtil npcUtil;

	private NPC currentlyInteractingWithRat;
	private int lastInteractedWithRatTick = -1;

	private static final Pattern KILLCOUNT_PATTERN = Pattern.compile("Your Scurrius kill count is:.*");

	private String[] ratNames = new String[]{
		"rat",
		"giant rat",
		"scurrius",
		"zombie rat",
		"brine rat",
		"hell-rat behemoth",
		"dungeon rat",
		"crypt rat",
		"giant crypt rat",
		"giant rat (scurrius)" // ?
	};

	KillingRat()
	{
		super(10);
	}

	public boolean onChatMessage(ChatMessage chatMessage)
	{
		if (chatMessage.getName() != "")
		{
			return false;
		}

		int currentTick = client.getTickCount();
		if (!canPlaySound(currentTick))
		{
			return false;
		}

		if (!KILLCOUNT_PATTERN.matcher(chatMessage.getMessage()).matches())
		{
			return false;
		}

		setLastPlayedTickTick(currentTick);
		soundEngine.playClip(Sound.KILLING_RAT_OR_SCURRIUS, executor);
		return true;
	}

	public void onNpcDespawned(NpcDespawned npcDespawned)
	{
		int currentTick = client.getTickCount();
		if (!canPlaySound(currentTick))
		{
			return;
		}

		NPC npc = npcDespawned.getNpc();

		if (npc != currentlyInteractingWithRat)
		{
			return;
		}

		if (!npcUtil.isDying(npc))
		{
			return;
		}

		if (!isNpcRat(npc))
		{
			return;
		}

		if (currentTick - lastInteractedWithRatTick > 10)
		{
			// Someone else probably killed it
			return;
		}

		setLastPlayedTickTick(currentTick);
		soundEngine.playClip(Sound.KILLING_RAT_OR_SCURRIUS, executor);
	}


	public void onInteractingChanged(InteractingChanged event)
	{
		// Check if we don't have a player, return
		if (!(event.getSource() instanceof Player) && !(event.getTarget() instanceof Player))
		{
			return;
		}

		Player player;
		NPC npc;

		// Check if source is player
		if (event.getSource() instanceof Player)
		{
			// Check if target is npc, if not return
			if (!(event.getTarget() instanceof NPC))
			{
				return;
			}
			player = (Player) event.getSource();
			npc = (NPC) event.getTarget();
		}
		else
		{
			// Check if source is npc, if not return
			if (!(event.getSource() instanceof NPC))
			{
				return;
			}
			player = (Player) event.getTarget();
			npc = (NPC) event.getSource();
		}

		Player localPlayer = client.getLocalPlayer();
		// We must be interacting with the npc
		if (player != localPlayer)
		{
			return;
		}

		// Check if npc is rat
		if (!isNpcRat(npc))
		{
			return;
		}
		lastInteractedWithRatTick = client.getTickCount();
		currentlyInteractingWithRat = npc;
	}

	private boolean isNpcRat(NPC npc)
	{
		String name = Text.standardize(npc.getName());
		for (String ratName : ratNames)
		{
			if (ratName.equals(name))
			{
				return true;
			}
		}

		return false;
	}
}
