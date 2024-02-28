package com.github.dappermickie.odablock;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.RuneLite;
import net.runelite.client.util.Text;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@Slf4j
public abstract class SnowballUserManager
{

	private static final File DOWNLOAD_DIR = new File(RuneLite.RUNELITE_DIR.getPath() + File.separator + "odablock-users");
	private static final HttpUrl RAW_GITHUB = HttpUrl.parse("https://raw.githubusercontent.com/dappermickie/odablock-sounds/snowball");
	private static final String USERVERSION_FILENAME = "USERVERSION";
	private static final String USERS_FILENAME = "users.txt";

	private static String[] users = null;

	public static void ensureDownloadDirectoryExists()
	{
		if (!DOWNLOAD_DIR.exists())
		{
			DOWNLOAD_DIR.mkdirs();
		}
	}

	public static void downloadSnowballUsers(final OkHttpClient okHttpClient)
	{
		HttpUrl versionUrl = RAW_GITHUB.newBuilder().addPathSegment(USERVERSION_FILENAME).build();
		int latestVersion = -1;
		try (Response res = okHttpClient.newCall(new Request.Builder().url(versionUrl).build()).execute())
		{
			if (res.body() != null)
			{
				latestVersion = Integer.parseInt(Text.standardize(res.body().string()));
			}
		}
		catch (IOException e)
		{
			log.error("Odablock Plugin could not download user version", e);
			return;
		}

		int currentVersion = -1;
		try
		{
			currentVersion = getUserVersion();
		}
		catch (IOException e)
		{
			// No current version available
			var soundVersionFile = new File(DOWNLOAD_DIR, USERVERSION_FILENAME);
			try
			{
				soundVersionFile.createNewFile();

			}
			catch (IOException e2)
			{
				log.error("Couldn't create userversion file");
			}
		}

		if (latestVersion == currentVersion)
		{
			return;
		}

		writeLatestVersion(latestVersion);

		HttpUrl usersUrl = RAW_GITHUB.newBuilder().addPathSegment(USERS_FILENAME).build();
		Path outputPath = Paths.get(DOWNLOAD_DIR.getPath(), USERS_FILENAME);
		try (Response res = okHttpClient.newCall(new Request.Builder().url(usersUrl).build()).execute())
		{
			if (res.body() != null)
			{
				Files.copy(new BufferedInputStream(res.body().byteStream()), outputPath, StandardCopyOption.REPLACE_EXISTING);
			}
		}
		catch (IOException e)
		{
			log.error("Odablock Plugin could not download snowball users", e);
			return;
		}
	}

	public static String[] getUsers()
	{
		if (SnowballUserManager.users != null)
		{
			return SnowballUserManager.users;
		}
		File userVersionFile = new File(DOWNLOAD_DIR, USERS_FILENAME);
		try
		{
			String userVersionFileContent = Files.readString(userVersionFile.toPath());
			String[] originalUsers = userVersionFileContent.split(",");
			String[] users = new String[originalUsers.length];

			for (int i = 0; i < originalUsers.length; i++)
			{
				users[i] = Text.standardize(originalUsers[i]);
			}

			SnowballUserManager.users = users;

			return users;
		}
		catch (IOException e)
		{
			return null;
		}
	}

	private static int getUserVersion() throws IOException
	{
		File userVersionFile = new File(DOWNLOAD_DIR, USERVERSION_FILENAME);
		String userVersionFileContent = Files.readString(userVersionFile.toPath());
		return Integer.parseInt(userVersionFileContent);
	}

	private static void writeLatestVersion(int version)
	{
		var soundVersionFile = new File(DOWNLOAD_DIR, USERVERSION_FILENAME);
		try
		{
			FileWriter myWriter = new FileWriter(soundVersionFile);
			myWriter.write(String.valueOf(version));
			myWriter.close();
		}
		catch (IOException e)
		{
			log.error("Couldn't write latest userversion");
		}
	}
}
