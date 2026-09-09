package com.github.m0bilebtw.sound;

import java.util.function.Consumer;
import java.util.function.LongSupplier;
import java.util.function.ToLongFunction;

/**
 * Keeps C Engineer from talking over himself.
 * <p>
 * Announcements that arrive close together are collected for a short window rather than played the instant they are
 * requested, because several of them routinely land in the same game tick, and only then is it clear which one matters
 * most. The most important of that batch is played, the next most important waits until it has finished, and the rest
 * are dropped. Dropping is deliberate: a queue that keeps everything ends up narrating a busy minute long after it is
 * over.
 */
public class AnnouncementQueue {

    /**
     * How long to wait for other announcements before playing anything. A game tick is 600ms, and announcements that
     * belong to the same moment usually share a tick or sit in adjacent ones, so this is long enough to catch them
     * together while staying short enough that C Engineer does not sound slow to react.
     */
    static final long COALESCE_WINDOW_MILLIS = 300;

    /** Used when a sound's real length cannot be measured, so a queued announcement is never waited on forever. */
    static final long FALLBACK_CLIP_LENGTH_MILLIS = 2_000;

    /**
     * How long a queued announcement may wait before it is no longer worth saying. The troll lines run to half a
     * minute, and an announcement about something that happened that long ago is just confusing.
     */
    static final long MAX_QUEUED_WAIT_MILLIS = 5_000;

    public interface Scheduler {
        void schedule(long delayMillis, Runnable task);
    }

    private final Consumer<Sound> player;
    private final LongSupplier clockMillis;
    private final Scheduler scheduler;
    private final ToLongFunction<Sound> clipLengthMillis;

    private boolean collecting = false;
    private boolean playing = false;

    private Sound winner = null;
    private Sound runnerUp = null;
    private long runnerUpRequestedAt = 0;

    public AnnouncementQueue(Consumer<Sound> player, LongSupplier clockMillis, Scheduler scheduler, ToLongFunction<Sound> clipLengthMillis) {
        this.player = player;
        this.clockMillis = clockMillis;
        this.scheduler = scheduler;
        this.clipLengthMillis = clipLengthMillis;
    }

    public synchronized void request(Sound sound) {
        if (collecting || playing) {
            offerAsCandidate(sound);
            return;
        }

        collecting = true;
        winner = sound;
        runnerUp = null;
        scheduler.schedule(COALESCE_WINDOW_MILLIS, this::closeWindow);
    }

    /**
     * While a window is open the candidate competes to be played first; while something is playing it competes only for
     * the single queued slot. Equal priority loses to whatever got here first, so a burst stays in the order it happened.
     */
    private void offerAsCandidate(Sound sound) {
        if (collecting && beats(sound, winner)) {
            queue(winner);
            winner = sound;
            return;
        }

        if (runnerUp == null || beats(sound, runnerUp))
            queue(sound);
    }

    private void queue(Sound sound) {
        runnerUp = sound;
        runnerUpRequestedAt = clockMillis.getAsLong();
    }

    private static boolean beats(Sound challenger, Sound incumbent) {
        return incumbent == null || challenger.getPriority().compareTo(incumbent.getPriority()) > 0;
    }

    private synchronized void closeWindow() {
        collecting = false;
        play(winner);
    }

    private synchronized void playNextOrFallIdle() {
        playing = false;

        if (runnerUp == null)
            return;

        Sound next = runnerUp;
        boolean stale = clockMillis.getAsLong() - runnerUpRequestedAt > MAX_QUEUED_WAIT_MILLIS;
        runnerUp = null;

        if (!stale)
            play(next);
    }

    private void play(Sound sound) {
        if (sound == null)
            return;

        winner = null;
        playing = true;
        player.accept(sound);
        scheduler.schedule(lengthOf(sound), this::playNextOrFallIdle);
    }

    private long lengthOf(Sound sound) {
        long length = clipLengthMillis.applyAsLong(sound);
        return length > 0 ? length : FALLBACK_CLIP_LENGTH_MILLIS;
    }
}
