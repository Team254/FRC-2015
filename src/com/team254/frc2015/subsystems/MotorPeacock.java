package com.team254.frc2015.subsystems;

import com.team254.frc2015.subsystems.controllers.TimedOpenLoopController;
import com.team254.lib.util.*;

public class MotorPeacock extends Subsystem implements Loopable {

    CheesySpeedController m_left_motor;
    CheesySpeedController m_right_motor;
    Controller m_controller = null;
    public static final boolean s_using_peacock = true;

    public MotorPeacock(CheesySpeedController left_motor, CheesySpeedController right_motor) {
        super("MotorPeacock");
        m_left_motor = left_motor;
        m_right_motor = right_motor;
    }

    public void setUnsafeLeftRightPower(double left, double right) {
        // left positive down
        // right negative down
        // input positive down
        if (s_using_peacock) {
            m_left_motor.set(left);
            m_right_motor.set(right);
        } else {
            m_left_motor.set(0);
            m_right_motor.set(0);
        }
    }

    public synchronized void setPowerTimeSetpoint(double start_power, double time_start_power, double end_power, double time_to_decel) {
        setUnsafeLeftRightPower(start_power, start_power); // power immediately!
        m_controller = new TimedOpenLoopController(start_power, time_start_power, end_power, time_to_decel);
    }

    public synchronized void disableControlLoop() {
        m_controller = null;
        setUnsafeLeftRightPower(0, 0);
    }

    public void setOpenLoop(double left, double right) {
        m_controller = null;
        setUnsafeLeftRightPower(left, right);
    }

    @Override
    public void reloadConstants() {

    }

    @Override
    public void getState(StateHolder states) {
        states.put("left_pwm", m_left_motor.get());
    }

    @Override
    public synchronized void update() {
        if (m_controller instanceof TimedOpenLoopController) {
            TimedOpenLoopController c = (TimedOpenLoopController) m_controller;
            double power = c.update();
            setUnsafeLeftRightPower(power, power);
        }
    }
}
