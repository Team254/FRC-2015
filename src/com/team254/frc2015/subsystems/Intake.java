package com.team254.frc2015.subsystems;

import com.team254.lib.util.CheesySolenoid;
import com.team254.lib.util.CheesySpeedController;
import com.team254.lib.util.StateHolder;
import com.team254.lib.util.Subsystem;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.Solenoid;

public class Intake extends Subsystem {

    CheesySolenoid m_solenoid;
    CheesySolenoid m_coop_solenoid;
    CheesySolenoid m_pinball_wizard_solenoid;
    AnalogInput m_breakbeam;
    CheesySpeedController m_left_motor;
    CheesySpeedController m_right_motor;

    public Intake(String name, CheesySolenoid solenoid, CheesySolenoid coop_solenoid, CheesySolenoid pinball_wizard_solenoid,
                  CheesySpeedController left_motor, CheesySpeedController right_motor, AnalogInput breakbeam) {
        super(name);
        m_solenoid = solenoid;
        m_left_motor = left_motor;
        m_right_motor = right_motor;
        m_breakbeam = breakbeam;
        m_coop_solenoid = coop_solenoid;
        m_pinball_wizard_solenoid = pinball_wizard_solenoid;
    }

    public void open() {
        m_solenoid.set(false);  // open default
    }

    public void close() {
        m_solenoid.set(true);  // open default
    }

    public void setSpeed(double speed) {
        setLeftRight(speed, speed);
    }

    public void setLeftRight(double left_speed, double right_speed) {
        m_left_motor.set(-left_speed);
        m_right_motor.set(-right_speed);
    }

    public void setPinballWizardOut(boolean out) {
        m_pinball_wizard_solenoid.set(out);
    }

    public void setCoopPusherOut(boolean out) {
        m_coop_solenoid.set(out);
    }

    public boolean getBreakbeamTriggered() {
        return m_breakbeam.getAverageVoltage() > 1.65;
    }

    @Override
    public void reloadConstants() {
        // TODO Auto-generated method stub

    }
    @Override
    public void getState(StateHolder states) {
        states.put("breakbeam_voltage", m_breakbeam.getAverageVoltage());
        states.put("breakbeam_state", getBreakbeamTriggered());
    }
}
