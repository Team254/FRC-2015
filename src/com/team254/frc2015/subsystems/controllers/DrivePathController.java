package com.team254.frc2015.subsystems.controllers;

import com.team254.frc2015.Constants;
import com.team254.frc2015.subsystems.Drive;
import com.team254.lib.trajectory.LegacyTrajectoryFollower;
import com.team254.lib.trajectory.Path;
import com.team254.lib.trajectory.Trajectory;
import com.team254.lib.util.ChezyMath;
import com.team254.lib.util.DriveSignal;
import com.team254.lib.util.Pose;

/**
 * DrivePathController.java This controller drives the robot along a specified
 * trajectory.
 *
 * @author Tom Bottiglieri
 */
public class DrivePathController implements Drive.DriveController {

    public DrivePathController(Path path) {
        init();
        loadProfile(path.getLeftWheelTrajectory(), path.getRightWheelTrajectory(), 1.0, 0.0);
    }

    Trajectory trajectory;
    LegacyTrajectoryFollower followerLeft = new LegacyTrajectoryFollower("left");
    LegacyTrajectoryFollower followerRight = new LegacyTrajectoryFollower("right");
    double direction;
    double heading;
    double kTurn = -Constants.kDrivePathHeadingFollowKp;

    public boolean onTarget() {
        return followerLeft.isFinishedTrajectory();
    }

    private void init() {
        followerLeft.configure(Constants.kDrivePositionKp,
                Constants.kDrivePositionKi, Constants.kDrivePositionKd,
                Constants.kDrivePositionKv, Constants.kDrivePositionKa);
        followerRight.configure(Constants.kDrivePositionKp,
                Constants.kDrivePositionKi, Constants.kDrivePositionKd,
                Constants.kDrivePositionKv, Constants.kDrivePositionKa);
    }

    private void loadProfile(Trajectory leftProfile, Trajectory rightProfile,
                             double direction, double heading) {
        reset();
        followerLeft.setTrajectory(leftProfile);
        followerRight.setTrajectory(rightProfile);
        this.direction = direction;
        this.heading = heading;
    }

    public void loadProfileNoReset(Trajectory leftProfile,
                                   Trajectory rightProfile) {
        followerLeft.setTrajectory(leftProfile);
        followerRight.setTrajectory(rightProfile);
    }

    public void reset() {
        followerLeft.reset();
        followerRight.reset();
    }

    public int getFollowerCurrentSegmentNumber() {
        return followerLeft.getCurrentSegmentNumber();
    }

    public int getNumSegments() {
        return followerLeft.getNumSegments();
    }

    public void setTrajectory(Trajectory t) {
        this.trajectory = t;
    }

    public double getGoal() {
        return 0;
    }

    @Override
    public DriveSignal update(Pose pose) {
        if (onTarget()) {
            return new DriveSignal(0, 0);
        } else {
            double distanceL = direction * pose.getLeftDistance();
            double distanceR = direction * pose.getRightDistance();

            double speedLeft = direction * followerLeft.calculate(distanceL);
            double speedRight = direction * followerRight.calculate(distanceR);

            double goalHeading = followerLeft.getHeading();
            double observedHeading = -pose.getHeading();

            double angleDiffRads = ChezyMath.getDifferenceInAngleRadians(
                    observedHeading, goalHeading);
            double angleDiff = Math.toDegrees(angleDiffRads);

            double turn = kTurn * angleDiff;
            return new DriveSignal(speedLeft + turn, speedRight - turn);
        }
    }

    @Override
    public Pose getCurrentSetpoint() {
        return new Pose(followerLeft.getCurrentSegment().pos, 0, 0, 0, -followerLeft.getHeading(), 0);
    }
}
