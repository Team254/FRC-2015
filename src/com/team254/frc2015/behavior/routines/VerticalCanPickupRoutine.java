package com.team254.frc2015.behavior.routines;

import com.team254.frc2015.behavior.Commands;
import com.team254.frc2015.behavior.RobotSetpoints;
import edu.wpi.first.wpilibj.Timer;

import java.util.Optional;

/**
 * Created by tombot on 3/30/15.
 */
public class VerticalCanPickupRoutine extends Routine {

    public enum States {
        MOVE_TO_POSITION, CLOSE, MOVE_UP, DONE
    }

    private States m_state = States.MOVE_TO_POSITION;
    private boolean m_is_new_state = true;
    Timer m_state_timer = new Timer();

    @Override
    public void reset() {
        m_state = States.MOVE_TO_POSITION;
        m_is_new_state = true;
        m_state_timer.start();
        m_state_timer.reset();
    }

    @Override
    public RobotSetpoints update(Commands commands, RobotSetpoints setpoints) {
        States new_state = m_state;
        switch (m_state) {
            case MOVE_TO_POSITION:
                setpoints.claw_action = RobotSetpoints.TopCarriageClawAction.OPEN;
                setpoints.intake_action = RobotSetpoints.IntakeAction.CLOSE;
                if (commands.intake_request == Commands.IntakeRequest.OPEN) {
                    setpoints.intake_action = RobotSetpoints.IntakeAction.CLOSE;
                }

                if (commands.roller_request == Commands.RollerRequest.INTAKE) {
                    setpoints.roller_action = RobotSetpoints.RollerAction.NONE;
                } else if (commands.roller_request == Commands.RollerRequest.NONE) {
                    setpoints.roller_action = RobotSetpoints.RollerAction.INTAKE;
                }

                if (m_is_new_state) {
                    setpoints.m_elevator_setpoints.bottom_setpoint = Optional.of(0.);
                    setpoints.m_elevator_setpoints.top_setpoint = Optional.of(4.25);
                }
                if (m_state_timer.get() > .25 && commands.vertical_can_grab_request == Commands.VerticalCanGrabberRequests.ACTIVATE) {
                    new_state = States.CLOSE;
                }
                setpoints.pinball_wizard_action = RobotSetpoints.PinballWizardAction.EXTEND;
                break;
            case CLOSE:
                setpoints.claw_action = RobotSetpoints.TopCarriageClawAction.CLOSE;
                setpoints.intake_action = RobotSetpoints.IntakeAction.OPEN;
                if (m_state_timer.get() > .35) {
                    new_state = States.MOVE_UP;
                }
                setpoints.pinball_wizard_action = RobotSetpoints.PinballWizardAction.EXTEND;
                break;
            case MOVE_UP:
                setpoints.claw_action = RobotSetpoints.TopCarriageClawAction.CLOSE;
                setpoints.intake_action = RobotSetpoints.IntakeAction.OPEN;
                if (m_is_new_state) {
                    setpoints.m_elevator_setpoints.top_setpoint = Optional.of(40.0);
                }
                if (!m_is_new_state && (top_carriage.isOnTarget() || m_state_timer.get() > 3.0)) {
                    new_state = States.DONE;
                }
                break;
        }

        if (commands.intake_request == Commands.IntakeRequest.OPEN) {
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

    }

    @Override
    public boolean isFinished() {
        return m_state == States.DONE;
    }

    @Override
    public String getName() {
        return "Vertical Can";
    }
}
