package com.team254.frc2015.subsystems.controllers;

import com.team254.lib.util.Controller;
import com.team254.lib.util.SynchronousPID;

public class ElevatorCarriageCurrentController extends Controller {
	SynchronousPID m_pid;
	boolean m_go_up;
	
	public ElevatorCarriageCurrentController(double kp, double ki, double kd, double max_output) {
		m_pid = new SynchronousPID(kp, ki, kd);
		m_pid.setOutputRange(-Math.abs(max_output), Math.abs(max_output));
	}
	
	public void setGoal(boolean push_upwards, double goal) {
		m_pid.setSetpoint(goal);
		if (m_go_up != push_upwards) {
			// Direction has changed.
			reset();
			m_go_up = push_upwards;
		}
	}

	public double update(double measured) {
		double output = m_pid.calculate(measured);
		if (!m_go_up) {
			output *= -1;
		}
		return output;
	}

	@Override
	public void reset() {
		m_pid.reset();
	}

	@Override
	public boolean isOnTarget() {
		// This is a "best effort" controller
		return true;
	}

}
