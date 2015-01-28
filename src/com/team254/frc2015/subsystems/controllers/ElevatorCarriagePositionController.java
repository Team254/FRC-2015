package com.team254.frc2015.subsystems.controllers;

import com.team254.lib.trajectory.Trajectory;
import com.team254.lib.trajectory.TrajectoryFollower;
import com.team254.lib.util.Controller;
import com.team254.lib.util.SynchronousPID;

public class ElevatorCarriagePositionController extends Controller {
	TrajectoryFollower m_follower;
	Trajectory m_trajectory;
	SynchronousPID m_pid;

	public ElevatorCarriagePositionController(double kp, double ki, double kd,
			double kv, double ka) {
		m_follower = new TrajectoryFollower();
		m_follower.configure(kp, ki, kd, kv, ka);

		m_pid = new SynchronousPID(kp, ki, kd);
	}

	public void setTrajectory(Trajectory trajectory) {
		m_trajectory = trajectory;
		m_follower.setTrajectory(trajectory);
		if (trajectory.getNumSegments() > 0) {
			m_pid.setSetpoint(trajectory.getSegment(trajectory.getNumSegments() - 1).pos);
		} else {
			m_pid.setSetpoint(0.0);
		}
	}

	public double update(double position) {
		if (!m_follower.isFinishedTrajectory()) {
			return m_follower.calculate(position);
		} else {
			return m_pid.calculate(position);
		}
	}

	public double getProfileVelocity() {
		if (!m_follower.isFinishedTrajectory()) {
			return m_trajectory.getSegment(m_follower.getCurrentSegment()).vel;
		} else {
			return 0.0;
		}
	}

	public double getProfilePosition() {
		if (!m_follower.isFinishedTrajectory()) {
			return m_trajectory.getSegment(m_follower.getCurrentSegment()).pos;
		} else {
			return m_trajectory.getSegment(m_trajectory.getNumSegments() - 1).pos;
		}
	}

	@Override
	public void reset() {
		m_follower.reset();
		m_pid.reset();
	}

	@Override
	public boolean isOnTarget() {
		return m_follower.isFinishedTrajectory() && m_pid.onTarget(1.0);
	}

}
