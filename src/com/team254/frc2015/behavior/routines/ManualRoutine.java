package com.team254.frc2015.behavior.routines;

import com.team254.frc2015.behavior.Commands;
import com.team254.frc2015.behavior.RobotSetpoints;

import java.util.Optional;

/**
 * Created by tombot on 2/26/15.
 */
public class ManualRoutine extends Routine {
    private static Optional<Double> m_nullopt = Optional.empty();

    @Override
    public void reset() {

    }

    @Override
    public RobotSetpoints update(Commands commands, RobotSetpoints existing_setpoints) {
        RobotSetpoints setpoints = existing_setpoints;

        // Turn off jog
        setpoints.bottom_open_loop_jog = m_nullopt;
        setpoints.top_open_loop_jog = m_nullopt;

        if (commands.preset_request == Commands.PresetRequest.CARRY_SQUEZE) {
            // Carrying
            setpoints.m_elevator_setpoints.bottom_setpoint = Optional.of(18.0);

            // Figure out how to move this later
            top_carriage.squeeze();
        } else if (commands.preset_request == Commands.PresetRequest.SCORE) {
            // Capping
            setpoints.m_elevator_setpoints.top_setpoint = Optional
                    .of(top_carriage.getHeight() + 2.0);
            setpoints.m_elevator_setpoints.bottom_setpoint = Optional.of(0.5);
        }  else if (commands.preset_request == Commands.PresetRequest.FLOOR) {
            // Floor load
            setpoints.m_elevator_setpoints.bottom_setpoint = Optional.of(0.5);
            setpoints.m_elevator_setpoints.top_setpoint = Optional.of(Math.max(30.0, top_carriage.getHeight()));
        }
        // Set jogs
        setpoints.bottom_open_loop_jog = commands.bottom_jog;
        setpoints.top_open_loop_jog = commands.top_jog;

        // Top carriage actions.
        if (setpoints.claw_action == RobotSetpoints.TopCarriageClawAction.NONE) {
            if (commands.top_carriage_claw_request == Commands.TopCarriageClawRequest.OPEN) {
                setpoints.claw_action = RobotSetpoints.TopCarriageClawAction.OPEN;
            } else if (commands.top_carriage_claw_request == Commands.TopCarriageClawRequest.CLOSE) {
                setpoints.claw_action = RobotSetpoints.TopCarriageClawAction.CLOSE;
            }
        }

        // Top carriage actions.
        if (setpoints.pivot_action == RobotSetpoints.TopCarriagePivotAction.NONE) {
            if (commands.top_carriage_pivot_request == Commands.TopCarriagePivotRequest.PIVOT_UP) {
                setpoints.pivot_action = RobotSetpoints.TopCarriagePivotAction.PIVOT_UP;
            } else if (commands.top_carriage_pivot_request == Commands.TopCarriagePivotRequest.PIVOT_DOWN) {
                setpoints.pivot_action = RobotSetpoints.TopCarriagePivotAction.PIVOT_DOWN;
            }
        }


        if (setpoints.flapper_action == RobotSetpoints.BottomCarriageFlapperAction.NONE) {
            // Bottom carriage actions.
            if (commands.bottom_carriage_flapper_request == Commands.BottomCarriageFlapperRequest.OPEN) {
                setpoints.flapper_action = RobotSetpoints.BottomCarriageFlapperAction.OPEN;
            } else if (commands.bottom_carriage_flapper_request == Commands.BottomCarriageFlapperRequest.CLOSE) {
                setpoints.flapper_action = RobotSetpoints.BottomCarriageFlapperAction.CLOSE;
            }
        }

        if (setpoints.intake_action == RobotSetpoints.IntakeAction.PREFER_CLOSE) {
            setpoints.intake_action = RobotSetpoints.IntakeAction.CLOSE;
            if (commands.intake_request == Commands.IntakeRequest.OPEN) {
                setpoints.intake_action = RobotSetpoints.IntakeAction.OPEN;
            }
        } else if (setpoints.intake_action == RobotSetpoints.IntakeAction.PREFER_OPEN) {
            setpoints.intake_action = RobotSetpoints.IntakeAction.OPEN;
            if (commands.intake_request == Commands.IntakeRequest.CLOSE) {
                setpoints.intake_action = RobotSetpoints.IntakeAction.CLOSE;
            }
        } else if (setpoints.intake_action == RobotSetpoints.IntakeAction.PREFER_NEUTRAL_HP) {
            setpoints.intake_action = RobotSetpoints.IntakeAction.NEUTRAL;
            if (commands.intake_request == Commands.IntakeRequest.CLOSE) {
                setpoints.intake_action = RobotSetpoints.IntakeAction.CLOSE;
            }
        } else if (setpoints.intake_action == RobotSetpoints.IntakeAction.NONE) {
            if (commands.intake_request == Commands.IntakeRequest.OPEN) {
                setpoints.intake_action = RobotSetpoints.IntakeAction.OPEN;
            } else if (commands.intake_request == Commands.IntakeRequest.CLOSE) {
                setpoints.intake_action = RobotSetpoints.IntakeAction.CLOSE;
            } else if (commands.intake_request == Commands.IntakeRequest.NEUTRAL) {
                setpoints.intake_action = RobotSetpoints.IntakeAction.NEUTRAL;
            }
        }

        // Roller actions.
        if (setpoints.roller_action == RobotSetpoints.RollerAction.NONE) {
            if (commands.roller_request == Commands.RollerRequest.INTAKE) {
                // Run intake inwards.
                setpoints.roller_action = RobotSetpoints.RollerAction.INTAKE;
            }
        }

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
        return "Manual";
    }
}
