
package com.team254.frc2015;

import com.team254.frc2015.subsystems.Drive;
import com.team254.frc2015.web.WebServer;
import com.team254.lib.ChezyEncoder;
import com.team254.lib.ChezyTalon;

import edu.wpi.first.wpilibj.IterativeRobot;

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
        
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    
    }
    
    public void disabledPeriodic() {
      i++;
      drive.setLeftRight(Math.sin(i/50.0), Math.cos(i/50.0));
      WebServer.updateAllStateStreams();
    }

}
