package com.team254.lib.util;

import com.team254.frc2015.HardwareAdaptor;

import edu.wpi.first.wpilibj.SpeedController;

public class CheesySpeedController implements SpeedController {
	protected SpeedController[] m_controllers;
	protected int[] m_pdp_slots;
	
	public CheesySpeedController(SpeedController controller, int pdp_slot) {
		m_controllers = new SpeedController[] {controller};
		m_pdp_slots = new int[] {pdp_slot};
	}

	public CheesySpeedController(SpeedController controller, int[] pdp_slots) {
		m_controllers = new SpeedController[] {controller};
		m_pdp_slots = pdp_slots;
	}
	
	public CheesySpeedController(SpeedController[] controllers, int[] pdp_slots) {
		assert(controllers.length == pdp_slots.length);
		m_controllers = controllers;
		m_pdp_slots = pdp_slots;
	}
	
	public double getCurrent() {
		double current = 0.0;
		for (int slot : m_pdp_slots) {
			current += HardwareAdaptor.kPDP.getCurrent(slot);
		}
		return current;
	}
	
	@Override
	public void pidWrite(double output) {
		for (SpeedController controller : m_controllers) {
			controller.pidWrite(output);
		}
	}
	
	@Override
	public double get() {
		return m_controllers[0].get();
	}
	
	@Override
	public void set(double speed, byte syncGroup) {
		for (SpeedController controller : m_controllers) {
			controller.set(speed, syncGroup);
		}
	}
	
	@Override
	public void set(double speed) {
		for (SpeedController controller : m_controllers) {
			controller.set(speed);
		}
	}
	
	@Override
	public void disable() {
		for (SpeedController controller : m_controllers) {
			controller.disable();
		}
	}
}