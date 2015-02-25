package com.team254.frc2015.subsystems.controllers;

import com.team254.frc2015.HardwareAdaptor;
import com.team254.frc2015.subsystems.BottomCarriage;
import com.team254.frc2015.subsystems.ElevatorCarriage;
import com.team254.lib.util.CheesySpeedController;
import com.team254.lib.util.Controller;

public class ElevatorCarriageForceController extends Controller {

    CheesySpeedController m_top_carriage = HardwareAdaptor.kTopCarriageMotor;
    CheesySpeedController m_bottom_carriage = HardwareAdaptor.kBottomCarriageMotor;

    double m_squeeze_power = 0;
    double m_bottom_highest_height = 0;
    boolean m_follow_bottom = true;
    boolean m_in_contact;

    public ElevatorCarriageForceController(ElevatorCarriage follower) {
        if (follower instanceof BottomCarriage) {
            m_follow_bottom = false;
        } else {
            m_follow_bottom = true;
        }
    }

    public void setSqueezePower(double squeeze_power) {
        m_squeeze_power = squeeze_power;
        reset();
    }

    @Override
    public void reset() {
        m_in_contact = false;
    }

    public double update() {
        // TODO(jared): Clean this up.
        if (m_follow_bottom) {
            // First move until contact.
            double speed = 0;
            if (HardwareAdaptor.kBottomCarriage.getSetpoint().vel != 0 || HardwareAdaptor.kBottomCarriage.getSetpoint().acc != 0) {
                //speed = m_bottom_carriage.get() - m_squeeze_power;
                speed = m_squeeze_power;
                m_bottom_highest_height = 0;
                m_in_contact = false;
            } else {
                if (m_in_contact) {
                    return 0;
                } else {
                    speed = -1.0;
                    if (HardwareAdaptor.kBottomCarriage.getHeight() < m_bottom_highest_height - .125) {
                        System.out.println("touch!");
                        m_in_contact = true;
                    }
                }
            }
            m_bottom_highest_height = Math.max(m_bottom_highest_height, HardwareAdaptor.kBottomCarriage.getHeight());

            if (HardwareAdaptor.kTopCarriage.getHeight() < 28.0) {
                m_in_contact = false;
                return 0.5;
            } else if (HardwareAdaptor.kTopCarriage.getHeight() < 30.0) {
                m_in_contact = false;
                return Math.max(0.0, speed);
            } else {
                return speed;
            }
        } else {
            return 0;
        }
    }

    @Override
    public boolean isOnTarget() {
        // This controller is a best effort controller.
        return false;
    }

}
