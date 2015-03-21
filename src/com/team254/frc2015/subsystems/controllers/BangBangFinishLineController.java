package com.team254.frc2015.subsystems.controllers;

import com.team254.lib.util.Controller;

public class BangBangFinishLineController extends Controller {

    private double m_position;
    private double m_goal;
    private double m_tolerance;
    private double m_direction = 0.0;

    public BangBangFinishLineController(double tolerance) {
        m_tolerance = tolerance;
    }

    public void setGoal(double goal) {
        m_goal = goal;
    }
    
    public double getGoal() {
        return m_goal;
    }

    @Override
    public void reset() {
        m_direction = 0.0;
    }

    @Override
    public boolean isOnTarget() {
        return (m_direction > 0 ? m_position > (m_goal - m_tolerance)
                : m_position < (m_goal + m_tolerance));
    }

    public double update(double position) {
        if (m_direction == 0.0) {
            m_direction = (position > m_goal ? -1.0 : 1.0);
        }
        m_position = position;
        if (m_direction > 0) {
            if (position < (m_goal - m_tolerance)) {
                return 1.0;
            } else {
                return 0.0;
            }
        } else {
            if (position > (m_goal + m_tolerance)) {
                return -1.0;
            } else {
                return 0.0;
            }
        }

    }

}
