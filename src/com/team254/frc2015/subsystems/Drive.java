package com.team254.frc2015.subsystems;

import com.team254.lib.util.CheesySpeedController;
import com.team254.lib.util.Pose;
import com.team254.lib.util.StateHolder;
import com.team254.lib.util.Subsystem;

import edu.wpi.first.wpilibj.Encoder;

public class Drive extends Subsystem {
	public CheesySpeedController m_left_motor;
	public CheesySpeedController m_right_motor;
	public Encoder m_left_encoder;
	public Encoder m_right_encoder;

	protected final double m_inches_per_tick = 4.0 * Math.PI / 360.0; // 4 inch
																		// diameter
																		// wheel,
																		// 360
																		// CPR
																		// encoder
	protected final double m_wheelbase_width = 26.0; // Get from CAD
	protected final double m_turn_slip_factor = 1.2; // Measure empirically

	public Drive(String name, CheesySpeedController left_drive,
			CheesySpeedController right_drive, Encoder left_encoder,
			Encoder right_encoder) {
		super(name);
		this.m_left_motor = left_drive;
		this.m_right_motor = right_drive;
		this.m_left_encoder = left_encoder;
		this.m_right_encoder = right_encoder;
		this.m_left_encoder.setDistancePerPulse(m_inches_per_tick);
		this.m_right_encoder.setDistancePerPulse(m_inches_per_tick);
	}

	public void setLeftRight(double left, double right) {
		m_left_motor.set(left);
		m_right_motor.set(-right);
	}

	public Pose getPose() {
		return new Pose(m_left_encoder.getDistance(), m_left_encoder.getRate(),
				m_right_encoder.getDistance(), m_right_encoder.getRate(), 0, 0);
	}

	@Override
	public void getState(StateHolder states) {

	}

}
