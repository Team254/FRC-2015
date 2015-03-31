package com.team254.lib.util;

import java.util.Vector;

/**
 * MultiLooper.java
 * <p>
 * Runs several Loopables simultaneously with one Looper.
 * Useful for running a bunch of control loops
 * with only one Thread worth of overhead.
 *
 * @author Tom Bottiglieri
 */
public class MultiLooper implements Loopable {
    private Looper looper;
    private Vector<Loopable> loopables = new Vector<Loopable>();

    public MultiLooper(String name, double period, boolean use_notifier) {
        if (use_notifier) {
            looper = new NotifierLooper(name, this, period);
        } else {
            looper = new Looper(name, this, period);
        }
    }

    public MultiLooper(String name, double period) {
        this(name, period, false);
    }

    public void update() {
        int i;
        for (i = 0; i < loopables.size(); ++i) {
            Loopable c = loopables.elementAt(i);
            if (c != null) {
                c.update();
            }
        }
    }

    public void start() {
        if (looper instanceof NotifierLooper) {
            NotifierLooper nl = (NotifierLooper) looper;
            nl.start();
        } else {
            looper.start();
        }
    }

    public void stop() {
        looper.stop();
    }

    public void addLoopable(Loopable c) {
        loopables.addElement(c);
    }
}
