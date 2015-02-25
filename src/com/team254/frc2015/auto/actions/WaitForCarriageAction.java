package com.team254.frc2015.auto.actions;

import com.team254.frc2015.subsystems.ElevatorCarriage;

public class WaitForCarriageAction extends TimeoutAction {
    private ElevatorCarriage m_carriage;

    public WaitForCarriageAction(ElevatorCarriage carriage, double timeout) {
        super(timeout);
        m_carriage = carriage;
    }

    @Override
    public boolean isFinished() {
        return m_carriage.isOnTarget() || super.isFinished();
    }

}
