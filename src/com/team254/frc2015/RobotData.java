package com.team254.frc2015;

import com.team254.lib.util.Serializable;

import edu.wpi.first.wpilibj.Timer;

public class RobotData {
	public static final Serializable robotTime = new Serializable() {
		public Object getState() {
			return Timer.getFPGATimestamp();
		}
		public String getName() {
			return "robotTime";
		}
		public String getType() {
			return Double.class.toString();
		}
    };
}
