package com.team254.frc2015.auto.actions;

public class WaitForTopCarriageSensorAction extends TimeoutAction {
    public WaitForTopCarriageSensorAction(double timeout) {
        super(timeout);
    }

    @Override
    public boolean isFinished() {

        return top_carriage.getBreakbeamTriggered() || super.isFinished();
    }

}


