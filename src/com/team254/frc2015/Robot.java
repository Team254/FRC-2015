
package com.team254.frc2015;

import java.io.File;
import java.util.Collection;

import com.team254.frc2015.subsystems.Drive;
import com.team254.frc2015.web.WebServer;
import com.team254.lib.util.Logger;
import com.team254.lib.util.Serializable;
import com.team254.lib.util.SystemManager;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
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
    CheesyDriveHelper cdh = new CheesyDriveHelper(drive);
    Joystick leftStick = new Joystick(0);
    Joystick rightStick = new Joystick(1);

	static {
		SystemManager.getInstance().add(RobotData.robotTime);
	}

    public void robotInit() {
      WebServer.startServer();
      
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
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    
    }
    
    public void disabledPeriodic() {
    	i++;
    	WebServer.updateAllStateStreams();
    	Logger.println(SystemManager.getInstance().get().toJSONString());
    }

}
