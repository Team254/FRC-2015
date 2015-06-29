package com.team254.frc2015.behavior.routines;

import com.team254.frc2015.behavior.Commands;
import com.team254.frc2015.behavior.RobotSetpoints;

public class ManualRoutine extends Routine {

    @Override
    public void reset() {

    }

    @Override
    public RobotSetpoints update(Commands commands, RobotSetpoints setpoints) {
        if (!setpoints.bottom_open_loop_jog.isPresent()) {
            setpoints.bottom_open_loop_jog = commands.bottom_jog;
        }
        if (!setpoints.top_open_loop_jog.isPresent()) {
            setpoints.top_open_loop_jog = commands.top_jog;
        }

        if (setpoints.claw_action == RobotSetpoints.TopCarriageClawAction.NONE) {
            if (commands.top_carriage_claw_request == Commands.TopCarriageClawRequest.OPEN) {
                setpoints.claw_action = RobotSetpoints.TopCarriageClawAction.OPEN;
            } else if (commands.top_carriage_claw_request == Commands.TopCarriageClawRequest.CLOSE) {
                setpoints.claw_action = RobotSetpoints.TopCarriageClawAction.CLOSE;
            }
        } else if (setpoints.claw_action == RobotSetpoints.TopCarriageClawAction.PREFER_CLOSE) {
            if (commands.top_carriage_claw_request == Commands.TopCarriageClawRequest.OPEN) {
                setpoints.claw_action = RobotSetpoints.TopCarriageClawAction.OPEN;
            }
        }

        if (setpoints.pivot_action == RobotSetpoints.TopCarriagePivotAction.NONE) {
            if (commands.top_carriage_pivot_request == Commands.TopCarriagePivotRequest.PIVOT_UP) {
                setpoints.pivot_action = RobotSetpoints.TopCarriagePivotAction.PIVOT_UP;
            } else if (commands.top_carriage_pivot_request == Commands.TopCarriagePivotRequest.PIVOT_DOWN) {
                setpoints.pivot_action = RobotSetpoints.TopCarriagePivotAction.PIVOT_DOWN;
            }
        }


        if (setpoints.flapper_action == RobotSetpoints.BottomCarriageFlapperAction.NONE) {
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
        } else if (setpoints.intake_action == RobotSetpoints.IntakeAction.NONE) {
            if (commands.intake_request == Commands.IntakeRequest.OPEN) {
                setpoints.intake_action = RobotSetpoints.IntakeAction.OPEN;
            } else if (commands.intake_request == Commands.IntakeRequest.CLOSE) {
                setpoints.intake_action = RobotSetpoints.IntakeAction.CLOSE;
            }
        }

        if (setpoints.roller_action == RobotSetpoints.RollerAction.NONE) {
            if (commands.roller_request == Commands.RollerRequest.INTAKE) {
                // Run intake inwards.
                setpoints.roller_action = RobotSetpoints.RollerAction.INTAKE;
            } else if (commands.roller_request == Commands.RollerRequest.EXHAUST) {
                // Run intake inwards.
                setpoints.roller_action = RobotSetpoints.RollerAction.EXHAUST;
            }
        }

        setpoints.deploy_peacock = commands.deploy_peacock;

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
