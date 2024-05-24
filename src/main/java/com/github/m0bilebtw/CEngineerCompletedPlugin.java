package com.github.m0bilebtw;

import com.github.m0bilebtw.announce.AnnouncementTriggers;
import com.github.m0bilebtw.eastereggs.EasterEggTriggers;
import com.github.m0bilebtw.player.CEngineerPlayer;
import com.github.m0bilebtw.player.LoggedInState;
import com.github.m0bilebtw.qol.QualityOfLifeTriggers;
import com.github.m0bilebtw.sound.SoundEngine;
import com.github.m0bilebtw.sound.SoundFileManager;
import com.github.m0bilebtw.trolls.TrollTriggers;
import com.google.inject.Provides;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import okhttp3.OkHttpClient;

import javax.inject.Inject;
import java.util.concurrent.ScheduledExecutorService;

@Slf4j
@PluginDescriptor(
        name = "C Engineer: Completed",
        description = "C Engineer announces when you complete an achievement",
        tags = {"c engineer", "stats", "levels", "quests", "diary", "announce"}
)

public class CEngineerCompletedPlugin extends Plugin {
    @Inject
    private Client client;

    @Getter(AccessLevel.PACKAGE)
    @Inject
    private ClientThread clientThread;

    @Inject
    private CEngineerCompletedConfig config;

    @Inject
    private ScheduledExecutorService executor;

    @Inject
    private OkHttpClient okHttpClient;

    @Inject
    private EventBus eventBus;

    @Inject
    private SoundEngine soundEngine;

    @Inject
    private CEngineerPlayer cEngineer;

    @Inject
    private AnnouncementTriggers announcementTriggers;

    @Inject
    private EasterEggTriggers easterEggTriggers;

    @Inject
    private TrollTriggers trollTriggers;

    @Inject
    private QualityOfLifeTriggers qolTriggers;

    @Inject
    private LoggedInState loggedInState;

    @Override
    protected void startUp() throws Exception {
        eventBus.register(cEngineer);
        eventBus.register(announcementTriggers);
        eventBus.register(easterEggTriggers);
        eventBus.register(trollTriggers);
        eventBus.register(qolTriggers);
        eventBus.register(loggedInState);
        loggedInState.setForCurrentGameState(client.getGameState());
        announcementTriggers.initialise();

        executor.submit(() -> SoundFileManager.prepareSoundFiles(okHttpClient, config.downloadStreamerTrolls()));
    }

    @Override
    protected void shutDown() throws Exception {
        eventBus.unregister(cEngineer);
        eventBus.unregister(announcementTriggers);
        eventBus.unregister(easterEggTriggers);
        eventBus.unregister(trollTriggers);
        eventBus.unregister(qolTriggers);
        eventBus.unregister(loggedInState);

        announcementTriggers.shutDown();
        soundEngine.close();
    }

    @Provides
    CEngineerCompletedConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(CEngineerCompletedConfig.class);
    }
}
