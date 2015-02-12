package com.team254.frc2015.subsystems.controllers;

import com.team254.lib.trajectory.TrajectoryFollower.TrajectoryConfig;
import com.team254.lib.util.DriveSignal;
import com.team254.lib.util.Pose;
import com.team254.lib.util.Pose.RelativePoseGenerator;

public class DriveTurnController extends TrajectoryFollowingPositionController {

	private RelativePoseGenerator m_relative_pose_generator;

	public DriveTurnController(double kp, double ki, double kd, double kv,
			double ka, double on_target_delta, TrajectoryConfig config) {
		super(kp, ki, kd, kv, ka, on_target_delta, config);
	}

	public void setStartPose(Pose pose) {
		m_relative_pose_generator = pose.new RelativePoseGenerator();
	}

	public DriveSignal update(Pose currentPose) {
		if (m_relative_pose_generator == null) {
			m_relative_pose_generator = currentPose.new RelativePoseGenerator();
		}
		Pose relative_pose = m_relative_pose_generator.get(currentPose);
		double turn = update(relative_pose.getHeading(),
				relative_pose.getHeadingVelocity());
		return new DriveSignal(turn, -turn);
	}

}
