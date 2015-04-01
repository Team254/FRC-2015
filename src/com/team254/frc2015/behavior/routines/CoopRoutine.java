package com.team254.frc2015.behavior.routines;

import com.team254.frc2015.Constants;
import com.team254.frc2015.behavior.Commands;
import com.team254.frc2015.behavior.RobotSetpoints;
import edu.wpi.first.wpilibj.Timer;

import java.util.Optional;

/**
 * Created by tombot on 3/3/15.
 */
public class CoopRoutine extends Routine {
    public enum States {
        START, RAISE_TO_HEIGHT, EXHAUST_ME_MAYBE, PUSH_OUT, OPEN_AND_WAIT
    }

    public States m_state = States.START;
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
        RobotSetpoints setpoints = existing_setpoints;
        States new_state = m_state;

        switch (m_state) {
            case START:
                new_state = States.RAISE_TO_HEIGHT;
                break;
            case RAISE_TO_HEIGHT:
                if (m_is_new_state) {
                    setpoints.m_elevator_setpoints.bottom_setpoint = Optional.of(Constants.kCoopBottomHeight);
                    setpoints.m_elevator_setpoints.top_setpoint = Optional.of(Constants.kCoopTopHeight);
                }
                if (!m_is_new_state && bottom_carriage.isOnTarget() && top_carriage.isOnTarget()) {
                    new_state = States.EXHAUST_ME_MAYBE;
                }
                break;
            case EXHAUST_ME_MAYBE:
                setpoints.intake_action = RobotSetpoints.IntakeAction.PREFER_OPEN;
                if (commands.roller_request == Commands.RollerRequest.EXHAUST) {
                    new_state = States.PUSH_OUT;
                }
                break;
            case PUSH_OUT:
                setpoints.roller_action = RobotSetpoints.RollerAction.EXHAUST_COOP;
                setpoints.intake_action = RobotSetpoints.IntakeAction.CLOSE;
                setpoints.coop_pusher_action = RobotSetpoints.CoopPusherAction.EXTEND;

                if (m_state_timer.get() > .75 && commands.roller_request != Commands.RollerRequest.EXHAUST) {
                    new_state = States.OPEN_AND_WAIT;
                }
                break;
            case OPEN_AND_WAIT:
                setpoints.intake_action = RobotSetpoints.IntakeAction.OPEN;
                setpoints.roller_action = RobotSetpoints.RollerAction.STOP;
                if (commands.roller_request == Commands.RollerRequest.EXHAUST) {
                    setpoints.roller_action = RobotSetpoints.RollerAction.EXHAUST_COOP;
                    setpoints.intake_action = RobotSetpoints.IntakeAction.CLOSE;
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

    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public String getName() {
        return "Co-Op";
    }
}
