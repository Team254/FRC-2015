package com.team254.frc2015.behavior.routines;

import com.team254.frc2015.behavior.Commands;
import com.team254.frc2015.behavior.RobotSetpoints;
import edu.wpi.first.wpilibj.Timer;

import java.util.Optional;

public class AfterFloorClampRoutine extends Routine {

    private boolean m_first_time = true;
    private Timer m_timer = new Timer();
    private boolean m_done = false;

    @Override
    public void reset() {
        m_timer.stop();
        m_timer.reset();
        m_first_time = true;
        m_done = false;
    }

    @Override
    public RobotSetpoints update(Commands commands, RobotSetpoints setpoints) {
        if (m_first_time) {
            m_timer.start();
        }
        m_first_time = false;
        if (m_timer.get() > .125) {
            setpoints.top_open_loop_jog = Optional.of(0.0);
            m_done = true;
        } else {
            setpoints.top_open_loop_jog = Optional.of(-.65);
        }
        setpoints.bottom_open_loop_jog = Optional.of(0.0);
        return setpoints;
    }

    @Override
    public void cancel() {

    }

    @Override
    public boolean isFinished() {
        return m_done;
    }

    @Override
    public String getName() {
        return "Clamp";
    }
}
