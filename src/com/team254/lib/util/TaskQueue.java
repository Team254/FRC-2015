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
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
  }

  public boolean addTask(Runnable r) {
    return bq.offer(r);
  }
 
}
