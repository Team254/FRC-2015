package com.team254.frc2015.auto.actions;

import com.team254.frc2015.subsystems.ElevatorCarriage;

public class WaitForCarriageHeightAction extends TimeoutAction {
    private ElevatorCarriage m_carriage;
    private double m_height;
    private boolean m_greater_than = true;

    public WaitForCarriageHeightAction(ElevatorCarriage carriage, double height, boolean greater_than, double timeout) {
        super(timeout);
        m_height = height;
        m_carriage = carriage;
        m_greater_than = greater_than;
    }

    @Override
    public boolean isFinished() {
        return (m_greater_than ? m_carriage.getHeight() >= m_height : m_carriage.getHeight() >= m_height)  || super.isFinished();
    }

}
