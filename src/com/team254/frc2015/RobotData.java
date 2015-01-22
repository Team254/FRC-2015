package com.team254.frc2015;

import com.team254.lib.util.Serializable;

import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.Timer;

public class RobotData {
	public static final Serializable robotTime = new Serializable() {
        @Override
		public Object getState() {
			return Timer.getFPGATimestamp();
		}
        @Override
		public String getName() {
			return "robotTime";
		}
        @Override
		public String getType() {
			return Double.class.toString();
		}
    };
    
    public static final Serializable batteryVoltage = new Serializable() {
        @Override
        public Object getState() {
        return HardwareAdaptor.kPDP.getVoltage();
      }

        @Override
        public String getName() {
        return "voltage";
      }

        @Override
        public String getType() {
            return Double.class.getName();
        }
    };
}
