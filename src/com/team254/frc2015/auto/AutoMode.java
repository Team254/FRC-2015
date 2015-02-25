package com.team254.frc2015.auto;

import com.team254.frc2015.HardwareAdaptor;
import com.team254.frc2015.auto.actions.TimeoutAction;
import com.team254.frc2015.auto.actions.WaitForCarriageAction;
import com.team254.frc2015.auto.actions.WaitForDriveAction;
import com.team254.frc2015.auto.actions.WaitForPathSegmentAction;
import com.team254.frc2015.subsystems.Drive;
import com.team254.frc2015.subsystems.ElevatorCarriage;
import com.team254.frc2015.subsystems.Intake;
import edu.wpi.first.wpilibj.PowerDistributionPanel;

public abstract class AutoMode extends AutoModeBase {

    protected Drive drive = HardwareAdaptor.kDrive;
    protected ElevatorCarriage top_carriage = HardwareAdaptor.kTopCarriage;
    protected ElevatorCarriage bottom_carriage = HardwareAdaptor.kBottomCarriage;
    protected PowerDistributionPanel pdp = HardwareAdaptor.kPDP;
    protected Intake intake = HardwareAdaptor.kIntake;

    public void waitTime(double seconds) throws AutoModeEndedException {
        runAction(new TimeoutAction(seconds));
    }

    public void waitForDrive(double timeout) throws AutoModeEndedException {
        runAction(new WaitForDriveAction(timeout));
    }

    public void waitForCarriage(ElevatorCarriage carriage, double timeout) throws AutoModeEndedException {
        runAction(new WaitForCarriageAction(carriage, timeout));
    }

    public void waitForPathSegment(int segNum, double timeout) throws AutoModeEndedException {
        runAction(new WaitForPathSegmentAction(segNum, timeout));
    }
}
