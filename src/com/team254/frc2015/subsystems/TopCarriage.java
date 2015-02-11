package com.team254.frc2015.subsystems;

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
        super(name, ElevatorCarriage.Position.TOP, motor, brake, encoder, home);
        m_pivot = pivot;
        m_grabber = grabber;
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
