package com.team254.frc2015.subsystems.controllers;

import com.team254.frc2015.subsystems.Drive;
import com.team254.lib.trajectory.TrajectoryFollower;
import com.team254.lib.util.DriveSignal;
import com.team254.lib.util.Pose.RelativePoseGenerator;
import com.team254.lib.util.SynchronousPID;
import com.team254.lib.trajectory.TrajectoryFollower.TrajectoryConfig;
import com.team254.lib.util.Pose;

import static com.team254.lib.trajectory.TrajectoryFollower.TrajectorySetpoint;

public class DriveStraightController implements Drive.DriveController {

    private TrajectoryFollowingPositionController mDistanceController;
    private SynchronousPID mTurnPid;
    private Pose mSetpointRelativePose;

	public DriveStraightController(
            double kpDist, double kiDist, double kdDist, double kvDist, double kaDist,
            double kpTurn, double kiTurn, double kdTurn,
			double on_target_delta, TrajectoryConfig config) {
        mDistanceController = new TrajectoryFollowingPositionController(
                kpDist, kiDist, kdDist, kvDist, kaDist, on_target_delta, config);
		this.mTurnPid = new SynchronousPID();
		mTurnPid.setPID(kpTurn, kiTurn, kdTurn);
	}

    @Override
	public DriveSignal update(Pose currentPose) {
        mDistanceController.update(
                (currentPose.getLeftDistance() + currentPose.getRightDistance()) / 2.0,
                (currentPose.getLeftVelocity() + currentPose.getRightVelocity()) / 2.0);
		double throttle = mDistanceController.get();
		double turn = mTurnPid.calculate(currentPose.getHeading());

		return new DriveSignal(throttle + turn, throttle - turn);
	}

    @Override
    public Pose getCurrentSetpoint() {
        TrajectorySetpoint trajectorySetpoint = mDistanceController.getSetpoint();
        double dist = trajectorySetpoint.pos;
        double velocity = trajectorySetpoint.vel;
        return new Pose(
                mSetpointRelativePose.getLeftDistance() + dist,
                mSetpointRelativePose.getRightDistance() + dist,
                mSetpointRelativePose.getLeftVelocity() + velocity,
                mSetpointRelativePose.getRightVelocity() + velocity,
                0,
                0);
    }

    public void setGoal(Pose priorSetpoint, double goalSetpoint) {
        TrajectorySetpoint initialSetpoint = new TrajectorySetpoint();
        initialSetpoint.pos = encoderDistance(priorSetpoint);
        initialSetpoint.vel = encoderVelocity(priorSetpoint);
        initialSetpoint.acc = 0;
        mDistanceController.setGoal(initialSetpoint, goalSetpoint);
        mTurnPid.setSetpoint(priorSetpoint.getHeading());
        mSetpointRelativePose = new Pose(
                priorSetpoint.getLeftDistance(),
                priorSetpoint.getRightDistance(),
                0,
                0,
                priorSetpoint.getHeading(),
                priorSetpoint.getHeadingVelocity());
    }

    public static double encoderVelocity(Pose pose) {
        return (pose.getLeftVelocity() + pose.getRightVelocity()) / 2.0;
    }

    public static double encoderDistance(Pose pose) {
        return (pose.getLeftDistance() + pose.getRightDistance()) / 2.0;
    }
}
