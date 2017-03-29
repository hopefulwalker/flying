/**
 * Created by Walker.Zhang on 2015/6/1.
 * Revision History:
 * Date          Who              Version      What
 * 2015/6/1     Walker.Zhang     0.1.0        Created.
 */
package com.flying.util.schedule;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Scheduler {
    private static final long DEFAULT_INITIAL_DELAY = 3 * 1000;
    private static final long DEFAULT_DELAY = 5 * 1000;
    private static final TimeUnit DEFAULT_TIME_UNIT = TimeUnit.MILLISECONDS;

    private long initialDelay = DEFAULT_INITIAL_DELAY;
    private long delay = DEFAULT_DELAY;
    private List<Runnable> tasks;
    private ScheduledExecutorService scheduledExecutorService;

    public Scheduler(List<Runnable> tasks) {
        this.tasks = tasks;
    }

    public void setInitialDelay(long initialDelay) {
        this.initialDelay = initialDelay;
    }

    public void setDelay(long delay) {
        this.delay = delay;
    }

    public void launch(Runnable task) {
        tasks.add(task);
        if (!scheduledExecutorService.isShutdown())
            scheduledExecutorService.scheduleWithFixedDelay(task, initialDelay, delay, DEFAULT_TIME_UNIT);
    }

    public void start() {
        if (scheduledExecutorService == null || scheduledExecutorService.isShutdown()) {
            scheduledExecutorService = Executors.newScheduledThreadPool(1);
            for (Runnable task : tasks) {
                scheduledExecutorService.scheduleWithFixedDelay(task, initialDelay, delay, DEFAULT_TIME_UNIT);
            }
        }
    }

    public void stop() {
        if (scheduledExecutorService.isShutdown()) return;
        scheduledExecutorService.shutdown();
    }
}