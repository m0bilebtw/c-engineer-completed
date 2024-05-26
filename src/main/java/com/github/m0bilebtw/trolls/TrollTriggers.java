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
import net.runelite.api.ItemID;
import net.runelite.api.Projectile;
import net.runelite.api.coords.WorldArea;
import net.runelite.api.coords.WorldPoint;
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
    private static final Duration SNOWBALL_DELAY_GAUNTLET_SOUND = Duration.ofSeconds(450);
    private static final Duration SNOWBALL_DELAY_SOUNDS = Duration.ofSeconds(20);

    private static final WorldArea GAUNTLET_LOBBY = new WorldArea(new WorldPoint(3026, 6117, 1), 14, 14);

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
            playSoundFromSnowball();
        }
    }

    private void playSoundFromSnowball() {
        WorldPoint currentLocation = WorldPoint.fromLocalInstance(client, client.getLocalPlayer().getLocalLocation());
        if (GAUNTLET_LOBBY.contains(currentLocation)) {
            soundEngine.playClip(Sound.SNOWBALL_GAUNTLET_LOBBY, executor, SNOWBALL_DELAY_GAUNTLET_SOUND);
            return;
        }

        Sound sound = pickSnowballSoundBasedOnEquipment();
        if (cEngineer.isWearing(ItemID._3RD_AGE_AMULET)) {
            soundEngine.playClip(sound, executor);
        } else {
            soundEngine.playClip(sound, executor, SNOWBALL_DELAY_SOUNDS);
        }
    }

    private Sound pickSnowballSoundBasedOnEquipment() {
        if (cEngineer.isWearing(ItemID.BUCKET_HELM_G))
            return Sound.SNOWBALL_EQUIPPING_BUCKET_HELM_G;

        if (cEngineer.isWearing(ItemID.GIANT_BOOT))
            return Sound.SNOWBALL_EQUIPPING_GIANT_BOOT;

        if (cEngineer.isWearing(ItemID.SAGACIOUS_SPECTACLES))
            return Sound.SNOWBALL_EQUIPPING_SAGACIOUS_SPECTACLES;

        if (cEngineer.isWearing(ItemID.MASK_OF_REBIRTH))
            return Sound.SNOWBALL_EQUIPPING_MASK_OF_REBIRTH;

        return Sound.randomSnowballSoundNotFromEquippedItem();
    }
}
