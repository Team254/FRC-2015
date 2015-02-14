package com.team254.frc2015.actions;

import edu.wpi.first.wpilibj.Timer;

public class TimeoutAction extends Action {
	double m_timeout;
	double m_time_start;

	public TimeoutAction(double timeout) {
		m_timeout = timeout;
	}

	@Override
	public boolean isFinished() {
		return Timer.getFPGATimestamp() >=  m_time_start + m_timeout;
	}

	@Override
	public void update() { }

	@Override
	public void done() { }

	@Override
	public void start() {
		m_time_start = Timer.getFPGATimestamp();		
	}

}
