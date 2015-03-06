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
        START, OPENING_FLAPS, MOVE_CARRIAGES, ROTATE_DOWN, OPEN_GRABBER, CLOSE_GRABBER, CENTER_DOWN, DRIVE_UP, ROTATE_UP, BEFORE_CLOSE_GRABBER, DONE
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
                setpoints.pivot_action = RobotSetpoints.TopCarriagePivotAction.PIVOT_UP;
                setpoints.claw_action = RobotSetpoints.TopCarriageClawAction.CLOSE;
                setpoints.intake_action = RobotSetpoints.IntakeAction.OPEN;
                if (m_is_new_state) {
                    setpoints.m_elevator_setpoints.bottom_setpoint = Optional.of(2.0);
                    setpoints.m_elevator_setpoints.top_setpoint = Optional.of(6.0);
                }
                if (!m_is_new_state  && bottom_carriage.isOnTarget() && top_carriage.isOnTarget()) {
                    new_state = States.ROTATE_DOWN;
                }
                break;
            case ROTATE_DOWN:
                setpoints.pivot_action = RobotSetpoints.TopCarriagePivotAction.PIVOT_DOWN;
                setpoints.claw_action = RobotSetpoints.TopCarriageClawAction.CLOSE;
                setpoints.intake_action = RobotSetpoints.IntakeAction.OPEN;
                if (m_state_timer.get() > .55) {
                    new_state = States.OPEN_GRABBER;
                }
                break;
            case OPEN_GRABBER:
                setpoints.pivot_action = RobotSetpoints.TopCarriagePivotAction.PIVOT_DOWN;
                setpoints.claw_action = RobotSetpoints.TopCarriageClawAction.OPEN;
                if (commands.can_grabber_request == Commands.CanGrabberRequests.TOGGLE_GRAB) {
                    new_state = States.BEFORE_CLOSE_GRABBER;
                }
                break;
            case BEFORE_CLOSE_GRABBER:
                setpoints.pivot_action = RobotSetpoints.TopCarriagePivotAction.PIVOT_DOWN;
                setpoints.claw_action = RobotSetpoints.TopCarriageClawAction.OPEN;
                setpoints.intake_action = RobotSetpoints.IntakeAction.OPEN;
                if (m_state_timer.get() > .125) {
                    new_state = States.CLOSE_GRABBER;
                }
                break;
            case CLOSE_GRABBER:
                setpoints.pivot_action = RobotSetpoints.TopCarriagePivotAction.PIVOT_DOWN;
                setpoints.claw_action = RobotSetpoints.TopCarriageClawAction.CLOSE;
                setpoints.intake_action = RobotSetpoints.IntakeAction.OPEN;
                if (commands.can_grabber_request == Commands.CanGrabberRequests.DO_STAGE) {
                    new_state = States.CENTER_DOWN;
                }
                if (commands.can_grabber_request == Commands.CanGrabberRequests.TOGGLE_GRAB) {
                    new_state = States.OPEN_GRABBER;
                }
                break;
            case CENTER_DOWN:
                setpoints.roller_action = RobotSetpoints.RollerAction.INTAKE;
                setpoints.pivot_action = RobotSetpoints.TopCarriagePivotAction.PIVOT_DOWN;
                setpoints.claw_action = RobotSetpoints.TopCarriageClawAction.CLOSE;
                setpoints.intake_action = RobotSetpoints.IntakeAction.OPEN;
                if (m_is_new_state) {
                    setpoints.m_elevator_setpoints.top_setpoint = Optional.of(5.0);
                }
                if (m_state_timer.get() > .35) {
                    new_state = States.DRIVE_UP;
                }
                break;
            case DRIVE_UP:
                setpoints.roller_action = RobotSetpoints.RollerAction.INTAKE;
                setpoints.pivot_action = RobotSetpoints.TopCarriagePivotAction.PIVOT_DOWN;
                setpoints.claw_action = RobotSetpoints.TopCarriageClawAction.CLOSE;
                setpoints.intake_action = RobotSetpoints.IntakeAction.OPEN;
                if (m_is_new_state) {
                    setpoints.m_elevator_setpoints.top_setpoint = Optional.of(55.0);
                }
                if (!m_is_new_state && top_carriage.getHeight() > 40.0) {
                    new_state = States.ROTATE_UP;
                }
                break;
            case ROTATE_UP:
                setpoints.pivot_action = RobotSetpoints.TopCarriagePivotAction.PIVOT_UP;
                setpoints.claw_action = RobotSetpoints.TopCarriageClawAction.CLOSE;
                if (m_state_timer.get() > .125 && top_carriage.isOnTarget()) {
                    new_state = States.DONE;
                }
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

    @Override
    public boolean isFinished() {
        return m_state == States.DONE;
    }

    @Override
    public String getName() {
        return "Can Grab";
    }
}
