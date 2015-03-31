package com.team254.frc2015.behavior;

import com.team254.frc2015.ElevatorSafety;

import java.util.Optional;

/**
 * Created by tombot on 2/26/15.
 */
public class RobotSetpoints {

    public enum IntakeAction {
        NONE, OPEN, CLOSE, PREFER_OPEN, PREFER_CLOSE
    }

    public enum RollerAction {
        NONE, INTAKE, EXHAUST, EXHAUST_COOP, STOP, INTAKE_CAN, INTAKE_CAN_SLOW
    }

    public enum TopCarriagePivotAction {
        NONE, PIVOT_DOWN, PIVOT_UP
    }

    public enum TopCarriageClawAction {
        NONE, OPEN, CLOSE, PREFER_CLOSE, NEUTRAL
    }

    public enum BottomCarriageFlapperAction {
        NONE, OPEN, CLOSE
    }

    public enum CoopPusherAction {
        EXTEND, RETRACT
    }

    public enum PinballWizardAction {
        STOW, EXTEND
    }

    public static final Optional<Double> m_nullopt = Optional.empty();

    public ElevatorSafety.Setpoints m_elevator_setpoints = new ElevatorSafety.Setpoints();
    public BottomCarriageFlapperAction flapper_action;
    public TopCarriageClawAction claw_action;
    public TopCarriagePivotAction pivot_action;
    public IntakeAction intake_action;
    public RollerAction roller_action;
    public PinballWizardAction pinball_wizard_action;
    public CoopPusherAction coop_pusher_action;
    public Optional<Double> top_open_loop_jog;
    public Optional<Double> bottom_open_loop_jog;
    public boolean top_carriage_squeeze;

    public void reset() {
        m_elevator_setpoints.bottom_setpoint = m_nullopt;
        m_elevator_setpoints.top_setpoint = m_nullopt;
        claw_action = TopCarriageClawAction.NONE;
        pivot_action = TopCarriagePivotAction.PIVOT_UP;
        flapper_action =  BottomCarriageFlapperAction.NONE;
        intake_action = IntakeAction.NONE;
        roller_action = RollerAction.NONE;
        coop_pusher_action = CoopPusherAction.RETRACT;
        pinball_wizard_action = PinballWizardAction.STOW;
        top_open_loop_jog = m_nullopt;
        bottom_open_loop_jog = m_nullopt;
        top_carriage_squeeze = false;
    }
}
