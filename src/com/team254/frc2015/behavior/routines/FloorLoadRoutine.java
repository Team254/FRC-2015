package com.team254.frc2015.behavior.routines;

import com.team254.frc2015.behavior.Commands;
import com.team254.frc2015.behavior.RobotSetpoints;
import com.team254.lib.util.Latch;
import com.team254.lib.util.TimeDelayedBoolean;
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
    private static final double TOTE_CLEAR_POS = 17.75;
    private static final double REGRASP_POS = 10;
    private static final double TOTE_GRAB_POS = 1.5;
    private boolean m_moved_down_once = false;
    private Latch m_top_carriage_init_latch = new Latch();
    private TimeDelayedBoolean m_stalled_motor = new TimeDelayedBoolean();

    @Override
    public void reset() {
        m_state = States.START;
        m_is_new_state = true;
        m_state_timer.start();
        m_state_timer.reset();
        m_moved_down_once = false;
    }

    @Override
    public RobotSetpoints update(Commands commands, RobotSetpoints setpoints) {
        States new_state = m_state;
        boolean do_squeeze = true;
        if (m_state != States.START && m_state != States.MOVE_TO_STARTING_POS) {
            setpoints.intake_action = RobotSetpoints.IntakeAction.CLOSE;
        }
        if (commands.roller_request == Commands.RollerRequest.INTAKE) {
            setpoints.roller_action = RobotSetpoints.RollerAction.STOP;
        } else if (commands.roller_request == Commands.RollerRequest.EXHAUST) {
            setpoints.roller_action = RobotSetpoints.RollerAction.EXHAUST;
        } else {
            setpoints.roller_action = RobotSetpoints.RollerAction.INTAKE;
        }
        boolean should_vent = false;

        switch (m_state) {
            case START:
                new_state = States.MOVE_TO_STARTING_POS;
                do_squeeze = false;
                break;
            case MOVE_TO_STARTING_POS:
                if (m_is_new_state) {
                    setpoints.m_elevator_setpoints.bottom_setpoint = Optional.of(TOTE_CLEAR_POS);
                    setpoints.m_elevator_setpoints.top_setpoint = Optional.of(TOTE_CLEAR_POS + 12.0);
                }
                if (!m_is_new_state && bottom_carriage.isOnTarget() || m_state_timer.get() > 2.0) {
                    new_state = States.WAIT_FOR_TOTE;
                }
                if (m_top_carriage_init_latch.update(top_carriage.getBreakbeamTriggered())) {
                    //setpoints.m_elevator_setpoints.top_setpoint = Optional.empty();
                    m_moved_down_once = true;
                }
                do_squeeze = m_moved_down_once;
                break;
            case WAIT_FOR_TOTE:
                do_squeeze = m_moved_down_once;
                if (intake.getBreakbeamTriggered()) {
                    new_state = States.MOVE_DOWN;
                }
                break;
            case MOVE_DOWN:
                if (m_is_new_state) {
                    bottom_carriage.setFastPositionSetpoint(TOTE_GRAB_POS);
                }
                if (m_moved_down_once && (bottom_carriage.isOnTarget() || m_state_timer.get() > 2.0)) {
                    new_state = States.MOVE_UP;
                }
                if (!m_moved_down_once && bottom_carriage.isOnTarget() && (top_carriage.getHeight() < 5.25 || m_state_timer.get() > .8)) {
                    new_state = States.MOVE_UP;
                }
                should_vent = (!m_moved_down_once && top_carriage.getHeight() < 10);
                break;
            case MOVE_UP:
                if (m_is_new_state) {
                    bottom_carriage.setFastPositionSetpoint(TOTE_CLEAR_POS);
                }
                if (bottom_carriage.isOnTarget() || m_state_timer.get() > 2.0) {
                    m_moved_down_once = true;
                    new_state = States.WAIT_FOR_TOTE;
                }
                if (top_carriage.getHeight() - bottom_carriage.getHeight() < 1.9 && !top_carriage.getBreakbeamTriggered()) {
                    new_state = States.MOVE_TO_STARTING_POS;
                }
                should_vent = !m_moved_down_once && (bottom_carriage.getHeight() < REGRASP_POS);
                break;
            case DONE:
                if (m_is_new_state) {
                    setpoints.bottom_open_loop_jog = Optional.of(0.0);
                }
                // Just in case
                if (bottom_carriage.getHeight() >= (TOTE_CLEAR_POS - 5.0) && intake.getBreakbeamTriggered()) {
                    new_state = States.MOVE_DOWN;
                }
                break;
        }

        if (m_state != States.MOVE_TO_STARTING_POS) {
            m_top_carriage_init_latch.update(false);
        }

        if (m_state == States.MOVE_UP && bottom_carriage.fastHitTop()) {
            System.out.println("Hit top - floor load, moving to DONE");
            new_state = States.DONE;
        }

        if (m_stalled_motor.update(m_state == States.MOVE_UP && bottom_carriage.getEncoderVelocity() < 8.0, 1.5)) {
            System.out.println("Stalled motor!");
            new_state = States.DONE;
        }


        setpoints.top_carriage_squeeze = do_squeeze;
        setpoints.claw_action = should_vent ? RobotSetpoints.TopCarriageClawAction.NEUTRAL : RobotSetpoints.TopCarriageClawAction.CLOSE;

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
