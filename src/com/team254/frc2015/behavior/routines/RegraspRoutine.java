package com.team254.frc2015.behavior.routines;

import com.team254.frc2015.behavior.Commands;
import com.team254.frc2015.behavior.RobotSetpoints;

import java.util.Optional;

/**
 * Created by tombot on 3/27/15.
 */
public class RegraspRoutine extends Routine {
    private boolean m_saw_sensor = false;

    @Override
    public void reset() {
        m_saw_sensor = false;
    }

    @Override
    public RobotSetpoints update(Commands commands, RobotSetpoints setpoints) {
        setpoints.top_carriage_squeeze = true;
        setpoints.bottom_open_loop_jog = Optional.of(0.0);
        m_saw_sensor |= top_carriage.getBreakbeamTriggered();
        setpoints.claw_action = m_saw_sensor ? RobotSetpoints.TopCarriageClawAction.PREFER_CLOSE : RobotSetpoints.TopCarriageClawAction.NEUTRAL;
        return setpoints;
    }

    @Override
    public void cancel() {

    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public String getName() {
        return "Regrasp";
    }
}
