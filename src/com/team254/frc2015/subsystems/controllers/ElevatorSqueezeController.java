package com.team254.frc2015.subsystems.controllers;

import com.team254.frc2015.Constants;
import com.team254.frc2015.HardwareAdaptor;
import com.team254.frc2015.subsystems.TopCarriage;
import com.team254.lib.util.CheesySpeedController;
import com.team254.lib.util.Controller;

public class ElevatorSqueezeController extends Controller {

    TopCarriage m_top_carriage = HardwareAdaptor.kTopCarriage;
    CheesySpeedController m_bottom_carriage_motor = HardwareAdaptor.kBottomCarriageMotor;

    public ElevatorSqueezeController() {
    }

    @Override
    public void reset() {
    }

    public double update() {
        double height = m_top_carriage.getHeight();
        double min_output = -1.0;
        if (height < Constants.kTopCarriageSafePositionInches - 1.0) {
            min_output = 1.0;
        } else if (height < Constants.kTopCarriageSafePositionInches + 1.0) {
            min_output = 0.0;
        }
        double command = 0.0;

        if (m_bottom_carriage_motor.get() > 0) {
            // Bottom carriage is rising, get out of the way!
            if (m_top_carriage.getBreakbeamTriggered()) {
                command = 1.0;
            } else {
                command = 0.0;
            }
        } else {
            // Bottom carriage is stationary or lowering
            if (m_top_carriage.getBreakbeamTriggered()) {
                command = 0.0;
            } else {
                command = -1.0;
            }
        }
        return Math.max(min_output, command);
    }

    @Override
    public boolean isOnTarget() {
        // This controller is a best effort controller.
        return false;
    }

}
