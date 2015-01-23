package com.team254.frc2015.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.VictorSP;

import com.team254.lib.util.CheesySpeedController;
import com.team254.lib.util.Subsystem;

public class ElevatorCarriage extends Subsystem {
	public CheesySpeedController m_motor;
	public Solenoid m_brake;
	public Encoder m_encoder;
	public DigitalInput m_home;
	
	protected Position m_position;
	protected Limits m_limits = new Limits();
	protected final double m_inches_per_tick = 0.564 * 2.0 * Math.PI / 360.0;  // Pulley radius = .564", 360 CPR
	
	public enum Position {
		TOP, BOTTOM
	}
	
	public class Limits {
		protected double m_min_position;
		protected double m_max_position;
		protected double m_max_speed;
		protected double m_max_acceleration;
		protected double m_home_position;
	}
	
	public ElevatorCarriage(String name, Position position,
			CheesySpeedController motor, Solenoid brake, Encoder encoder, DigitalInput home) {
		super(name);
		m_position = position;
		m_motor = motor;
		m_brake = brake;
		m_encoder = encoder;
		m_home = home;
		
		if (m_position == Position.TOP) {
			m_limits.m_min_position = 10.0;
			m_limits.m_max_position = 70.0;
			m_limits.m_max_speed = 60.0;
			m_limits.m_max_acceleration = 180.0;
			m_limits.m_home_position = 20.0;
		} else {
			m_limits.m_min_position = 0.0;
			m_limits.m_max_position = 60.0;
			m_limits.m_max_speed = 60.0;
			m_limits.m_max_acceleration = 180.0;
			m_limits.m_home_position = 0.0;
		}
	}
	
	public double getHeight() {
		return m_encoder.get() * m_inches_per_tick;
	}
	
	public void setSpeedUnsafe(double speed) {
		setBrake(true);
		m_motor.set(speed);
	}
	
	public void setSpeedSafe(double desired_speed) {
		double height = getHeight();
		if (height >= m_limits.m_max_position) {
			desired_speed = Math.min(0, desired_speed);
		} else if (height <= m_limits.m_min_position) {
			desired_speed = Math.max(0, desired_speed);
		}
		setSpeedUnsafe(desired_speed);
	}
	
	public void setBrake(boolean on) {
		m_brake.set(!on);
	}
}
