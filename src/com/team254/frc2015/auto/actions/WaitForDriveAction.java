package com.team254.frc2015.auto.actions;

public class WaitForDriveAction extends TimeoutAction {
    public WaitForDriveAction(double timeout) {
        super(timeout);
    }

    @Override
    public boolean isFinished() {
        return drive.controllerOnTarget() || super.isFinished();
    }

}
