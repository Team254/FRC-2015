package com.team254.frc2015.behavior;

import java.util.Optional;

public class Commands {
    public enum ElevatorMode {
        BOTTOM_CARRIAGE_MODE, TOP_CARRIAGE_MODE
    }

    public enum PresetRequest {
        NONE, MANUAL, CARRY_SQUEZE, SCORE, COOP
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

    public enum HorizontalCanGrabberRequests {
        NONE, ACTIVATE
    }

    public enum VerticalCanGrabberRequests {
        NONE, ACTIVATE
    }

    public enum MotorPeacockRequests {
        NONE, MOVE_UP, MOVE_DOWN
    }

    public ElevatorMode elevator_mode;
    public PresetRequest preset_request;
    public Optional<Double> top_jog = Optional.empty();
    public Optional<Double> bottom_jog = Optional.empty();
    public IntakeRequest intake_request;
    public RollerRequest roller_request;
    public TopCarriageClawRequest top_carriage_claw_request;
    public TopCarriagePivotRequest top_carriage_pivot_request;
    public BottomCarriageFlapperRequest bottom_carriage_flapper_request;
    public HorizontalCanGrabberRequests horizontal_can_grabber_request;
    public VerticalCanGrabberRequests vertical_can_grab_request;
    public MotorPeacockRequests left_motor_peacock_requests;
    public MotorPeacockRequests right_motor_peacock_requests;
    public boolean floor_load_mode = false;
    public boolean cancel_current_routine = false;
    public boolean deploy_peacock = false;
}
