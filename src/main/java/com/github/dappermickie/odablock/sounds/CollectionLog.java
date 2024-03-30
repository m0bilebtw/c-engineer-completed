package com.github.dappermickie.odablock.sounds;

import com.github.dappermickie.odablock.OdablockConfig;
import static com.github.dappermickie.odablock.OdablockPlugin.ODABLOCK;
import com.github.dappermickie.odablock.Sound;
import com.github.dappermickie.odablock.SoundEngine;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;
import java.util.regex.Pattern;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.Varbits;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.VarbitChanged;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.chat.ChatColorType;
import net.runelite.client.chat.ChatMessageBuilder;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.chat.QueuedMessage;
import net.runelite.client.events.ConfigChanged;

@Singleton
@Slf4j

public class CollectionLog
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
	private ChatMessageManager chatMessageManager;

	@Getter(AccessLevel.PACKAGE)
	@Inject
	private ClientThread clientThread;

	@Inject
	private Pet pet;
	private int lastColLogSettingWarning = -1;
	private boolean gameStateLoggedIn = false;

	private static final Set<Integer> badCollectionLogNotificationSettingValues = Set.of(0, 2);

	private static final Pattern COLLECTION_LOG_ITEM_REGEX = Pattern.compile("New item added to your collection log:.*");

	public boolean onChatMessage(ChatMessage chatMessage)
	{
		if (!config.announceCollectionLog() || !COLLECTION_LOG_ITEM_REGEX.matcher(chatMessage.getMessage()).matches())
		{
			return false;
		}

		int currentTick = client.getTickCount();
		if (currentTick - pet.getReceivedPetTick() <= 10)
		{
			return false;
		}

		if (config.showChatMessages())
		{
			client.addChatMessage(ChatMessageType.PUBLICCHAT, ODABLOCK, "Collection log slot: completed.", null);
		}
		soundEngine.playClip(Sound.COLLECTION_LOG_SLOT, executor);
		return true;
	}

	private void checkAndWarnForCollectionLogNotificationSetting(int newVarbitValue)
	{
		if (!config.announceCollectionLog())
		{
			return;
		}

		if (!gameStateLoggedIn)
		{
			return;
		}

		if (badCollectionLogNotificationSettingValues.contains(newVarbitValue))
		{
			if (lastColLogSettingWarning == -1 || client.getTickCount() - lastColLogSettingWarning > 16)
			{
				lastColLogSettingWarning = client.getTickCount();
				sendHighlightedMessage("Please enable \"Collection log - New addition notification\" in your game settings for C Engineer to know when to announce it! (The chat message one, pop-up doesn't matter)");
			}
		}
	}

	public void onConfigChanged(ConfigChanged event)
	{
		if ("announceCollectionLog".equals(event.getKey()))
		{
			clientThread.invokeLater(() ->
				checkAndWarnForCollectionLogNotificationSetting(client.getVarbitValue(Varbits.COLLECTION_LOG_NOTIFICATION)));
		}
	}

	public void onGameStateChanged(GameStateChanged event)
	{
		gameStateLoggedIn = event.getGameState() == GameState.LOGGED_IN;
	}

	public void setlastColLogSettingWarning()
	{
		lastColLogSettingWarning = client.getTickCount(); // avoid warning during DC
	}

	public void onVarbitChanged(VarbitChanged event)
	{
		if (event.getVarbitId() == Varbits.COLLECTION_LOG_NOTIFICATION)
		{
			checkAndWarnForCollectionLogNotificationSetting(event.getValue());
		}
	}


	private void sendHighlightedMessage(String message)
	{
		String highlightedMessage = new ChatMessageBuilder()
			.append(ChatColorType.HIGHLIGHT)
			.append(message)
			.build();

		chatMessageManager.queue(QueuedMessage.builder()
			.type(ChatMessageType.CONSOLE)
			.runeLiteFormattedMessage(highlightedMessage)
			.build());
	}
}
