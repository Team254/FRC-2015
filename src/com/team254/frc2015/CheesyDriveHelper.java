package com.team254.frc2015;

import com.team254.frc2015.subsystems.Drive;
import com.team254.lib.util.DriveSignal;
import com.team254.lib.util.Util;

import edu.wpi.first.wpilibj.DriverStation;

/**
 * CheesyDriveHelper implements the calculations used in CheesyDrive, sending
 * power to the motors.
 */
public class CheesyDriveHelper {

	private Drive drive;
	double m_old_turn, m_quick_stop_accumulator;
	private static final double kthrottleDeadband = 0.02;
	private static final double kWheelDeadband = 0.02;
	private static final double kWheelNonLinearity = 0.6;
	private DriveSignal signal = new DriveSignal(0, 0);
	double m_neg_inertia_accumulator = 0.0;

	public CheesyDriveHelper(Drive drive) {
		this.drive = drive;
	}

	public void cheesyDrive(double throttle_in, double turn_in, boolean isQuickTurn) {
		if (DriverStation.getInstance().isAutonomous()) {
			return;
		}

		double left_pwm, right_pwm, over_power, angular_power;
		double sensitivity = Constants.kDriveSensitivity;
		double turn = handleDeadband(turn_in, kWheelDeadband);
		double throttle = handleDeadband(throttle_in, kthrottleDeadband);

		double neg_inertia = turn - m_old_turn;
		m_old_turn = turn;

		turn = Math.sin(Math.PI / 2.0 * kWheelNonLinearity * turn) / Math.sin(Math.PI / 2.0 * kWheelNonLinearity);
		turn = Math.sin(Math.PI / 2.0 * kWheelNonLinearity * turn) / Math.sin(Math.PI / 2.0 * kWheelNonLinearity);

		double neg_inertia_power = neg_inertia * Constants.kNegativeInertiaScalar;
		m_neg_inertia_accumulator += neg_inertia_power;

		turn = turn + m_neg_inertia_accumulator;
		if (m_neg_inertia_accumulator > 1) {
			m_neg_inertia_accumulator -= 1;
		} else if (m_neg_inertia_accumulator < -1) {
			m_neg_inertia_accumulator += 1;
		} else {
			m_neg_inertia_accumulator = 0;
		}

		if (isQuickTurn) {
			if (Math.abs(throttle) < 0.2) {
				double alpha = 0.1;
				m_quick_stop_accumulator = (1 - alpha) * m_quick_stop_accumulator + alpha * Util.limit(turn, 1.0) * 5;
			}
			over_power = 1.0;
			sensitivity = 1.0;
			angular_power = turn;
		} else {
			over_power = 0.0;
			angular_power = Math.abs(throttle) * turn * sensitivity
					- m_quick_stop_accumulator;
			if (m_quick_stop_accumulator > 1) {
				m_quick_stop_accumulator -= 1;
			} else if (m_quick_stop_accumulator < -1) {
				m_quick_stop_accumulator += 1;
			} else {
				m_quick_stop_accumulator = 0.0;
			}
		}

		right_pwm = left_pwm = throttle;
		left_pwm += angular_power;
		right_pwm -= angular_power;
		if (left_pwm > 1.0) {
			right_pwm -= over_power * (left_pwm - 1.0);
			left_pwm = 1.0;
		} else if (right_pwm > 1.0) {
			left_pwm -= over_power * (right_pwm - 1.0);
			right_pwm = 1.0;
		} else if (left_pwm < -1.0) {
			right_pwm += over_power * (-1.0 - left_pwm);
			left_pwm = -1.0;
		} else if (right_pwm < -1.0) {
			left_pwm += over_power * (-1.0 - right_pwm);
			right_pwm = -1.0;
		}
		signal.leftMotor = left_pwm;
		signal.rightMotor = right_pwm;
		drive.setOpenLoop(signal);
	}

	public double handleDeadband(double val, double deadband) {
		return (Math.abs(val) > Math.abs(deadband)) ? val : 0.0;
	}
}
