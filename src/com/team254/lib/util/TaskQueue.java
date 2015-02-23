package com.team254.lib.util;

import java.util.concurrent.ArrayBlockingQueue;

public class TaskQueue implements Runnable {
    public ArrayBlockingQueue<Runnable> bq;
    Thread thread;
    private boolean running = true;

    public TaskQueue(int size) {
        bq = new ArrayBlockingQueue<Runnable>(size);
    }

    public void start() {
        running = true;
        if (thread == null || !thread.isAlive()) {
            thread = new Thread(this);
        }
        thread.setName("TaskQueue - " + this.toString());
        thread.setPriority(Thread.MIN_PRIORITY); // All tasks should go slow for now!
        thread.start();

    }

    public void stop() {
        running = false;
    }

    @Override
    public void run() {
        while (running) {
            Runnable runnable;
            try {
                runnable = bq.take();
                if (runnable != null) {
                    runnable.run();
                }
            } catch (InterruptedException e) {
                System.err.println("Caught InterruptedException in task queue");
                e.printStackTrace();
            } catch (RuntimeException e) {
                System.err.println("Caught run time exception in task queue");
                e.printStackTrace();
            }
        }
    }

    public boolean addTask(Runnable r) {
        return bq.offer(r);
    }

}
