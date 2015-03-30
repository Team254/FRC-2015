package com.team254.frc2015.auto.actions;

/**
 * Created by tombot on 3/24/15.
 */
public class WaitForTopCarriageSensorAction extends TimeoutAction {
    public WaitForTopCarriageSensorAction(double timeout) {
        super(timeout);
    }

    @Override
    public boolean isFinished() {

        return top_carriage.getBreakbeamTriggered() || super.isFinished();
    }

}


