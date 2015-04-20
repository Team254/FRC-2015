package com.team254.frc2015.subsystems.controllers;

import com.team254.lib.util.Controller;
import edu.wpi.first.wpilibj.Timer;

public class TimedOpenLoopController extends Controller {

    double m_t0 = 0; // Time of control loop init
    double m_t1 = 0; // Time to start deceling
    double m_t2 = 0; // Time to end decel
    double m_start_power = 0;
    double m_end_power = 0;

    public TimedOpenLoopController(double start_power, double time_full_on, double end_power, double time_to_decel) {
        m_t0 = Timer.getFPGATimestamp();
        m_t1 = m_t0 + time_full_on;
        m_t2 = m_t1 + time_to_decel;
        m_start_power = start_power;
        m_end_power = end_power;
    }

    public boolean expired() {
        return Timer.getFPGATimestamp() > m_t2;
    }


    public double update() {
        double cur = Timer.getFPGATimestamp();
        if (cur <= m_t1) {
            return m_start_power;
        } else if (cur > m_t1 && cur <= m_t2) {
            // decel
            double rel_t = cur - m_t1;
            double slope = (m_end_power - m_start_power) / (m_t2 - m_t1);
            return (m_start_power + (slope * rel_t));
        } else {
            return m_end_power;
        }
    }

    @Override
    public void reset() {
        m_t0 = m_t1 = m_t2 = m_end_power = m_start_power = 0;
    }

    @Override
    public boolean isOnTarget() {
        return expired();
    }
}
