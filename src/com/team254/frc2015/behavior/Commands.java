package com.team254.frc2015.behavior;

import java.util.Optional;

/**
 * Created by tombot on 2/25/15.
 */
public class Commands {
    public enum ElevatorMode {
        BOTTOM_CARRIAGE_MODE, TOP_CARRIAGE_MODE
    }

    public enum PresetRequest {
        NONE, MANUAL, CARRY_SQUEZE, FLOOR, RAMMING, SCORE, COOP
    }

    public enum IntakeRequest {
        NONE, OPEN, CLOSE, NEUTRAL
    }

    public enum RollerRequest {
        NONE, INTAKE, EXHAUST
    }

    public enum TopCarriageClawRequest {
        NONE, OPEN, CLOSE
    }

    public enum TopCarriagePivotRequest {
        NONE, PIVOT_DOWN, PIVOT_UP
    }

    public enum BottomCarriageFlapperRequest {
        NONE, OPEN, CLOSE
    }

    public enum BottomCarriagePusherRequest {
        NONE, EXTEND, RETRACT
    }


    public enum ElevatorAction {
        NONE, HP_LOAD_ACTION, FLOOR_LOAD_ACTION, LOAD_PREP_ACTION
    }

    public enum CanGrabberRequests {
        NONE, DO_STAGE, TOGGLE_GRAB
    }

    public enum HumanLoadRequests {
        NONE, LOAD_FIRST_BIN, INDEX_BIN
    }

    public ElevatorMode elevator_mode;
    public PresetRequest preset_request;
    public Optional<Double> top_jog = Optional.empty();
    public Optional<Double> bottom_jog = Optional.empty();
    public IntakeRequest intake_request;
    public RollerRequest roller_request;
    public ElevatorAction elevator_action;
    public TopCarriageClawRequest top_carriage_claw_request;
    public TopCarriagePivotRequest top_carriage_pivot_request;
    public BottomCarriageFlapperRequest bottom_carriage_flapper_request;
    public BottomCarriagePusherRequest bottom_carriage_pusher_request;
    public CanGrabberRequests can_grabber_request;
    public boolean human_player_mode = false;
    public boolean floor_load_mode = false;
    public boolean cancel_current_routine = false;
    public HumanLoadRequests human_load_requests;
}
