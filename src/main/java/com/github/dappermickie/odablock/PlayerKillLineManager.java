package com.github.dappermickie.odablock;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.regex.Pattern;
import net.runelite.client.RuneLite;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PlayerKillLineManager
{
	private static final HttpUrl RAW_GITHUB = HttpUrl.parse("https://raw.githubusercontent.com/dappermickie/odablock-sounds/playerkillpatterns");
	private static final String USERS_FILENAME = "pklines.txt";
	private static Pattern[] patterns = null;

	public static void Setup(final OkHttpClient okHttpClient)
	{
		HttpUrl usersUrl = RAW_GITHUB.newBuilder().addPathSegment(USERS_FILENAME).build();
		try (Response res = okHttpClient.newCall(new Request.Builder().url(usersUrl).build()).execute())
		{
			if (res.body() != null)
			{
				String[] lines = res.body().string().split("\n");
				patterns = new Pattern[lines.length];

				for (int i = 0; i < lines.length; i++)
				{
					patterns[i] = Pattern.compile(lines[i]);
				}
			}
		}
		catch (IOException e)
		{
			return;
		}
	}

	public static Pattern[] getPatterns()
	{
		return patterns;
	}
}
