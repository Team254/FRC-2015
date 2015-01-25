
package com.team254.frc2015;

import java.io.File;
import java.util.Collection;

import com.team254.frc2015.subsystems.Drive;
import com.team254.frc2015.subsystems.Elevator;
import com.team254.frc2015.web.WebServer;
import com.team254.lib.util.Logger;
import com.team254.lib.util.Serializable;
import com.team254.lib.util.SystemManager;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    int i = 0;
    Drive drive = HardwareAdaptor.drive;
    Elevator elevator = HardwareAdaptor.elevator;
    CheesyDriveHelper cdh = new CheesyDriveHelper(drive);
    Joystick leftStick = new Joystick(0);
    Joystick rightStick = new Joystick(1);
    Joystick buttonBoard = new Joystick(2);
    PowerDistributionPanel pdp = new PowerDistributionPanel();
    Solenoid intakeOpen = new Solenoid(1);

    Talon leftIntake = new Talon(1);
    Talon rightIntake = new Talon(4);

	static {
		SystemManager.getInstance().add(RobotData.robotTime);
		SystemManager.getInstance().add(Logger.getInstance());
	}

    public void robotInit() {
      WebServer.startServer();
      HardwareAdaptor.lidar.start(20);
      SystemManager.getInstance().add(new Serializable() {
		public Object getState() {
			return pdp.getVoltage();
		}

		public String getName() {
			return "voltage";
		}

		public String getType() {
			return Double.class.getName();
		}
      });
      
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
    	
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
    	cdh.cheesyDrive(leftStick.getY(), -rightStick.getX(), rightStick.getRawButton(1), true);
    	elevator.setElevator(buttonBoard.getRawAxis(3) < 0.5);

    	double left = 0;
    	double right = 0;
    	if (buttonBoard.getRawButton(7)) {
    		left = 1;
    		right = -1;
    	} else if ( buttonBoard.getRawButton(8)) {
    		left = -1;
    		right = 1;
    		if (HardwareAdaptor.lidar.getDistance() < 1.2) {
    			intakeOpen.set(false);
    		} else {
    			intakeOpen.set(true);
    		}
    	} else {
    		intakeOpen.set(true);
    	}
    	leftIntake.set(left);
    	rightIntake.set(right);
/*
    	if (buttonBoard.getRawButton(9)) {
    		intakeOpen.set(false);
    	} else if (buttonBoard.getRawButton(10)) {
    		intakeOpen.set(true);
    	}
*/
    	System.out.println(HardwareAdaptor.lidar.getDistance());
    	
    	
    	
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    
    }
    
    public void disabledPeriodic() {
    	i++;
    	WebServer.updateAllStateStreams();
    	HardwareAdaptor.sonar.getDistanceInInches();
    	//Logger.println(SystemManager.getInstance().get().toJSONString());
    	System.out.println(HardwareAdaptor.lidar.getDistance());
    }

}
