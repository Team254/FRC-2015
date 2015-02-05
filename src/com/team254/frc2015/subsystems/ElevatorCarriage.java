package com.team254.frc2015.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Solenoid;

import com.team254.frc2015.Constants;
import com.team254.frc2015.subsystems.controllers.ElevatorCarriagePositionController;
import com.team254.lib.trajectory.TrajectoryFollower;
import com.team254.lib.util.CheesySpeedController;
import com.team254.lib.util.Controller;
import com.team254.lib.util.Loopable;
import com.team254.lib.util.Subsystem;

public class ElevatorCarriage extends Subsystem implements Loopable {
	public CheesySpeedController m_motor;
	public Solenoid m_brake;
	public Encoder m_encoder;
	public DigitalInput m_home;

	protected Controller m_controller = null;

	protected Position m_position;
	protected Limits m_limits = new Limits();
	boolean m_brake_on_target = false;

	public enum Position {
		TOP, BOTTOM
	}

	public class Limits {
		protected double m_min_position;
		protected double m_max_position;
		protected double m_home_position;
	}

	public ElevatorCarriage(String name, Position position,
			CheesySpeedController motor, Solenoid brake, Encoder encoder,
			DigitalInput home) {
		super(name);
		m_position = position;
		m_motor = motor;
		m_brake = brake;
		m_encoder = encoder;
		m_home = home;
		reloadConstants();
	}

	@Override
	public void reloadConstants() {
		if (m_position == Position.TOP) {
			m_limits.m_min_position = Constants.kTopCarriageMinPositionInches;
			m_limits.m_max_position = Constants.kTopCarriageMaxPositionInches;
			m_limits.m_home_position = Constants.kTopCarriageHomePositionInches;
		} else {
			m_limits.m_min_position = Constants.kBottomCarriageMinPositionInches;
			m_limits.m_max_position = Constants.kBottomCarriageMaxPositionInches;
			m_limits.m_home_position = Constants.kBottomCarriageHomePositionInches;
		}
	}

	public double getHeight() {
		return m_encoder.get() * 2.0 * Constants.kElevatorPulleyRadiusInches
				* Math.PI / Constants.kElevatorEncoderCountsPerRev
				+ m_limits.m_home_position;
	}

	public double getVelocity() {
		return m_encoder.getRate() * 2.0
				* Constants.kElevatorPulleyRadiusInches * Math.PI
				/ Constants.kElevatorEncoderCountsPerRev;
	}

	public synchronized TrajectoryFollower.TrajectorySetpoint getSetpoint() {
		TrajectoryFollower.TrajectorySetpoint setpoint;
		// Rather than reading encoder velocity, we report the last commanded
		// velocity from a velocity profile. This ensures that the input is
		// smooth when changing setpoints.
		if (m_controller instanceof ElevatorCarriagePositionController) {
			setpoint = ((ElevatorCarriagePositionController) m_controller)
					.getSetpoint();
		} else {
			setpoint = new TrajectoryFollower.TrajectorySetpoint();
			setpoint.pos = getHeight();
		}
		return setpoint;
	}

	protected synchronized void setSpeedUnsafe(double speed) {
		m_motor.set(speed);
	}

	protected synchronized void setSpeedSafe(double desired_speed) {
		double height = getHeight();
		if (getBrake()) {
			desired_speed = 0;
		}
		if (height >= m_limits.m_max_position) {
			desired_speed = Math.min(0, desired_speed);
		} else if (height <= m_limits.m_min_position) {
			desired_speed = Math.max(0, desired_speed);
		}
		setSpeedUnsafe(desired_speed);
	}

	protected synchronized void setBrake(boolean on) {
		m_brake.set(!on);
		if (on) {
			setSpeedUnsafe(0);
		}
	}

	public boolean getBrake() {
		return !m_brake.get();
	}

	public synchronized void setPositionSetpoint(double setpoint,
			boolean brake_on_target) {
		if (!(m_controller instanceof ElevatorCarriagePositionController)) {
			TrajectoryFollower.TrajectoryConfig config = new TrajectoryFollower.TrajectoryConfig();
			config.dt = Constants.kDt;
			config.max_acc = Constants.kElevatorMaxAccelInchesPerSec2;
			config.max_vel = Constants.kElevatorMaxSpeedInchesPerSec;
			m_controller = new ElevatorCarriagePositionController(
					Constants.kElevatorCarriagePositionKp,
					Constants.kElevatorCarriagePositionKi,
					Constants.kElevatorCarriagePositionKd,
					Constants.kElevatorCarriagePositionKv,
					Constants.kElevatorCarriagePositionKa, config);
		}
		((ElevatorCarriagePositionController) m_controller).setGoal(
				getSetpoint(), setpoint);
		m_brake_on_target = brake_on_target;
	}

	public synchronized void setOpenLoop(double speed, boolean brake) {
		m_controller = null;
		setBrake(brake);
		setSpeedSafe(speed);
	}

	@Override
	public synchronized void update() {
		if (m_controller instanceof ElevatorCarriagePositionController) {
			ElevatorCarriagePositionController position_controller = (ElevatorCarriagePositionController) m_controller;
			if (position_controller.isOnTarget()) {
				setBrake(m_brake_on_target);
				if (!m_brake_on_target) {
					setSpeedSafe(position_controller.update(getHeight(),
							getVelocity()));
				}
			} else {
				setSpeedSafe(position_controller.update(getHeight(),
						getVelocity()));
			}
		} else {
			// do nothing.
		}
	}
}
