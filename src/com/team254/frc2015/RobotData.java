package com.team254.frc2015;

import com.team254.lib.util.StateHolder;
import com.team254.lib.util.Tappable;
import edu.wpi.first.wpilibj.Timer;

public class RobotData implements Tappable {

    @Override
    public String getName() {
        return "robot";
    }

    @Override
    public void getState(StateHolder states) {
        states.put("voltage", HardwareAdaptor.kPDP.getVoltage());
        states.put("robotTime", Timer.getFPGATimestamp());
    }
}
