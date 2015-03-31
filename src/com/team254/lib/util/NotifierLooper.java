package com.team254.lib.util;

import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.TimerEventHandler;

/**
 * A Looper is an easy way to create a timed task the gets
 * called periodically.
 * <p>
 * Just make a new Looper and give it a Loopable.
 *
 * @author Tom Bottiglieri
 */
public class NotifierLooper extends Looper {

    private double period = 1.0 / 100.0;
    protected Loopable loopable;
    protected String m_name;

    Notifier m_notifier;

    TimerEventHandler m_handler = new TimerEventHandler() {
        @Override
        public void update(Object param) {
            NotifierLooper.this.update();
        }
    };

    public NotifierLooper(String name, Loopable loopable, double period) {
        super(name, loopable, period);
        this.period = period;
        this.loopable = loopable;
        this.m_name = name;
        this.m_notifier = new Notifier(m_handler, this);
    }

    @Override
    public void start() {
        m_notifier.startPeriodic(period);
    }

    @Override
    public void stop() {
        m_notifier.stop();
    }

    protected void update() {
        loopable.update();
    }
}
