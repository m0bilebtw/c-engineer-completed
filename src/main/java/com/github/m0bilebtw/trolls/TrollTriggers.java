package com.github.m0bilebtw.trolls;

import com.github.m0bilebtw.CEngineerCompletedConfig;
import com.github.m0bilebtw.emote.EmoteTriggers;
import com.github.m0bilebtw.player.CEngineerPlayer;
import com.github.m0bilebtw.projectile.ProjectileID;
import com.github.m0bilebtw.sound.Sound;
import com.github.m0bilebtw.sound.SoundEngine;
import net.runelite.api.Actor;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.Projectile;
import net.runelite.api.events.AnimationChanged;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.ProjectileMoved;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.util.Text;

import javax.inject.Inject;
import java.time.Duration;
import java.util.concurrent.ScheduledExecutorService;
import java.util.regex.Pattern;

public class TrollTriggers {
    private static final Pattern STAT_SPY_REGEX = Pattern.compile(Text.standardize(CEngineerPlayer.RSN + " is reading your skill stats!"));
    private static final Pattern ESCAPE_CRYSTAL_REGEX = Pattern.compile(Text.standardize(CEngineerPlayer.RSN + " activated your crystal\\."));

    private static final int SNOWBALL_COOLDOWN_TICKS = 50;
    private static final Duration SNOWBALL_DELAY_SOUNDS = Duration.ofSeconds(20);

    @Inject
    private Client client;

    @Inject
    private CEngineerCompletedConfig config;

    @Inject
    private ScheduledExecutorService executor;

    @Inject
    private SoundEngine soundEngine;

    @Inject
    private CEngineerPlayer cEngineer;

    @Inject
    private EmoteTriggers emoteTriggers;

    private int lastSnowballTriggerTick = -1;

    @Subscribe
    public void onChatMessage(ChatMessage chatMessage) {
        if (chatMessage.getType() != ChatMessageType.GAMEMESSAGE && chatMessage.getType() != ChatMessageType.SPAM)
            return;

        if (config.easterEggs() && STAT_SPY_REGEX.matcher(Text.standardize(chatMessage.getMessage())).matches()) {
            soundEngine.playClip(Sound.STAT_SPY_SOUP, executor);

        } else if (config.easterEggs() && ESCAPE_CRYSTAL_REGEX.matcher(Text.standardize(chatMessage.getMessage())).matches()) {
            soundEngine.playClip(Sound.ESCAPE_CRYSTAL, executor);
        }
    }

    @Subscribe
    public void onAnimationChanged(AnimationChanged animationChanged) {
        if (cEngineer.isOutOfRenderDistance())
            return;

        Actor actor = animationChanged.getActor();
        if (!cEngineer.actorEquals(actor))
            return;

        if (!config.easterEggs())
            return;

        int actorAnimationId = actor.getAnimation();
        if (actorAnimationId == -1)
            return;

        emoteTriggers.runTriggers(actorAnimationId);
    }

    @Subscribe
    public void onProjectileMoved(ProjectileMoved projectileMoved) {
        if (cEngineer.isOutOfRenderDistance())
            return;

        if (!config.easterEggs())
            return;

        Projectile projectile = projectileMoved.getProjectile();
        if (projectile.getId() != ProjectileID.SNOWBALL)
            return;

        int currentTick = client.getTickCount();
        if (currentTick - lastSnowballTriggerTick < SNOWBALL_COOLDOWN_TICKS)
            return;

        Actor myself = client.getLocalPlayer();
        if (myself == null)
            return;

        Actor projectileInteracting = projectile.getInteracting();
        if (!myself.equals(projectileInteracting))
            return;

        if (cEngineer.couldHaveThrownProjectileFrom(projectile)) {
            lastSnowballTriggerTick = currentTick;
            soundEngine.playClip(Sound.randomSnowballSound(), executor, SNOWBALL_DELAY_SOUNDS);
        }
    }
}
