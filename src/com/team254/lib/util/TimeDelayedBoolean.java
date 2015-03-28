package com.team254.lib.util;

import edu.wpi.first.wpilibj.Timer;

public class TimeDelayedBoolean {

    private Timer t = new Timer();
    private boolean m_old = false;

    public boolean update(boolean value, double timeout) {
        if (!m_old && value) {
            t.reset();
            t.start();
        }
        m_old = value;
        return value && t.get() >= timeout;
    }
}
