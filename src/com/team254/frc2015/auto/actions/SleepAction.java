package com.team254.frc2015.auto.actions;

import com.team254.frc2015.auto.AutoAction;

import edu.wpi.first.wpilibj.Timer;

public class SleepAction extends AutoAction {
	double m_time_to_sleep;
	double m_time_start;

	public SleepAction(double timeToSleep) {
		m_time_to_sleep = timeToSleep;
	}

	@Override
	public boolean isFinished() {
		return Timer.getFPGATimestamp() >=  m_time_start + m_time_to_sleep;
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
