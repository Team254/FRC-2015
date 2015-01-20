package com.team254.frc2015;

import com.team254.lib.util.Serializable;

import edu.wpi.first.wpilibj.PowerDistributionPanel;
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
    
    public static final Serializable batteryVoltage = new Serializable() {
        public Object getState() {
        return HardwareAdaptor.pdp.getVoltage();
      }

        public String getName() {
        return "voltage";
      }

        public String getType() {
            return Double.class.getName();
        }
    };
}
