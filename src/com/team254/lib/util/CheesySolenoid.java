package com.team254.lib.util;

import com.team254.frc2015.Robot;
import edu.wpi.first.wpilibj.Solenoid;

public class CheesySolenoid extends Solenoid {
    private boolean m_on = false;
    private boolean m_was_disabled = true;

    public CheesySolenoid(int channel) {
        super((channel > 7 ? 1 : 0), (channel > 7 ? channel - 8 : channel));
    }

    @Override
    public void set(boolean on) {
        boolean is_disabled = Robot.getState() == Robot.RobotState.DISABLED;
        if ((!is_disabled && m_was_disabled) || on != m_on) {
            super.set(on);
        }
        m_on = on;
        m_was_disabled = is_disabled;
    }

    @Override
    public boolean get() {
        return m_on;
    }
}
