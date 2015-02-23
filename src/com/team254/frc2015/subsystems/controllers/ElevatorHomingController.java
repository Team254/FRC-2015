package com.team254.frc2015.subsystems.controllers;

import com.team254.frc2015.subsystems.ElevatorCarriage;
import com.team254.lib.util.Controller;
import edu.wpi.first.wpilibj.DriverStation;

public class ElevatorHomingController extends Controller {
    private ElevatorCarriage m_carriage;
    private boolean m_move_off_positive;
    private double m_dt;
    private double m_zero_point;
    private boolean m_null_controller = false;

    public ElevatorHomingController(ElevatorCarriage carriage,
                                    boolean move_off_positive, double dt) {
        m_carriage = carriage;
        m_move_off_positive = move_off_positive;
        m_dt = dt;
    }

    enum HomingStates {
        UNINITIALIZED, MOVING_ON, MOVING_OFF, READY
    }

    ;

    private HomingStates m_state = HomingStates.UNINITIALIZED;

    public double update(double old_setpoint, double current_relative_position) {
        double fast_move_speed = 12.5;
        double slow_move_speed = 0.25;

        double new_setpoint = old_setpoint;
        double direction = m_move_off_positive ? 1.0 : -1.0;
        double slow_move_delta = m_dt * slow_move_speed * direction;
        double fast_move_delta = m_dt * fast_move_speed * direction;
        boolean on_sensor = m_carriage.getHomeSensorHovered();
        HomingStates next_state = m_state;
        boolean enabled = DriverStation.getInstance().isEnabled();

        m_null_controller = false;

        switch (m_state) {
            case UNINITIALIZED:
                if (!enabled) {
                    break;
                } else if (on_sensor) {
                    next_state = HomingStates.MOVING_OFF;
                } else {
                    next_state = HomingStates.MOVING_ON;
                }
                break;
            case MOVING_OFF:
                new_setpoint = current_relative_position;
                new_setpoint += slow_move_delta;
                if (!enabled) {
                    next_state = HomingStates.UNINITIALIZED;
                } else if (!on_sensor) {
                    m_zero_point = current_relative_position;
                    next_state = HomingStates.READY;
                    m_null_controller = true;
                }
                break;
            case MOVING_ON:
                new_setpoint -= fast_move_delta;
                if (!enabled) {
                    next_state = HomingStates.UNINITIALIZED;
                } else if (on_sensor) {
                    next_state = HomingStates.MOVING_OFF;
                    m_null_controller = true;
                }
                break;
            case READY:
                break;
            default:
                break;
        }
        m_state = next_state;
        return new_setpoint;
    }

    public boolean isReady() {
        return m_state == HomingStates.READY;
    }

    public double getZeroOffset() {
        return m_zero_point;
    }

    @Override
    public void reset() {
    }

    @Override
    public boolean isOnTarget() {
        return false;
    }

    public boolean needsControllerNullOut() {
        return m_null_controller;
    }

}
