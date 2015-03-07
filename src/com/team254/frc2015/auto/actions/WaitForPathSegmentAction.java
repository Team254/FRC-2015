package com.team254.frc2015.auto.actions;

import com.team254.frc2015.subsystems.Drive;
import com.team254.frc2015.subsystems.controllers.DrivePathController;

public class WaitForPathSegmentAction extends TimeoutAction {
    int m_i = 0;

    public WaitForPathSegmentAction(int i, double timeout) {
        super(timeout);
        m_i = i;
    }

    @Override
    public boolean isFinished() {
        Drive.DriveController c = drive.getController();
        boolean done = false;
        if (c instanceof DrivePathController) {
            DrivePathController dpc = (DrivePathController) c;
            int seg = dpc.getFollowerCurrentSegmentNumber();
            done = seg >= m_i;
        }
        return done || super.isFinished();
    }

}
