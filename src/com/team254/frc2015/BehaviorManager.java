package com.team254.frc2015;

import com.team254.frc2015.subsystems.BottomCarriage;
import com.team254.frc2015.subsystems.Drive;
import com.team254.frc2015.subsystems.ElevatorCarriage;
import com.team254.frc2015.subsystems.Intake;
import com.team254.frc2015.subsystems.TopCarriage;

import edu.wpi.first.wpilibj.PowerDistributionPanel;

public class BehaviorManager {
    protected Drive drive = HardwareAdaptor.kDrive;
    protected TopCarriage top_carriage = HardwareAdaptor.kTopCarriage;
    protected BottomCarriage bottom_carriage = HardwareAdaptor.kBottomCarriage;
    protected Intake intake = HardwareAdaptor.kIntake;
    protected PowerDistributionPanel pdp = HardwareAdaptor.kPDP;

    public enum ElevatorMode {
        BOTTOM_CARRIAGE_MODE, TOP_CARRIAGE_MODE
    }

    public enum PresetHeight {
        NONE, MANUAL, ONE, TWO, THREE, FOUR, FIVE, SIX
    }

    public enum IntakeAction {
        NONE, OPEN, CLOSE
    }

    public enum RollerAction {
        NONE, INTAKE, EXHAUST
    }

    public enum ElevatorAction {
        NONE, HP_LOAD_ACTION, FLOOR_LOAD_ACTION, LOAD_PREP_ACTION
    }

    public enum TopCarriagePivotAction {
        NONE, PIVOT_DOWN, PIVOT_UP
    }

    public enum TopCarriageClawAction {
        NONE, OPEN, CLOSE
    }

    public enum BottomCarriageFlapperAction {
        NONE, OPEN, CLOSE
    }

    public enum BottomCarriagePusherAction {
        NONE, EXTEND, RETRACT
    }

    public static class Commands {
        public ElevatorMode elevator_mode;
        public PresetHeight preset_height;
        public double manual_height;
        public IntakeAction intake_action;
        public RollerAction roller_action;
        public ElevatorAction elevator_action;
        public TopCarriagePivotAction top_carriage_pivot_action;
        public TopCarriageClawAction top_carriage_claw_action;
        public BottomCarriageFlapperAction bottom_carriage_flapper_action;
        public BottomCarriagePusherAction bottom_carriage_pusher_action;
    }

    private ElevatorMode m_current_mode = ElevatorMode.BOTTOM_CARRIAGE_MODE;

    public void update(Commands commands) {
        boolean wants_mode_changed = false;
        if (commands.elevator_mode != m_current_mode) {
            m_current_mode = commands.elevator_mode;
            wants_mode_changed = true;
        }
        boolean can_close_intake = true;
        boolean can_control_top_carriage_pivot = true;
        boolean can_control_top_carriage_grabber = true;
        boolean can_control_bottom_carriage = true;
        if (m_current_mode == ElevatorMode.BOTTOM_CARRIAGE_MODE) {
            if (commands.elevator_action == ElevatorAction.HP_LOAD_ACTION) {
                // TODO
            } else if (commands.elevator_action == ElevatorAction.FLOOR_LOAD_ACTION) {
                // TODO
            } else if (commands.elevator_action == ElevatorAction.LOAD_PREP_ACTION) {
                // TODO
            } else {
                // TODO
            }
        } else { /* if (m_current_mode == ElevatorMode.TOP_CARRIAGE_MODE) */
            if (commands.elevator_action == ElevatorAction.FLOOR_LOAD_ACTION) {
                // TODO
            } else if (commands.elevator_action == ElevatorAction.LOAD_PREP_ACTION) {
                // TODO
            } else {
                // TODO
            }
        }

        // Top carriage actions.
        if (can_control_top_carriage_grabber
                && commands.top_carriage_claw_action == TopCarriageClawAction.OPEN) {
            top_carriage.setGrabberOpen(true);
        } else if (can_control_top_carriage_grabber
                && commands.top_carriage_claw_action == TopCarriageClawAction.CLOSE) {
            top_carriage.setGrabberOpen(false);
        }
        if (can_control_top_carriage_pivot
                && commands.top_carriage_pivot_action == TopCarriagePivotAction.PIVOT_DOWN) {
            top_carriage.setPivotDown(true);
        } else if (can_control_top_carriage_pivot
                && commands.top_carriage_pivot_action == TopCarriagePivotAction.PIVOT_UP) {
            top_carriage.setPivotDown(false);
        }

        // Bottom carriage actions.
        if (can_control_bottom_carriage
                && commands.bottom_carriage_flapper_action == BottomCarriageFlapperAction.OPEN) {
            bottom_carriage.setFlapperOpen(true);
        } else if (can_control_bottom_carriage
                && commands.bottom_carriage_flapper_action == BottomCarriageFlapperAction.CLOSE) {
            bottom_carriage.setFlapperOpen(false);
        }
        if (can_control_bottom_carriage
                && commands.bottom_carriage_pusher_action == BottomCarriagePusherAction.EXTEND) {
            bottom_carriage.setPusherExtended(true);
        } else if (can_control_bottom_carriage
                && commands.bottom_carriage_pusher_action == BottomCarriagePusherAction.RETRACT) {
            bottom_carriage.setPusherExtended(false);
        }

        // Intake actions.
        if (!can_close_intake || commands.intake_action == IntakeAction.OPEN) {
            // Open intake
            intake.open();
        } else if (commands.intake_action == IntakeAction.CLOSE) {
            // Close intake
            intake.close();
        }

        // Roller actions.
        if (commands.roller_action == RollerAction.INTAKE) {
            // Run intake inwards.
            intake.setSpeed(1.0);
        } else if (commands.roller_action == RollerAction.EXHAUST) {
            // Run intake outwards.
            intake.setSpeed(-1.0);
        } else {
            // Stop intake.
            intake.setSpeed(0.0);
        }
    }
}
