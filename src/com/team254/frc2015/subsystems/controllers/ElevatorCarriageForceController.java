package com.team254.frc2015.subsystems.controllers;

import com.team254.frc2015.Constants;
import com.team254.frc2015.HardwareAdaptor;
import com.team254.frc2015.subsystems.BottomCarriage;
import com.team254.frc2015.subsystems.TopCarriage;
import com.team254.lib.util.CheesySpeedController;
import com.team254.lib.util.Controller;
import com.team254.lib.util.SynchronousPID;

public class ElevatorCarriageForceController extends Controller {
    
    CheesySpeedController m_top_carriage = HardwareAdaptor.kTopCarriageMotor;
    CheesySpeedController m_bottom_carriage = HardwareAdaptor.kBottomCarriageMotor;
    
    SynchronousPID m_pid;
    
    public ElevatorCarriageForceController() {
        m_pid = new SynchronousPID(Constants.kElevatorCarriageForceControllerKp,
                Constants.kElevatorCarriageForceControllerKi,
                Constants.kElevatorCarriageForceControllerKd);
    }
    
    public void setGoal(double current_sum) {
        m_pid.setSetpoint(current_sum);
    }

    @Override
    public void reset() {
        m_pid.reset();
    }
    
    public double update() {
        double total_current = -Math.signum(m_top_carriage.get()) * m_top_carriage.getCurrent() +
                Math.signum(m_bottom_carriage.get()) * m_bottom_carriage.getCurrent();
        return -m_pid.calculate(total_current);
    }

    @Override
    public boolean isOnTarget() {
        // This controller is a best effort controller.
        return false;
    }

}
