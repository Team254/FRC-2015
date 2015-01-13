
package com.team254.frc2015;

import com.team254.lib.ChezyEncoder;
import com.team254.lib.ChezyTalon;
import com.team254.lib.web.SimplestServer;

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
    ChezyTalon testTalonB = new ChezyTalon("Drive Left A", 0);
    ChezyTalon testTalonA = new ChezyTalon("Drive Left B", 1);
    ChezyEncoder encoder = new ChezyEncoder("Left Drive Encoder", 0, 1);

    int i = 0;

    public void robotInit() {
      new Thread(new Runnable() {
        public void run() {
          try {
            System.out.println("Starting server");
            SimplestServer.startServer();
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      }).start();
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
      testTalonA.set(Math.sin(i/50.0));
      testTalonB.set(Math.cos(i/50.0));
      System.out.println("e: " + encoder.get());
    }

}
