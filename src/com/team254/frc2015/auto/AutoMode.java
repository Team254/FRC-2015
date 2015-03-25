package com.team254.frc2015.auto;

import com.team254.frc2015.HardwareAdaptor;
import com.team254.frc2015.auto.actions.*;
import com.team254.frc2015.subsystems.*;
import edu.wpi.first.wpilibj.PowerDistributionPanel;

public abstract class AutoMode extends AutoModeBase {

    protected Drive drive = HardwareAdaptor.kDrive;
    protected TopCarriage top_carriage = HardwareAdaptor.kTopCarriage;
    protected BottomCarriage bottom_carriage = HardwareAdaptor.kBottomCarriage;
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

    public void waitForCarriageHeight(ElevatorCarriage carriage, double height, boolean greater_than, double timeout) throws AutoModeEndedException {
        runAction(new WaitForCarriageHeightAction(carriage, height, greater_than, timeout));
    }

    public void waitForTote(double timeout) throws AutoModeEndedException {
        runAction(new WaitForToteAction(timeout));
    }

    public void waitForTurnAngle(double angle, boolean positive, double timeout) throws AutoModeEndedException {
        runAction(new WaitForTurnPastAngleAction(angle, positive, timeout));
    }

    public void waitForTopCarriageSensor(double timeout) throws AutoModeEndedException {
        runAction(new WaitForTopCarriageSensorAction(timeout));
    }

    public void waitForDriveDistance(double distance, boolean positive, double timeout) throws AutoModeEndedException {
        runAction(new WaitForDriveDistanceAction(distance, positive, timeout));
    }

    public void waitForPathSegment(int segNum, double timeout) throws AutoModeEndedException {
        runAction(new WaitForPathSegmentAction(segNum, timeout));
    }
}
