package com.team254.frc2015;

import java.util.Optional;

import com.team254.frc2015.subsystems.ElevatorCarriage;
import com.team254.lib.trajectory.Trajectory;
import com.team254.lib.trajectory.TrajectoryGenerator;

public class ElevatorTrajectoryGenerator {
	ElevatorCarriage kTopCarriage = HardwareAdaptor.kTopCarriage;
	ElevatorCarriage kBottomCarriage = HardwareAdaptor.kBottomCarriage;

	public static final int kBottomCarriageIndex = 0;
	public static final int kTopCarriageIndex = 1;

	public Trajectory[] generateSafeTrajectories(
			Optional<Double> bottom_setpoint, Optional<Double> top_setpoint) {
		// First sanity check the setpoints to ensure they are within limits and
		// the bottom is not above the top.
		double adjusted_bottom_setpoint = -1;
		double adjusted_top_setpoint = -1;
		if (top_setpoint.isPresent()) {
			adjusted_top_setpoint = top_setpoint.get();
			if (adjusted_top_setpoint < Constants.kTopCarriageMinPositionInches) {
				adjusted_top_setpoint = Constants.kTopCarriageMinPositionInches;
			} else if (adjusted_top_setpoint > Constants.kTopCarriageMaxPositionInches) {
				adjusted_top_setpoint = Constants.kTopCarriageMaxPositionInches;
			}
		}
		if (bottom_setpoint.isPresent()) {
			adjusted_bottom_setpoint = bottom_setpoint.get();
			if (adjusted_bottom_setpoint < Constants.kBottomCarriageMinPositionInches) {
				adjusted_bottom_setpoint = Constants.kBottomCarriageMinPositionInches;
			} else if (adjusted_bottom_setpoint > Constants.kBottomCarriageMaxPositionInches) {
				adjusted_bottom_setpoint = Constants.kBottomCarriageMaxPositionInches;
			}
			if (top_setpoint.isPresent()
					&& adjusted_top_setpoint < Constants.kBottomCarriageHeight
							+ adjusted_bottom_setpoint) {
				adjusted_bottom_setpoint = adjusted_top_setpoint
						- Constants.kBottomCarriageHeight;
			}
		}

		// Generate the trajectories.
		Trajectory[] result = new Trajectory[2];
		if (bottom_setpoint.isPresent()) {
			TrajectoryGenerator.Config config = new TrajectoryGenerator.Config();
			config.dt = Constants.kDt;
			config.max_vel = Constants.kBottomCarriageMaxSpeedInchesPerSec;
			config.max_acc = Constants.kBottomCarriageMaxAccelInchesPerSec2;
			double[] current_command = kBottomCarriage
					.getCommandedPositionAndVelocity();
			result[kBottomCarriageIndex] = TrajectoryGenerator.generate(config,
					current_command[0], current_command[1],
					adjusted_bottom_setpoint, 0.0);
		}
		if (top_setpoint.isPresent()) {
			TrajectoryGenerator.Config config = new TrajectoryGenerator.Config();
			config.dt = Constants.kDt;
			config.max_vel = Constants.kTopCarriageMaxSpeedInchesPerSec;
			config.max_acc = Constants.kTopCarriageMaxAccelInchesPerSec2;
			double[] current_command = kTopCarriage
					.getCommandedPositionAndVelocity();
			result[kTopCarriageIndex] = TrajectoryGenerator.generate(config,
					current_command[0], current_command[1],
					adjusted_bottom_setpoint, 0.0);
		}
		return result;
	}
}
