package com.team254.frc2015.subsystems;

import com.team254.frc2015.Constants;
import com.team254.frc2015.subsystems.controllers.ElevatorSqueezeController;
import com.team254.lib.util.CheesySpeedController;
import com.team254.lib.util.StateHolder;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Solenoid;

public class TopCarriage extends ElevatorCarriage {
    Solenoid m_pivot;
    Solenoid m_grabber_open;
    Solenoid m_grabber_close;
    AnalogInput m_breakbeam;

    public TopCarriage(String name, CheesySpeedController motor,
                       Solenoid brake, Encoder encoder, DigitalInput home, Solenoid pivot,
                       Solenoid grabber_open, Solenoid grabber_close, AnalogInput breakbeam) {
        super(name, motor, brake, encoder, home, true);
        m_pivot = pivot;
        m_grabber_open = grabber_open;
        m_grabber_close = grabber_close;
        m_breakbeam = breakbeam;
    }

    public enum GrabberPositions {
        OPEN, CLOSED, VENTED, BOTH
    }

    @Override
    public void reloadConstants() {
        m_limits.m_min_position = Constants.kTopCarriageMinPositionInches;
        m_limits.m_max_position = Constants.kTopCarriageMaxPositionInches;
        m_limits.m_home_position = Constants.kTopCarriageHomePositionInches;
        m_limits.m_rezero_position = Constants.kTopCarriageReZeroPositionInches;
        super.reloadConstants();
    }

    public void setPivotDown(boolean down) {
        m_pivot.set(down);
    }

    public boolean getPivotDown() {
        return m_pivot.get();
    }

    public void setGrabberPosition(GrabberPositions pos) {
        m_grabber_open.set(pos == GrabberPositions.BOTH || pos == GrabberPositions.OPEN);
        m_grabber_close.set(pos == GrabberPositions.BOTH || pos == GrabberPositions.CLOSED);
    }

    public boolean getGrabberOpen() {
        return m_grabber_open.get();
    }

    public boolean hasSquezeEnabled() {
        return m_controller instanceof ElevatorSqueezeController;
    }

    public boolean getBreakbeamTriggered() {
        return m_breakbeam.getAverageVoltage() > Constants.kBreambeamVoltage;
    }

    @Override
    public void getState(StateHolder states) {
        super.getState(states);
        states.put("breakbeam_voltage", m_breakbeam.getAverageVoltage());
        states.put("breakbeam_state", getBreakbeamTriggered());
    }
}
