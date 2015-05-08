package com.team254.frc2015.auto.actions;

import com.team254.frc2015.HardwareAdaptor;

public class WaitForGyroDataAction extends TimeoutAction {
    public WaitForGyroDataAction(double timeout) {
        super(timeout);
    }

    @Override
    public boolean isFinished() {
        return HardwareAdaptor.kGyroThread.hasData() || super.isFinished();
    }
}
