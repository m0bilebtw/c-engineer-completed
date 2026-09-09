package com.github.m0bilebtw.sound;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AnnouncementQueueTest {

    private static final long CLIP_LENGTH_MILLIS = 1000;

    private final List<Sound> played = new ArrayList<>();
    private final List<ScheduledTask> scheduled = new ArrayList<>();

    private long now;
    private AnnouncementQueue queue;

    private static class ScheduledTask {
        private final long dueAt;
        private final Runnable task;

        private ScheduledTask(long dueAt, Runnable task) {
            this.dueAt = dueAt;
            this.task = task;
        }
    }

    @Before
    public void setUp() {
        played.clear();
        scheduled.clear();
        now = 10_000;
        queue = new AnnouncementQueue(played::add, () -> now, (delay, task) -> scheduled.add(new ScheduledTask(now + delay, task)), sound -> CLIP_LENGTH_MILLIS);
    }

    /**
     * Move time forward, running anything that came due on the way, in the order it came due.
     */
    private void advance(long millis) {
        long target = now + millis;
        while (true) {
            ScheduledTask next = null;
            for (ScheduledTask candidate : scheduled) {
                if (candidate.dueAt <= target && (next == null || candidate.dueAt < next.dueAt))
                    next = candidate;
            }
            if (next == null)
                break;

            scheduled.remove(next);
            now = Math.max(now, next.dueAt);
            next.task.run();
        }
        now = target;
    }

    @Test
    public void playsASingleAnnouncementOnceTheWindowHasPassed() {
        queue.request(Sound.SLAYER_TASK);
        assertTrue("nothing should play until the coalescing window closes", played.isEmpty());

        advance(AnnouncementQueue.COALESCE_WINDOW_MILLIS);

        assertEquals(List.of(Sound.SLAYER_TASK), played);
    }

    @Test
    public void playsTheHighestPriorityOfTheAnnouncementsInTheSameWindowFirst() {
        queue.request(Sound.LEVEL_UP);
        queue.request(Sound.COLLECTION_LOG_SLOT);

        advance(AnnouncementQueue.COALESCE_WINDOW_MILLIS);

        assertEquals(List.of(Sound.COLLECTION_LOG_SLOT), played);
    }

    @Test
    public void playsTheRunnerUpAfterTheWinnerHasFinished() {
        queue.request(Sound.LEVEL_UP);
        queue.request(Sound.COLLECTION_LOG_SLOT);

        advance(AnnouncementQueue.COALESCE_WINDOW_MILLIS);
        assertEquals(List.of(Sound.COLLECTION_LOG_SLOT), played);

        advance(CLIP_LENGTH_MILLIS);
        assertEquals(List.of(Sound.COLLECTION_LOG_SLOT, Sound.LEVEL_UP), played);
    }

    @Test
    public void queuesOnlyOneDeepAndDropsTheRest() {
        queue.request(Sound.COLLECTION_LOG_SLOT);
        queue.request(Sound.SLAYER_TASK);
        queue.request(Sound.LEVEL_UP);

        advance(AnnouncementQueue.COALESCE_WINDOW_MILLIS + CLIP_LENGTH_MILLIS * 3);

        assertEquals(List.of(Sound.COLLECTION_LOG_SLOT, Sound.SLAYER_TASK), played);
    }

    @Test
    public void playsAnAnnouncementRequestedWhileAnotherIsPlayingOnceItFinishes() {
        queue.request(Sound.COLLECTION_LOG_SLOT);
        advance(AnnouncementQueue.COALESCE_WINDOW_MILLIS);

        advance(CLIP_LENGTH_MILLIS / 2);
        queue.request(Sound.SLAYER_TASK);
        assertEquals("the queued announcement must wait its turn", List.of(Sound.COLLECTION_LOG_SLOT), played);

        advance(CLIP_LENGTH_MILLIS);
        assertEquals(List.of(Sound.COLLECTION_LOG_SLOT, Sound.SLAYER_TASK), played);
    }

    @Test
    public void keepsTheHighestPriorityOfSeveralRequestedWhileAnotherIsPlaying() {
        queue.request(Sound.SLAYER_TASK);
        advance(AnnouncementQueue.COALESCE_WINDOW_MILLIS);

        queue.request(Sound.LEVEL_UP);
        queue.request(Sound.COLLECTION_LOG_SLOT);
        queue.request(Sound.QOL_GEM_CRAB_MOVED);

        advance(CLIP_LENGTH_MILLIS * 3);

        assertEquals(List.of(Sound.SLAYER_TASK, Sound.COLLECTION_LOG_SLOT), played);
    }

    @Test
    public void dropsAQueuedAnnouncementThatWaitedTooLongToStillBeWorthSaying() {
        long longTrollSound = AnnouncementQueue.MAX_QUEUED_WAIT_MILLIS * 4;
        queue = new AnnouncementQueue(played::add, () -> now, (delay, task) -> scheduled.add(new ScheduledTask(now + delay, task)), sound -> longTrollSound);

        queue.request(Sound.EMOTE_TROLL_AF);
        advance(AnnouncementQueue.COALESCE_WINDOW_MILLIS);
        assertEquals(List.of(Sound.EMOTE_TROLL_AF), played);

        queue.request(Sound.SLAYER_TASK);
        advance(longTrollSound);

        assertEquals("the slayer task was too old to announce by the time the troll finished", List.of(Sound.EMOTE_TROLL_AF), played);
    }

    @Test
    public void keepsFollowingAnnouncementsAfterOneIsDroppedForBeingStale() {
        long longTrollSound = AnnouncementQueue.MAX_QUEUED_WAIT_MILLIS * 4;
        queue = new AnnouncementQueue(played::add, () -> now, (delay, task) -> scheduled.add(new ScheduledTask(now + delay, task)), sound -> sound == Sound.EMOTE_TROLL_AF ? longTrollSound : CLIP_LENGTH_MILLIS);

        queue.request(Sound.EMOTE_TROLL_AF);
        advance(AnnouncementQueue.COALESCE_WINDOW_MILLIS);
        queue.request(Sound.SLAYER_TASK);
        advance(longTrollSound);
        assertEquals(List.of(Sound.EMOTE_TROLL_AF), played);

        queue.request(Sound.COLLECTION_LOG_SLOT);
        advance(AnnouncementQueue.COALESCE_WINDOW_MILLIS);

        assertEquals(List.of(Sound.EMOTE_TROLL_AF, Sound.COLLECTION_LOG_SLOT), played);
    }

    @Test
    public void keepsTheFirstRequestWhenPrioritiesAreEqual() {
        queue.request(Sound.SLAYER_TASK);
        queue.request(Sound.COMBAT_TASK);

        advance(AnnouncementQueue.COALESCE_WINDOW_MILLIS);

        assertEquals(List.of(Sound.SLAYER_TASK), played);
    }

    @Test
    public void returnsToPlayingImmediatelyOnceEverythingHasDrained() {
        queue.request(Sound.SLAYER_TASK);
        advance(AnnouncementQueue.COALESCE_WINDOW_MILLIS + CLIP_LENGTH_MILLIS);
        assertEquals(List.of(Sound.SLAYER_TASK), played);

        advance(60_000);

        queue.request(Sound.LEVEL_UP);
        advance(AnnouncementQueue.COALESCE_WINDOW_MILLIS);

        assertEquals(List.of(Sound.SLAYER_TASK, Sound.LEVEL_UP), played);
    }
}
