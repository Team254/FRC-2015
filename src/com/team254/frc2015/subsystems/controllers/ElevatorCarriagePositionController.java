package com.team254.frc2015.subsystems.controllers;

import com.team254.lib.trajectory.Trajectory;
import com.team254.lib.trajectory.TrajectoryFollower;
import com.team254.lib.util.Controller;
import com.team254.lib.util.SynchronousPID;

public class ElevatorCarriagePositionController extends Controller {
	TrajectoryFollower m_follower;
	SynchronousPID m_pid;

	public ElevatorCarriagePositionController(double kp, double ki, double kd,
			double kv, double ka) {
		m_follower = new TrajectoryFollower();
		m_follower.configure(kp, ki, kd, kv, ka);

		m_pid = new SynchronousPID(kp, ki, kd);
	}

	public void setTrajectory(Trajectory trajectory) {
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
