package com.team254.frc2015.behavior.routines;

import com.team254.frc2015.behavior.Commands;
import com.team254.frc2015.behavior.RobotSetpoints;

import java.util.Optional;

/**
 * Created by tombot on 2/27/15.
 */
public abstract class SimplePresetRoutine extends Routine {
    RobotSetpoints m_preset_setpoints = new RobotSetpoints();
    boolean m_first_run = true;
    Optional<Double> m_top_height_setpoint = Optional.empty();
    Optional<Double> m_bottom_height_setpoint = Optional.empty();
    Optional<Double> m_nullopt = Optional.empty();

    @Override
    public void reset() {
        m_first_run = true;

    }

    @Override
    public RobotSetpoints update(Commands commands, RobotSetpoints existing_setpoints) {
        if (m_first_run) {
            if (m_bottom_height_setpoint.isPresent()) {
                m_preset_setpoints.m_elevator_setpoints.bottom_setpoint = Optional.of(m_bottom_height_setpoint.get());
            }
            if (m_top_height_setpoint.isPresent()) {
                m_preset_setpoints.m_elevator_setpoints.top_setpoint = Optional.of(m_top_height_setpoint.get());
            }
        } else {
            m_preset_setpoints.m_elevator_setpoints.bottom_setpoint = m_nullopt;
            m_preset_setpoints.m_elevator_setpoints.top_setpoint = m_nullopt;
        }
        m_first_run = false;
        return m_preset_setpoints;
    }

    @Override
    public void cancel() {

    }

    @Override
    public boolean isFinished() {
        return false;
    }

    public abstract void setPresets();

    public static SimplePresetRoutine RammingModePresetRoutine = new SimplePresetRoutine() {

        @Override
        public void setPresets() {
            m_bottom_height_setpoint = Optional.of(0.0);
            m_top_height_setpoint = Optional.of(20.0);
            m_preset_setpoints.intake_action = RobotSetpoints.IntakeAction.OPEN;
        }
    };
}
