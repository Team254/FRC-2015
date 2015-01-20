
package com.team254.frc2015;

import com.team254.frc2015.subsystems.Drive;
import com.team254.frc2015.subsystems.Elevator;
import com.team254.frc2015.web.WebServer;
import com.team254.lib.util.Logger;
import com.team254.lib.util.Serializable;
import com.team254.lib.util.SystemManager;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import com.team254.lib.util.LidarLiteSensor;

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
    LidarLiteSensor mLidarLiteSensor = new LidarLiteSensor(I2C.Port.kMXP);

    static {
    	SystemManager.getInstance().add(RobotData.robotTime);
    	SystemManager.getInstance().add(Logger.getInstance());
    }

    public void robotInit() {
        mLidarLiteSensor.start();
        WebServer.startServer();

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
