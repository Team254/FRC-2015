package com.team254.frc2015.subsystems;

import com.team254.frc2015.Constants;
import com.team254.frc2015.subsystems.controllers.ElevatorHomingController;
import com.team254.lib.util.CheesySpeedController;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Solenoid;

public class TopCarriage extends ElevatorCarriage {
    Solenoid m_pivot;
    Solenoid m_grabber;

    public TopCarriage(String name, CheesySpeedController motor,
            Solenoid brake, Encoder encoder, DigitalInput home, Solenoid pivot,
            Solenoid grabber) {
        super(name, motor, brake, encoder, home);
        m_pivot = pivot;
        m_grabber = grabber;

        m_homing_controller = new ElevatorHomingController(this, false,
                Constants.kControlLoopsDt);
    }

    @Override
    public void reloadConstants() {
        m_limits.m_min_position = Constants.kTopCarriageMinPositionInches;
        m_limits.m_max_position = Constants.kTopCarriageMaxPositionInches;
        m_limits.m_home_position = Constants.kTopCarriageHomePositionInches;
        super.reloadConstants();
    }

    public void setPivotDown(boolean down) {
        m_pivot.set(down);
    }

    public boolean getPivotDown() {
        return m_pivot.get();
    }

    public void setGrabberOpen(boolean open) {
        m_grabber.set(open);
    }

    public boolean getGrabberOpen() {
        return m_grabber.get();
    }

}
