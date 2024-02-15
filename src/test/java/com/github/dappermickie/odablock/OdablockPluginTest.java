package com.github.dappermickie.odablock;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class OdablockPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(OdablockPlugin.class);
		RuneLite.main(args);
	}
}