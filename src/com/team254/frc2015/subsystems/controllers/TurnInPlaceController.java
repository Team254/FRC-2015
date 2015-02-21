package com.team254.frc2015.subsystems.controllers;

import com.team254.frc2015.Constants;
import com.team254.frc2015.subsystems.Drive;
import com.team254.lib.trajectory.TrajectoryFollower;
import com.team254.lib.util.ConstantsBase;
import com.team254.lib.util.DriveSignal;
import com.team254.lib.util.Pose;

import static com.team254.lib.trajectory.TrajectoryFollower.TrajectoryConfig;
import static com.team254.lib.trajectory.TrajectoryFollower.TrajectorySetpoint;

/**
 * Controls the robot to turn in place
 */
public class TurnInPlaceController implements Drive.DriveController {

    private final TrajectoryFollowingPositionController mLeftController;
    private final TrajectoryFollowingPositionController mRightController;

    private final Pose mInitialSetpointPose;

    public TurnInPlaceController(Pose initialSetpointPose, double destHeading, double maxTurnVelocity) {
        mInitialSetpointPose = initialSetpointPose;

        TrajectoryConfig config = new TrajectoryConfig();
        config.dt = Constants.kControlLoopsDt;
        config.max_acc = Constants.kTurnMaxAccelRadsPerSec2;
        config.max_vel = roatationToWheelTravel(maxTurnVelocity);

        double angularTravelDistance = normalizedSubtractAngles(destHeading, mInitialSetpointPose.getHeading());
        double encoderTravelDistance = roatationToWheelTravel(angularTravelDistance);
        System.out.println("encoder: " + encoderTravelDistance);

        mLeftController = makeController(
                config,
                initialSetpointPose.getLeftDistance(),
                initialSetpointPose.getLeftVelocity(),
                encoderTravelDistance);
        mRightController = makeController(
                config,
                initialSetpointPose.getRightDistance(),
                initialSetpointPose.getRightVelocity(),
                -encoderTravelDistance);
    }

    @Override
    public DriveSignal update(Pose pose) {
        mLeftController.update(pose.getLeftDistance(), pose.getLeftVelocity());
        mRightController.update(pose.getRightDistance(), pose.getRightVelocity());
        return new DriveSignal(mLeftController.get(), mRightController.get());
    }

    @Override
    public Pose getCurrentSetpoint() {
        TrajectorySetpoint leftSetpoint = mLeftController.getSetpoint();
        TrajectorySetpoint rightSetpoint = mRightController.getSetpoint();

        double leftTravel = leftSetpoint.pos - mInitialSetpointPose.getLeftDistance();
        double rightTravel = rightSetpoint.pos - mInitialSetpointPose.getRightDistance();

        return new Pose(
                leftSetpoint.pos,
                rightSetpoint.pos,
                leftSetpoint.vel,
                rightSetpoint.vel,
                wheelTravelToRotation(leftTravel - rightTravel),
                wheelTravelToRotation(leftSetpoint.vel - rightSetpoint.vel));
    }

    /** @return the angular distance between 'from' and 'to' between -PI and PI */
    private static double normalizedSubtractAngles(double to, double from) {
        double modDistance = (to - from) % (2 * Math.PI);
        if (modDistance > Math.PI) {
            return modDistance - 2 * Math.PI;
        }
        // FIXME: Not sure if java's mod implementation produces negative output for negative numerators
        if (modDistance < - Math.PI) {
            return modDistance + 2 * Math.PI;
        }
        return modDistance;
    }

    private static double roatationToWheelTravel(double radians) {
        return radians * Constants.kWheelbaseWidth * Constants.kTurnSlipFactor;
    }

    private static double wheelTravelToRotation(double deltaInches) {
        return deltaInches / (Constants.kWheelbaseWidth * Constants.kTurnSlipFactor);
    }

    private static TrajectoryFollowingPositionController makeController(
            TrajectoryConfig config,
            double initialDist,
            double initialVelocity,
            double travelDistance) {
        TrajectoryFollowingPositionController controller = new TrajectoryFollowingPositionController(
                Constants.kTurnKp,
                Constants.kTurnKi,
                Constants.kTurnKd,
                Constants.kTurnKv,
                Constants.kTurnKa,
                Constants.kTurnOnTargetError,
                config);
        TrajectorySetpoint initialSetpoint = new TrajectorySetpoint();
        initialSetpoint.pos = initialDist;
        initialSetpoint.vel = initialVelocity;
        controller.setGoal(initialSetpoint, initialDist + travelDistance);
        return controller;
    }


}
