package com.team254.frc2015.behavior.routines;

import com.team254.frc2015.Constants;
import com.team254.frc2015.behavior.Commands;
import com.team254.frc2015.behavior.RobotSetpoints;
import edu.wpi.first.wpilibj.Timer;

import java.util.Optional;

/**
 * Created by tombot on 2/26/15.
 */
public class HorizontalCanPickupRoutine extends Routine {

    public enum States {
        START, OPENING_FLAPS, MOVE_CARRIAGES, WAIT_FOR_CAN, GET_CAN, CLOSE_GRABBER, DRIVE_UP, ROTATE_UP, DONE
    }

    States m_state = States.START;
    private boolean m_is_new_state = false;
    Timer m_state_timer = new Timer();
    RobotSetpoints setpoints;

    @Override
    public void reset() {
        m_state_timer.start();
        m_state_timer.reset();
        m_state = States.START;
    }

    @Override
    public RobotSetpoints update(Commands commands, RobotSetpoints existing_setpoints) {
        setpoints = existing_setpoints;
        States new_state = m_state;

        // Set defaults so manual control can't kick us out
        setpoints.flapper_action = RobotSetpoints.BottomCarriageFlapperAction.OPEN;
        setpoints.intake_action = RobotSetpoints.IntakeAction.PREFER_OPEN;
        setpoints.claw_action = RobotSetpoints.TopCarriageClawAction.CLOSE;
        setpoints.pinball_wizard_action = RobotSetpoints.PinballWizardAction.EXTEND;

        // Do state machine
        switch(m_state) {
            case START:
                new_state = States.OPENING_FLAPS;
                break;
            case OPENING_FLAPS:
                setpoints.pivot_action = RobotSetpoints.TopCarriagePivotAction.PIVOT_UP;
                setpoints.claw_action = RobotSetpoints.TopCarriageClawAction.CLOSE;
                setpoints.intake_action = RobotSetpoints.IntakeAction.OPEN;
                if (m_state_timer.get() > .125) {
                    new_state = States.MOVE_CARRIAGES;
                }
                break;
            case MOVE_CARRIAGES:
                setpoints.roller_action = RobotSetpoints.RollerAction.INTAKE_CAN;
                setpoints.pivot_action = RobotSetpoints.TopCarriagePivotAction.PIVOT_DOWN;
                setpoints.claw_action = RobotSetpoints.TopCarriageClawAction.NEUTRAL;
                setpoints.intake_action = RobotSetpoints.IntakeAction.OPEN;
                if (m_is_new_state) {
                    setpoints.m_elevator_setpoints.bottom_setpoint = Optional.of(Constants.kCanPickupStartBottomHeight);
                    setpoints.m_elevator_setpoints.top_setpoint = Optional.of(Constants.kCanPickupStartTopHeight);
                }
                if (!m_is_new_state  && bottom_carriage.isOnTarget() && top_carriage.isOnTarget() && m_state_timer.get() > .5) {
                    new_state = States.WAIT_FOR_CAN;
                } else if (!m_is_new_state && m_state_timer.get() > 2.3) {
                    setpoints.bottom_open_loop_jog = Optional.of(0.0);
                    setpoints.top_open_loop_jog = Optional.of(0.0);
                    new_state = States.WAIT_FOR_CAN;
                }
                break;
            case WAIT_FOR_CAN:
                setpoints.roller_action = RobotSetpoints.RollerAction.INTAKE_CAN;
                setpoints.pivot_action = RobotSetpoints.TopCarriagePivotAction.PIVOT_DOWN;
                setpoints.claw_action = RobotSetpoints.TopCarriageClawAction.NEUTRAL;
                setpoints.intake_action = RobotSetpoints.IntakeAction.OPEN;
                if (commands.horizontal_can_grabber_request == Commands.HorizontalCanGrabberRequests.ACTIVATE) {
                    new_state = States.GET_CAN;
                }
                break;
            case GET_CAN:
                setpoints.pivot_action = RobotSetpoints.TopCarriagePivotAction.PIVOT_DOWN;
                setpoints.claw_action = RobotSetpoints.TopCarriageClawAction.NEUTRAL;
                setpoints.intake_action = RobotSetpoints.IntakeAction.CLOSE;
                setpoints.roller_action = RobotSetpoints.RollerAction.INTAKE_CAN_SLOW;
                if (m_is_new_state) {
                    setpoints.m_elevator_setpoints.bottom_setpoint = Optional.of(Constants.kCanPickupEndBottomHeight);
                    setpoints.m_elevator_setpoints.top_setpoint = Optional.of(Constants.kCanPickupEndTopHeight);
                }
                if (!m_is_new_state && top_carriage.isOnTarget() && bottom_carriage.isOnTarget()) {
                    new_state = States.CLOSE_GRABBER;
                }
                if (m_state_timer.get() > 2.5) {
                    new_state = States.CLOSE_GRABBER;
                }
                break;
            case CLOSE_GRABBER:
                setpoints.pivot_action = RobotSetpoints.TopCarriagePivotAction.PIVOT_DOWN;
                setpoints.claw_action = RobotSetpoints.TopCarriageClawAction.CLOSE;
                setpoints.intake_action = RobotSetpoints.IntakeAction.CLOSE;
                setpoints.roller_action = RobotSetpoints.RollerAction.INTAKE_CAN_SLOW;
                if (m_state_timer.get() > .3) {
                    new_state = States.ROTATE_UP;
                }
                break;
            case DRIVE_UP:
                setpoints.pivot_action = RobotSetpoints.TopCarriagePivotAction.PIVOT_UP;
                setpoints.claw_action = RobotSetpoints.TopCarriageClawAction.CLOSE;
                setpoints.intake_action = RobotSetpoints.IntakeAction.OPEN;
                if (m_is_new_state) {
                    setpoints.m_elevator_setpoints.top_setpoint = Optional.of(35.0);
                }
                if (!m_is_new_state && top_carriage.getHeight() > 25.0) {
                    new_state = States.DONE;
                }
                break;
            case ROTATE_UP:
                setpoints.pivot_action = RobotSetpoints.TopCarriagePivotAction.PIVOT_UP;
                setpoints.claw_action = RobotSetpoints.TopCarriageClawAction.CLOSE;
                setpoints.intake_action = RobotSetpoints.IntakeAction.OPEN;
                if (m_state_timer.get() > .4) {
                    new_state = States.DRIVE_UP;
                }
            default:
                break;
        }

        if (commands.roller_request == Commands.RollerRequest.INTAKE && m_state != States.GET_CAN) {
            setpoints.roller_action = RobotSetpoints.RollerAction.STOP;
        } else if (commands.roller_request == Commands.RollerRequest.EXHAUST && m_state != States.GET_CAN) {
            setpoints.roller_action = RobotSetpoints.RollerAction.EXHAUST;
        }

        if (setpoints.intake_action == RobotSetpoints.IntakeAction.OPEN && commands.intake_request == Commands.IntakeRequest.OPEN) {
            setpoints.intake_action = RobotSetpoints.IntakeAction.CLOSE;
        }


        m_is_new_state = false;
        if (new_state != m_state) {
            m_state = new_state;
            m_state_timer.reset();
            m_is_new_state = true;
        }
        return setpoints;
    }

    @Override
    public void cancel() {
        m_state = States.START;
        m_state_timer.stop();
    }

    @Override
    public boolean isFinished() {
        return m_state == States.DONE;
    }

    @Override
    public String getName() {
        return "Can Grab";
    }
}
