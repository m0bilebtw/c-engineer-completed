package com.github.m0bilebtw.player;

import com.github.m0bilebtw.sound.Sound;
import com.github.m0bilebtw.sound.SoundEngine;
import net.runelite.api.Client;
import net.runelite.api.events.ChatMessage;

import javax.inject.Inject;
import java.util.Optional;
import java.util.concurrent.ScheduledExecutorService;

public class CEngineerChatTrolls {

    @Inject
    private Client client;

    @Inject
    private SoundEngine soundEngine;

    @Inject
    private ScheduledExecutorService executor;

    public void runTriggers(ChatMessage chatMessageFromC) {
        Optional<String> lowerJagexNameOpt = LocalPlayer.getLowerJagexName(client);
        if (lowerJagexNameOpt.isEmpty()) return;

        String lowerJagexName = lowerJagexNameOpt.get();
        if ("skill specs".equals(lowerJagexName))
            skillSpecsChatTrolls(chatMessageFromC);
    }

    private void skillSpecsChatTrolls(ChatMessage chatMessageFromC) {
        switch (chatMessageFromC.getMessage().toLowerCase()) {
            case "hello skillspecs":
                soundEngine.playClip(Sound.CHAT_TROLL_SKILL_SPECS_HELLO, executor);
                break;
            case "are you ok":
                soundEngine.playClip(Sound.CHAT_TROLL_SKILL_SPECS_OK, executor);
                break;
            case "oops":
                soundEngine.playClip(Sound.CHAT_TROLL_SKILL_SPECS_OOPS, executor);
                break;
            default: break;
        }
    }
}
