package com.team254.frc2015.behavior.routines;

import com.team254.frc2015.Constants;
import com.team254.frc2015.ElevatorSafety;
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

        if (commands.preset_height == Commands.PresetHeight.CARRY) {
            // Carrying
            setpoints.m_elevator_setpoints.bottom_setpoint = Optional.of(18.0);

            // Figure out how to move this later
            top_carriage.setSqueezeSetpoint(.7);
        } else if (commands.preset_height == Commands.PresetHeight.CAPPING) {
            // Capping
            setpoints.m_elevator_setpoints.top_setpoint = Optional
                    .of(Constants.kTopCarriageMaxPositionInches);
            setpoints.m_elevator_setpoints.bottom_setpoint = Optional.of(0.25);
        } else if (commands.preset_height == Commands.PresetHeight.CAN) {
            // Can loading
            setpoints.m_elevator_setpoints.top_setpoint = Optional.of(5.5);
            setpoints.m_elevator_setpoints.bottom_setpoint = Optional.of(3.5);
        } else if (commands.preset_height == Commands.PresetHeight.FLOOR) {
            // Floor load
            setpoints.m_elevator_setpoints.bottom_setpoint = Optional.of(0.25);
            setpoints.m_elevator_setpoints.top_setpoint = Optional.of(Math.max(30.0, top_carriage.getHeight()));
        } else if (commands.preset_height == Commands.PresetHeight.HUMAN) {
            // Human Loading
            setpoints.m_elevator_setpoints.bottom_setpoint = Optional.of(33.0);
            setpoints.m_elevator_setpoints.top_setpoint = Optional.of(Constants.kTopCarriageMaxPositionInches);
        } else if (commands.preset_height == Commands.PresetHeight.HOME) {
            // Home
            setpoints.m_elevator_setpoints.bottom_setpoint = Optional
                    .of(Constants.kBottomCarriageHomePositionInches);
            setpoints.m_elevator_setpoints.top_setpoint = Optional
                    .of(Constants.kTopCarriageHomePositionInches);
        }

        // Set jogs
        setpoints.bottom_open_loop_jog = commands.bottom_jog;
        setpoints.top_open_loop_jog = commands.top_jog;

        // Top carriage actions.
        if (commands.top_carriage_claw_request == Commands.TopCarriageClawRequest.OPEN) {
            setpoints.claw_action = RobotSetpoints.TopCarriageClawAction.OPEN;
        } else if (commands.top_carriage_claw_request == Commands.TopCarriageClawRequest.CLOSE) {
            setpoints.claw_action = RobotSetpoints.TopCarriageClawAction.CLOSE;
        }

        // Top carriage actions.
        if (commands.top_carriage_pivot_request == Commands.TopCarriagePivotRequest.PIVOT_UP) {
            setpoints.pivot_action = RobotSetpoints.TopCarriagePivotAction.PIVOT_UP;
        } else if (commands.top_carriage_pivot_request == Commands.TopCarriagePivotRequest.PIVOT_DOWN) {
            setpoints.pivot_action = RobotSetpoints.TopCarriagePivotAction.PIVOT_DOWN;
        }

        // Bottom carriage actions.
        if (commands.bottom_carriage_flapper_request == Commands.BottomCarriageFlapperRequest.OPEN) {
            setpoints.flapper_action = RobotSetpoints.BottomCarriageFlapperAction.OPEN;
        } else if (commands.bottom_carriage_flapper_request == Commands.BottomCarriageFlapperRequest.CLOSE) {
            setpoints.flapper_action = RobotSetpoints.BottomCarriageFlapperAction.CLOSE;
        }

        // Intake actions.
        if (commands.intake_request == Commands.IntakeRequest.OPEN) {
            // Open intake
            setpoints.intake_action = RobotSetpoints.IntakeAction.OPEN;
        } else if (commands.intake_request == Commands.IntakeRequest.CLOSE) {
            // Close intake
            setpoints.intake_action = RobotSetpoints.IntakeAction.CLOSE;
        }  else if (commands.intake_request == Commands.IntakeRequest.NEUTRAL) {
            // Neutral intake
            setpoints.intake_action = RobotSetpoints.IntakeAction.NEUTRAL;
        }

        // Roller actions.
        if (commands.roller_request == Commands.RollerRequest.INTAKE) {
            // Run intake inwards.
            setpoints.roller_action = RobotSetpoints.RollerAction.INTAKE;
        } else if (commands.roller_request == Commands.RollerRequest.EXHAUST) {
            // Run intake outwards.
            setpoints.roller_action = RobotSetpoints.RollerAction.EXHAUST;
        }

        return setpoints;
    }

    @Override
    public void cancel() {

    }
}
