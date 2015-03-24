package com.team254.frc2015.auto.actions;

import com.team254.frc2015.subsystems.ElevatorCarriage;

/**
 * Created by tombot on 3/23/15.
 */
public class WaitForToteAction extends TimeoutAction {

    public WaitForToteAction(double timeout) {
        super(timeout);
    }

    @Override
    public boolean isFinished() {

        return intake.getBreakbeamTriggered() || super.isFinished();
    }

}


