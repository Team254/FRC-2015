package com.team254.frc2015;

public class BehaviorManager {
    public enum ElevatorMode {
        NONE, HUMAN_LOAD_TOTES, FLOOR_LOAD_TOTES, FLOOR_LOAD_CANS, SCORE_CAN, SCORE_STACK
    }

    public enum PresetHeight {
        NONE, ONE, TWO, THREE, FOUR, FIVE, SIX
    }

    public enum IntakeAction {
        NONE, OPEN, CLOSE, AUTO_CLOSE
    }

    public enum RollerAction {
        NONE, INTAKE, EXHAUST, AUTO_INTAKE
    }

    public enum ElevatorAction {
        NONE, INDEX, AUTO_INDEX
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

    public class Commands {
        public ElevatorMode elevator_mode;
        public PresetHeight preset_height;
        public double manual_height_modifier;
        public IntakeAction intake_action;
        public RollerAction roller_action;
        public ElevatorAction elevator_action;
        public TopCarriagePivotAction top_carriage_pivot_action;
        public TopCarriageClawAction top_carriage_claw_action;
        public BottomCarriageFlapperAction bottom_carriage_flapper_action;
        public BottomCarriagePusherAction bottom_carriage_pusher_action;
    }

    private ElevatorMode m_current_mode = ElevatorMode.HUMAN_LOAD_TOTES;

    public void update(Commands commands) {
        if (m_current_mode == ElevatorMode.HUMAN_LOAD_TOTES) {

        } else if (m_current_mode == ElevatorMode.FLOOR_LOAD_TOTES) {

        } else if (m_current_mode == ElevatorMode.FLOOR_LOAD_CANS) {

        } else if (m_current_mode == ElevatorMode.SCORE_CAN) {

        } else if (m_current_mode == ElevatorMode.SCORE_STACK) {

        } else {
            // NONE
        }
    }
}
