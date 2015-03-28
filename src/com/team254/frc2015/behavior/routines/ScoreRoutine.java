package com.team254.frc2015.behavior.routines;

import com.team254.frc2015.Constants;
import com.team254.frc2015.behavior.Commands;
import com.team254.frc2015.behavior.RobotSetpoints;

import java.util.Optional;

/**
 * Created by tombot on 3/27/15.
 */
public class ScoreRoutine extends Routine {
    private boolean m_first_run = true;
    private boolean m_was_on_target = false;

    @Override
    public void reset() {
        m_first_run = true;
        m_was_on_target = false;
    }

    @Override
    public RobotSetpoints update(Commands commands, RobotSetpoints setpoints) {
        if (m_first_run) {
            // Capping
            setpoints.m_elevator_setpoints.top_setpoint = Optional.of(top_carriage.getHeight() + 1.0);
            setpoints.m_elevator_setpoints.bottom_setpoint = Optional.of(1.0);
        }
        m_was_on_target |= bottom_carriage.getHeight() < 2.0;
        if (m_was_on_target) {
            setpoints.flapper_action = RobotSetpoints.BottomCarriageFlapperAction.OPEN;
        }
        m_first_run = false;
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
        return null;
    }
}
