package com.github.m0bilebtw.sound;

import lombok.extern.slf4j.Slf4j;
import net.runelite.client.RuneLite;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;

@Slf4j
public class SoundFileManager {
    private SoundFileManager() {}

    private static final Path DOWNLOAD_DIR = Path.of(RuneLite.RUNELITE_DIR.getPath(), "c-engineer-sounds");
    private static final String DELETE_WARNING_FILENAME = "_EXTRA_FILES_WILL_BE_DELETED_BUT_FOLDERS_WILL_REMAIN";
    private static final Path DELETE_WARNING_FILE = DOWNLOAD_DIR.resolve(DELETE_WARNING_FILENAME);

    private static final HttpUrl RAW_GITHUB = HttpUrl.parse("https://raw.githubusercontent.com/m0bilebtw/c-engineer-completed/sounds");

    public static File getSoundFile(Sound sound) {
        return DOWNLOAD_DIR.resolve(sound.getResourceName()).toFile();
    }

    public static void prepareSoundFiles(OkHttpClient okHttpClient, boolean downloadStreamerTrolls) {
        ensureDownloadDirectoryExists();
        deleteUndesiredFilesIgnoringFolders(downloadStreamerTrolls);
        downloadNotYetPresentSounds(okHttpClient, downloadStreamerTrolls);
    }

    private static void ensureDownloadDirectoryExists() {
       try {
           if (!Files.exists(DOWNLOAD_DIR))
                Files.createDirectories(DOWNLOAD_DIR);
            Files.createFile(DELETE_WARNING_FILE);
        } catch (FileAlreadyExistsException ignored) {
            /* ignored */
        } catch (IOException e) {
            log.error("Could not create download directory or warning file", e);
        }
    }

    private static void deleteUndesiredFilesIgnoringFolders(boolean keepStreamerTrolls) {
        Set<String> desiredSoundFileNames = getDesiredSounds(keepStreamerTrolls)
                .map(Sound::getResourceName)
                .collect(Collectors.toSet());

        Set<Path> toDelete = getFilesPresent().stream()
                .filter(not(desiredSoundFileNames::contains))
                .map(DOWNLOAD_DIR::resolve)
                .collect(Collectors.toSet());
        try {
            for (Path pathToDelete : toDelete) {
                Files.delete(pathToDelete);
            }
        } catch (IOException e) {
            log.error("Failed to delete disused sound files", e);
        }
    }

    private static void downloadNotYetPresentSounds(OkHttpClient okHttpClient, boolean downloadStreamerTrolls) {
        getFilesToDownload(downloadStreamerTrolls)
                .forEach(filename -> downloadFilename(okHttpClient, filename));
    }

    private static void downloadFilename(OkHttpClient okHttpClient, String filename) {
        if (RAW_GITHUB == null) {
            // Hush intellij, it's okay, the potential NPE can't hurt you now
            log.error("C Engineer Completed could not download sounds due to an unexpected null RAW_GITHUB value");
            return;
        }
        HttpUrl soundUrl = RAW_GITHUB.newBuilder().addPathSegment(filename).build();
        Request request = new Request.Builder().url(soundUrl).build();
        try (Response res = okHttpClient.newCall(request).execute()) {
            if (res.body() != null)
                Files.copy(new BufferedInputStream(res.body().byteStream()), DOWNLOAD_DIR.resolve(filename), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            log.error("C Engineer Completed could not download sounds", e);
        }
    }

    private static Stream<String> getFilesToDownload(boolean downloadStreamerTrolls) {
        Set<String> filesAlreadyPresent = getFilesPresent();

        return getDesiredSounds(downloadStreamerTrolls)
                .map(Sound::getResourceName)
                .filter(not(filesAlreadyPresent::contains));
    }

    private static Set<String> getFilesPresent() {
        try (Stream<Path> paths = Files.list(DOWNLOAD_DIR)) {
            return paths
                    .filter(path -> !Files.isDirectory(path))
                    .map(Path::toFile)
                    .map(File::getName)
                    .filter(filename -> !DELETE_WARNING_FILENAME.equals(filename))
                    .collect(Collectors.toSet());
        } catch (IOException e) {
            log.warn("Could not list files in {}, assuming it to be empty", DOWNLOAD_DIR);
            return Set.of();
        }
    }

    private static Stream<Sound> getDesiredSounds(boolean includeStreamerTrolls) {
        return Arrays.stream(Sound.values())
                .filter(sound -> includeStreamerTrolls || !sound.isStreamerTroll());
    }
}
