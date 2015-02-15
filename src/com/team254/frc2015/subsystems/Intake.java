package com.team254.frc2015.subsystems;

import com.team254.lib.util.CheesySpeedController;
import com.team254.lib.util.StateHolder;
import com.team254.lib.util.Subsystem;

import edu.wpi.first.wpilibj.Solenoid;

public class Intake extends Subsystem {

    Solenoid m_left_solenoid;
    Solenoid m_right_solenoid;
    CheesySpeedController m_left_motor;
    CheesySpeedController m_right_motor;

    public Intake(String name, Solenoid left_solenoid, Solenoid right_solenoid,
            CheesySpeedController left_motor, CheesySpeedController right_motor) {
        super(name);
        m_left_solenoid = left_solenoid;
        m_right_solenoid = right_solenoid;
        m_left_motor = left_motor;
        m_right_motor = right_motor;
    }

    public void open() {
        m_left_solenoid.set(true);
        m_right_solenoid.set(true);
    }

    public void close() {
        m_left_solenoid.set(false);
        m_right_solenoid.set(false);
    }

    public void setSpeed(double speed) {
        setLeftRight(speed, speed);
    }

    public void setLeftRight(double left_speed, double right_speed) {
        m_left_motor.set(left_speed);
        m_right_motor.set(right_speed);
    }

    @Override
    public void getState(StateHolder states) {
        // TODO Auto-generated method stub

    }

}
