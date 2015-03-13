package com.team254.frc2015.subsystems;

import com.team254.frc2015.Constants;
import com.team254.lib.util.CheesySpeedController;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Solenoid;

public class BottomCarriage extends ElevatorCarriage {

    Solenoid m_pusher;
    Solenoid m_flapper;

    public BottomCarriage(String name, CheesySpeedController motor,
            Solenoid brake, Encoder encoder, DigitalInput home,
            Solenoid pusher, Solenoid flapper) {
        super(name, motor, brake, encoder, home);
        m_pusher = pusher;
        m_flapper = flapper;
    }

    @Override
    public void reloadConstants() {
        m_limits.m_min_position = Constants.kBottomCarriageMinPositionInches;
        m_limits.m_max_position = Constants.kBottomCarriageMaxPositionInches;
        m_limits.m_home_position = Constants.kBottomCarriageHomePositionInches;
        m_limits.m_rezero_position = Constants.kBottomCarriageReZeroPositionInches;
        super.reloadConstants();
    }

    public void setPusherExtended(boolean extended) {
        m_pusher.set(extended);
    }

    public boolean getPusherExtended() {
        return m_pusher.get();
    }

    public void setFlapperOpen(boolean open) {
        m_flapper.set(!open);
    }

    public boolean getFlapperOpen() {
        return m_flapper.get();
    }

}
