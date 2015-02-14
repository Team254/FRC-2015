package com.team254.frc2015.subsystems;

import com.team254.frc2015.Constants;
import com.team254.frc2015.subsystems.controllers.DriveStraightController;
import com.team254.frc2015.subsystems.controllers.TrajectoryFollowingPositionController;
import com.team254.lib.trajectory.TrajectoryFollower;
import com.team254.lib.util.CheesySpeedController;
import com.team254.lib.util.Controller;
import com.team254.lib.util.DriveSignal;
import com.team254.lib.util.Loopable;
import com.team254.lib.util.Pose;
import com.team254.lib.util.StateHolder;
import com.team254.lib.util.Subsystem;
import com.team254.lib.util.gyro.GyroThread;

import edu.wpi.first.wpilibj.Encoder;

public class Drive extends Subsystem implements Loopable {
	public CheesySpeedController m_left_motor;
	public CheesySpeedController m_right_motor;
	public Encoder m_left_encoder;
	public Encoder m_right_encoder;
	public GyroThread m_gyro;
	protected Controller m_controller = null;

	protected final double m_inches_per_tick = Constants.kDriveWheelSizeInches
			* Math.PI / Constants.kElevatorEncoderCountsPerRev;
	protected final double m_wheelbase_width = 26.0; // Get from CAD
	protected final double m_turn_slip_factor = 1.2; // Measure empirically
	private Pose m_cached_pose = new Pose(0,0,0,0,0,0); // Don't allocate poses at 200Hz!

	public Drive(String name, CheesySpeedController left_drive,
			CheesySpeedController right_drive, Encoder left_encoder,
			Encoder right_encoder, GyroThread gyro) {
		super(name);
		this.m_left_motor = left_drive;
		this.m_right_motor = right_drive;
		this.m_left_encoder = left_encoder;
		this.m_right_encoder = right_encoder;
		this.m_left_encoder.setDistancePerPulse(m_inches_per_tick);
		this.m_right_encoder.setDistancePerPulse(m_inches_per_tick);
		this.m_gyro = gyro;
	}

	public void setOpenLoop(DriveSignal signal) {
		m_controller = null;
		set(signal);
	}

	public synchronized TrajectoryFollower.TrajectorySetpoint getSetpoint() {
		TrajectoryFollower.TrajectorySetpoint setpoint;
		// Rather than reading encoder velocity, we report the last commanded
		// velocity from a velocity profile. This ensures that the input is
		// smooth when changing setpoints.
		if (m_controller instanceof TrajectoryFollowingPositionController) {
			setpoint = ((TrajectoryFollowingPositionController) m_controller)
					.getSetpoint();
		} else {
			setpoint = new TrajectoryFollower.TrajectorySetpoint();
			setpoint.pos = getPose().getLeftDistance();
		}
		return setpoint;
	}

	public synchronized void setDistanceSetpoint(double setpoint) {
		TrajectoryFollower.TrajectorySetpoint prior_setpoint = getSetpoint();
		if (!(m_controller instanceof DriveStraightController)) {
			TrajectoryFollower.TrajectoryConfig config = new TrajectoryFollower.TrajectoryConfig();
			config.dt = Constants.kControlLoopsDt;
			config.max_acc = Constants.kDriveMaxAccelInchesPerSec2;
			config.max_vel = Constants.kDriveMaxSpeedInchesPerSec;
			m_controller = new DriveStraightController(
					Constants.kDrivePositionKp, Constants.kDrivePositionKi,
					Constants.kDrivePositionKd, Constants.kDrivePositionKv,
					Constants.kDrivePositionKa, Constants.kDriveStraightKp,
					Constants.kDriveStraightKi, Constants.kDriveStraightKd,
					Constants.kDriveOnTargetError, config);
		}
		((DriveStraightController) m_controller).setGoal(prior_setpoint,
				setpoint);
	}

	private void set(DriveSignal signal) {
		setLeftRight(signal.leftMotor, signal.rightMotor);
	}

	private void setLeftRight(double left, double right) {
		m_left_motor.set(left);
		m_right_motor.set(-right);
	}

	public Pose getPose() {
		m_cached_pose.reset(m_left_encoder.getDistance(), m_left_encoder.getRate(),
				m_right_encoder.getDistance(), m_right_encoder.getRate(),
				m_gyro.getAngle(), m_gyro.getRate());
		return m_cached_pose;
	}

	@Override
	public void getState(StateHolder states) {
		states.put("gyro_angle", m_gyro.getAngle());
		states.put("left_encoder", m_left_encoder.getDistance());
		states.put("right_encoder", m_right_encoder.getDistance());
	}

	@Override
	public void update() {
		if (m_controller instanceof DriveStraightController) {
			DriveStraightController position_controller = (DriveStraightController) m_controller;
			DriveSignal curSig = position_controller.update(getPose());
			set(curSig);
		} else {
			// do nothing.
		}
	}

}
