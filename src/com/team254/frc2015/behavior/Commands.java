package com.team254.frc2015.behavior;

/**
 * Created by tombot on 2/25/15.
 */
public class Commands {
    public enum ElevatorMode {
        BOTTOM_CARRIAGE_MODE, TOP_CARRIAGE_MODE
    }

    public enum PresetHeight {
        NONE, MANUAL, CARRY, CAPPING, CAN, FLOOR, HUMAN, HOME
    }

    public enum IntakeAction {
        NONE, OPEN, CLOSE, NEUTRAL
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

    public ElevatorMode elevator_mode;
    public PresetHeight preset_height;
    public double top_jog;
    public double bottom_jog;
    public IntakeAction intake_action;
    public RollerAction roller_action;
    public ElevatorAction elevator_action;
    public TopCarriagePivotAction top_carriage_pivot_action;
    public TopCarriageClawAction top_carriage_claw_action;
    public BottomCarriageFlapperAction bottom_carriage_flapper_action;
    public BottomCarriagePusherAction bottom_carriage_pusher_action;
}
