package com.team254.frc2015.subsystems;

import com.team254.lib.util.CheesySolenoid;
import com.team254.lib.util.StateHolder;
import com.team254.lib.util.Subsystem;

/**
 * Created by tombot on 4/1/15.
 */
public class Peacock extends Subsystem {

    CheesySolenoid m_solenoid;

    public Peacock(CheesySolenoid solenoid) {
        super("Peacock");
        m_solenoid = solenoid;
    }

    public void setDown(boolean down) {
        m_solenoid.set(down);
    }

    @Override
    public void reloadConstants() {
    }

    @Override
    public void getState(StateHolder states) {
    }
}
