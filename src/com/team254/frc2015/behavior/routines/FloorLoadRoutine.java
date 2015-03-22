package com.team254.frc2015.behavior.routines;

import com.team254.frc2015.behavior.Commands;
import com.team254.frc2015.behavior.RobotSetpoints;
import edu.wpi.first.wpilibj.Timer;

import java.util.Optional;

/**
 * Created by tombot on 3/21/15.
 */
public class FloorLoadRoutine extends Routine {

    public enum States {
        START, MOVE_TO_STARTING_POS, WAIT_FOR_TOTE, MOVE_DOWN, MOVE_UP, DONE
    }

    private States m_state = States.START;
    private boolean m_is_new_state = true;
    Timer m_state_timer = new Timer();
    private static final double TOTE_CLEAR_POS = 18.25;
    private static final double TOTE_GRAB_POS = 2.5;

    @Override
    public void reset() {
        m_state = States.START;
        m_is_new_state = true;
        m_state_timer.start();
        m_state_timer.reset();
    }

    @Override
    public RobotSetpoints update(Commands commands, RobotSetpoints setpoints) {
        States new_state = m_state;
        boolean do_squeeze = true;
        setpoints.roller_action = commands.roller_request == Commands.RollerRequest.INTAKE ? RobotSetpoints.RollerAction.STOP : RobotSetpoints.RollerAction.INTAKE;

        switch(m_state) {
            case START:
                new_state = States.MOVE_TO_STARTING_POS;
                do_squeeze = false;
                break;
            case MOVE_TO_STARTING_POS:
                if (m_is_new_state) {
                    setpoints.m_elevator_setpoints.bottom_setpoint = Optional.of(TOTE_CLEAR_POS);
                    setpoints.m_elevator_setpoints.top_setpoint = Optional.of(TOTE_CLEAR_POS + 5.0);
                }
                if (bottom_carriage.isOnTarget() || m_state_timer.get() > 2.0) {
                    new_state = States.WAIT_FOR_TOTE;
                }
                do_squeeze = false;
                break;
            case WAIT_FOR_TOTE:
                if (intake.getBreakbeamTriggered()) {
                    new_state = States.MOVE_DOWN;
                }
                break;
            case MOVE_DOWN:
                if (m_is_new_state) {
                    bottom_carriage.setFastPositionSetpoint(TOTE_GRAB_POS);
                }
                if (bottom_carriage.isOnTarget() || m_state_timer.get() > 2.0) {
                    new_state = States.MOVE_UP;
                }
                break;
            case MOVE_UP:
                if (m_is_new_state) {
                    bottom_carriage.setFastPositionSetpoint(TOTE_CLEAR_POS);
                }
                if (bottom_carriage.isOnTarget() || m_state_timer.get() > 2.0) {
                    new_state = States.WAIT_FOR_TOTE;
                }
                break;
            case DONE:
                if (m_is_new_state) {
                    setpoints.bottom_open_loop_jog = Optional.of(0.0);
                }
                break;
        }

        if (m_state == States.MOVE_UP && bottom_carriage.fastHitTop()) {
            new_state = States.DONE;
        }

        setpoints.top_carriage_squeeze = do_squeeze;

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
        return "Floor load";
    }
}
