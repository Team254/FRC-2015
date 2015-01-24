package com.team254.frc2015.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.VictorSP;

import com.team254.frc2015.subsystems.controllers.ElevatorCarriageCurrentController;
import com.team254.lib.trajectory.TrajectoryFollower;
import com.team254.lib.util.CheesySpeedController;
import com.team254.lib.util.Controller;
import com.team254.lib.util.Loopable;
import com.team254.lib.util.Subsystem;

public class ElevatorCarriage extends Subsystem implements Loopable {
	// /// TUNING CONSTANTS ///
	public static final double kCurrentKp = 0.05;
	public static final double kCurrentKi = 0.0;
	public static final double kCurrentKd = 0.0;
	public static final double kCurrentMaxOutput = 1.0;
	// /// END TUNING CONSTANTS ///
	
	public CheesySpeedController m_motor;
	public Solenoid m_brake;
	public Encoder m_encoder;
	public DigitalInput m_home;

	protected Controller m_current_controller = null;
	
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
	}
	
	public boolean getBrake() {
		return !m_brake.get();
	}
	
	public synchronized void setPositionSetpoint(double setpoint, boolean brake_on_target) {
		// TODO
	}
	
	public synchronized void setCurrentUpSetpoint(double setpoint) {
		if (!(m_current_controller instanceof ElevatorCarriageCurrentController)) {
			m_current_controller = new ElevatorCarriageCurrentController(kCurrentKp, kCurrentKi, kCurrentKd, kCurrentMaxOutput);
		}
		((ElevatorCarriageCurrentController) m_current_controller).setGoal(true, setpoint);
	}
	
	public synchronized void setCurrentDownSetpoint(double setpoint) {
		if (!(m_current_controller instanceof ElevatorCarriageCurrentController)) {
			m_current_controller = new ElevatorCarriageCurrentController(kCurrentKp, kCurrentKi, kCurrentKd, kCurrentMaxOutput);
		}
		((ElevatorCarriageCurrentController) m_current_controller).setGoal(false, setpoint);
	}
	
	public synchronized void setOpenLoop(double speed, boolean brake) {
		m_current_controller = null;
		setBrake(brake);
		setSpeedSafe(speed);
	}

	@Override
	public synchronized void update() {
		if (m_current_controller instanceof ElevatorCarriageCurrentController) {
			((ElevatorCarriageCurrentController) m_current_controller).update(m_motor.getCurrent());
		} else {
			// do nothing.
		}
	}
}
