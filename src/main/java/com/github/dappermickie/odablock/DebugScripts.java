package com.github.dappermickie.odablock;

import java.util.concurrent.ScheduledExecutorService;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.MenuAction;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.events.ScriptCallbackEvent;
import net.runelite.api.events.VarbitChanged;
import net.runelite.api.events.WidgetLoaded;

@Singleton
@Slf4j
public class DebugScripts
{

	@Inject
	private Client client;

	@Inject
	private OdablockConfig config;

	@Inject
	private SoundEngine soundEngine;

	@Inject
	private ScheduledExecutorService executor;

	public void onMenuOptionClicked(MenuOptionClicked menuOptionClicked)
	{
		int currentTick = client.getTickCount();

		// Thank you RuneWatch for groupId
		int groupId = OdablockPlugin.TO_GROUP(menuOptionClicked.getParam1());
		String option = menuOptionClicked.getMenuOption();
		MenuAction action = menuOptionClicked.getMenuAction();

		StringBuilder builder = new StringBuilder();
		builder.append("MENU GID: ");
		builder.append(groupId);
		builder.append(" ; OPTION: ");
		builder.append(option);
		builder.append(" ; ACTIONID: ");
		builder.append(action.getId());
		builder.append(" ; TICK: ");
		builder.append(currentTick);
		builder.append("\n");

		Logger.getGlobal().log(Level.INFO, builder.toString());
	}

	public void onVarbitChanged(VarbitChanged varbitChanged)
	{
		StringBuilder builder = new StringBuilder();
		builder.append("BIT: ");
		builder.append(varbitChanged.getVarbitId());
		builder.append(" ; VALUE: ");
		builder.append(varbitChanged.getValue());
		builder.append(" ; VARPID: ");
		builder.append(varbitChanged.getVarpId());
		builder.append("\n");

		Logger.getGlobal().log(Level.INFO, builder.toString());
	}

	public void onWidgetLoaded(WidgetLoaded event)
	{
		StringBuilder builder = new StringBuilder();
		builder.append("WIDGET GROUP ID: ");
		builder.append(event.getGroupId());
		Logger.getGlobal().log(Level.INFO, builder.toString());

		var widget = client.getWidget(event.getGroupId());
	}

	public void onScriptCallbackEvent(ScriptCallbackEvent scriptCallbackEvent)
	{
		String ename = scriptCallbackEvent.getEventName();
		if (ename.equals("outerZoomLimit") ||
			ename.equals("innerZoomLimit") ||
			ename.equals("friendsChatSetPosition") ||
			ename.equals("friendsChatSetText") ||
			ename.equals("zoomExpToLin") ||
			ename.equals("scrollWheelZoom") ||
			ename.equals("scrollWheelZoomIncrement"))
		{
			return;
		}
		StringBuilder builder = new StringBuilder();
		builder.append("EID: ");
		builder.append(scriptCallbackEvent.hashCode());
		builder.append("; ENAME: ");
		builder.append(ename);
		Logger.getGlobal().log(Level.INFO, builder.toString());
	}
}
