package com.team254.frc2015.behavior.routines;

import com.team254.frc2015.Constants;
import com.team254.frc2015.behavior.Commands;
import com.team254.frc2015.behavior.RobotSetpoints;
import edu.wpi.first.wpilibj.Timer;

import java.util.Optional;

/**
 * Created by tombot on 2/27/15.
 */
public class HumanLoadRoutine extends Routine {
    public enum States {
        IDLE, FIRST_BIN_LOCATION, GRAB_BIN_FROM_BELOW, GRAB_BIN_FROM_BELOW_MOVE, GRAB_BIN_FROM_ABOVE, CLOSE_THEN_RAISE_BIN_OUT_OF_WAY, RAISE_BIN_OUT_OF_WAY, PUSH_STACK_UP
    }
    States m_state = States.IDLE;
    private boolean m_is_new_state = false;
    Timer m_state_timer = new Timer();

    final static double GROUND_LOCATION = 0.5;
    final static double GRAB_SECOND_BIN_LOCATION = 13.0;
    final static double OUT_OF_WAY_LOCATION = 33.0;

    @Override
    public void reset() {
        m_state_timer.start();
        m_state_timer.reset();
        m_state = States.IDLE;
    }

    @Override
    public RobotSetpoints update(Commands commands, RobotSetpoints existing_setpoints) {
        RobotSetpoints setpoints = existing_setpoints;

        States new_state = m_state;

        // Always run roller in and vent intake
        setpoints.roller_action = RobotSetpoints.RollerAction.INTAKE;
        setpoints.intake_action = RobotSetpoints.IntakeAction.PREFER_NEUTRAL_HP;

        switch (m_state) {
            case IDLE:
                if (commands.human_load_requests == Commands.HumanLoadRequests.LOAD_FIRST_BIN) {
                    new_state = States.FIRST_BIN_LOCATION;
                } else if (commands.human_load_requests == Commands.HumanLoadRequests.INDEX_BIN) {
                    if (bottom_carriage.getHeight() < GRAB_SECOND_BIN_LOCATION) {
                        new_state = States.GRAB_BIN_FROM_BELOW;
                    } else {
                        new_state = States.GRAB_BIN_FROM_ABOVE;
                    }
                }
                break;
            case FIRST_BIN_LOCATION:
                setpoints.flapper_action = RobotSetpoints.BottomCarriageFlapperAction.CLOSE;
                if (m_is_new_state) {
                    setpoints.m_elevator_setpoints.bottom_setpoint = Optional.of(GROUND_LOCATION);
                    setpoints.m_elevator_setpoints.top_setpoint = Optional.of(44.); // Noodle location
                }
                if (!m_is_new_state  && bottom_carriage.isOnTarget() && top_carriage.isOnTarget()) {
                    new_state = States.IDLE;
                }
                break;
            case GRAB_BIN_FROM_BELOW:
                setpoints.flapper_action = RobotSetpoints.BottomCarriageFlapperAction.OPEN;
                if (m_state_timer.get() > .3) {
                    new_state = States.GRAB_BIN_FROM_BELOW_MOVE;
                }
                break;
            case GRAB_BIN_FROM_BELOW_MOVE:
                setpoints.flapper_action = RobotSetpoints.BottomCarriageFlapperAction.OPEN;
                if (m_is_new_state) {
                    setpoints.m_elevator_setpoints.bottom_setpoint = Optional.of(GRAB_SECOND_BIN_LOCATION);
                    setpoints.m_elevator_setpoints.top_setpoint = Optional.of(Constants.kTopCarriageMaxPositionInches);
                }
                if (!m_is_new_state  && bottom_carriage.isOnTarget()) {
                    new_state = States.CLOSE_THEN_RAISE_BIN_OUT_OF_WAY;
                }
                break;
            case GRAB_BIN_FROM_ABOVE:
                setpoints.flapper_action = RobotSetpoints.BottomCarriageFlapperAction.CLOSE;
                if (m_is_new_state) {
                    setpoints.m_elevator_setpoints.bottom_setpoint = Optional.of(GRAB_SECOND_BIN_LOCATION);
                    setpoints.m_elevator_setpoints.top_setpoint = Optional.of(Constants.kTopCarriageMaxPositionInches);
                }
                if (!m_is_new_state  && bottom_carriage.isOnTarget()) {
                    new_state = States.RAISE_BIN_OUT_OF_WAY;
                }
                break;
            case CLOSE_THEN_RAISE_BIN_OUT_OF_WAY:
                setpoints.flapper_action = RobotSetpoints.BottomCarriageFlapperAction.CLOSE;
                if (m_state_timer.get() > .25) {
                    new_state = States.RAISE_BIN_OUT_OF_WAY;
                }
                break;
            case RAISE_BIN_OUT_OF_WAY:
                setpoints.flapper_action = RobotSetpoints.BottomCarriageFlapperAction.CLOSE;
                if (m_is_new_state) {
                    setpoints.m_elevator_setpoints.bottom_setpoint = Optional.of(OUT_OF_WAY_LOCATION);
                    setpoints.m_elevator_setpoints.top_setpoint = Optional.of(Constants.kTopCarriageMaxPositionInches);
                }
                if (top_carriage.getBreakbeamTriggered()) {
                    new_state = States.PUSH_STACK_UP;
                }
                if (!m_is_new_state  && bottom_carriage.isOnTarget()) {
                    new_state = States.IDLE;
                }
                break;
            case PUSH_STACK_UP:
                setpoints.bottom_open_loop_jog  = Optional.of(.8);
                setpoints.top_open_loop_jog  = Optional.of(.5);
                if (m_state_timer.get() > .5) {
                    setpoints.bottom_open_loop_jog  = Optional.empty();
                    setpoints.top_open_loop_jog  = Optional.empty();
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

    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public String getName() {
        return "HP Load";
    }
}
