package com.team254.frc2015.behavior.routines;

import com.team254.frc2015.behavior.Commands;
import com.team254.frc2015.behavior.RobotSetpoints;
import edu.wpi.first.wpilibj.Timer;

import java.util.Optional;

/**
 * Created by tombot on 2/26/15.
 */
public class CanGrabRoutine extends Routine {

    public enum States {
        START, OPENING_FLAPS, MOVE_CARRIAGES, ROTATE_DOWN, OPEN_GRABBER, CLOSE_GRABBER, DRIVE_UP, ROTATE_UP, IDLE
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
        setpoints.flapper_action = RobotSetpoints.BottomCarriageFlapperAction.OPEN;
        switch(m_state) {
            case START:
                new_state = States.OPENING_FLAPS;
                break;
            case OPENING_FLAPS:
                setpoints.pivot_action = RobotSetpoints.TopCarriagePivotAction.PIVOT_UP;
                setpoints.claw_action = RobotSetpoints.TopCarriageClawAction.CLOSE;
                if (m_state_timer.get() > .125) {
                    new_state = States.IDLE.MOVE_CARRIAGES;
                }
                break;
            case MOVE_CARRIAGES:
                setpoints.pivot_action = RobotSetpoints.TopCarriagePivotAction.PIVOT_UP;
                setpoints.claw_action = RobotSetpoints.TopCarriageClawAction.CLOSE;
                if (m_is_new_state) {
                    setpoints.m_elevator_setpoints.bottom_setpoint = Optional.of(5.5);
                    setpoints.m_elevator_setpoints.top_setpoint = Optional.of(5.5);
                }
                if (bottom_carriage.isOnTarget() && top_carriage.isOnTarget()) {
                    new_state = States.IDLE.ROTATE_DOWN;
                }
                break;
            case ROTATE_DOWN:
                setpoints.pivot_action = RobotSetpoints.TopCarriagePivotAction.PIVOT_DOWN;
                setpoints.claw_action = RobotSetpoints.TopCarriageClawAction.CLOSE;
                if (m_state_timer.get() > .75) {
                    new_state = States.OPEN_GRABBER;
                }
                break;
            case OPEN_GRABBER:
                setpoints.pivot_action = RobotSetpoints.TopCarriagePivotAction.PIVOT_DOWN;
                setpoints.claw_action = RobotSetpoints.TopCarriageClawAction.OPEN;
                if (commands.can_grabber_request == Commands.CanGrabberRequests.DO_GRAB) {
                    new_state = States.IDLE;
                }
                break;
            default:
                break;
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
}
