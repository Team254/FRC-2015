package com.team254.frc2015.behavior;

import com.team254.frc2015.Constants;
import com.team254.frc2015.ElevatorSafety;
import com.team254.frc2015.HardwareAdaptor;
import com.team254.frc2015.behavior.routines.CanGrabRoutine;
import com.team254.frc2015.behavior.routines.ManualRoutine;
import com.team254.frc2015.behavior.routines.Routine;
import com.team254.frc2015.subsystems.BottomCarriage;
import com.team254.frc2015.subsystems.Drive;
import com.team254.frc2015.subsystems.Intake;
import com.team254.frc2015.subsystems.TopCarriage;

public class BehaviorManager {

    public boolean isZero(double val) {
        return val < 0.0001 && val > -0.0001;
    }
    protected Drive drive = HardwareAdaptor.kDrive;
    protected TopCarriage top_carriage = HardwareAdaptor.kTopCarriage;
    protected BottomCarriage bottom_carriage = HardwareAdaptor.kBottomCarriage;
    protected Intake intake = HardwareAdaptor.kIntake;

    private ElevatorSafety.Setpoints m_elevator_setpoints = new ElevatorSafety.Setpoints();
    private boolean m_top_jogging = false;
    private boolean m_bottom_jogging = false;


    private Routine m_cur_routine = null;
    private RobotSetpoints m_setpoints;
    private ManualRoutine m_manual_routine = new ManualRoutine();

    private void setNewRoutine(Routine new_routine) {
        boolean needs_cancel = new_routine != m_cur_routine && m_cur_routine != null;
        boolean needs_reset = new_routine != m_cur_routine && new_routine != null;
        if (needs_cancel) {
            m_cur_routine.cancel();
        }
        m_cur_routine = new_routine;
        if (needs_reset) {
            m_cur_routine.reset();
        }
    }

    public void reset() {
        setNewRoutine(null);
    }

    public BehaviorManager() {
        m_setpoints = new RobotSetpoints();
        m_setpoints.reset();
    }
    public void update(Commands commands) {
        m_setpoints.reset();


        if (m_cur_routine != null && m_cur_routine.isFinished()) {
            setNewRoutine(null);
        }

        if (commands.cancel_current_routine) {
            setNewRoutine(null);
        } else if (commands.can_grabber_request == Commands.CanGrabberRequests.STAGE_FOR_GRAB && !(m_cur_routine instanceof CanGrabRoutine)) {
            setNewRoutine(new CanGrabRoutine());
        }

        if (m_cur_routine != null) {
            m_setpoints = m_cur_routine.update(commands, m_setpoints);
        }

        // Get manual m_setpoints
        m_setpoints = m_manual_routine.update(commands, m_setpoints);
        
        boolean can_close_intake = true;
        boolean can_control_top_carriage_pivot = true;
        boolean can_control_top_carriage_grabber = true;
        boolean can_control_bottom_carriage = true;


        // Set elevator m_setpoints and jog
        if (bottom_carriage.isInitialized()) {
            if (m_setpoints.bottom_open_loop_jog.isPresent()) {
                double jog = m_setpoints.bottom_open_loop_jog.get();
                bottom_carriage.setOpenLoop(jog, isZero(jog));
                m_bottom_jogging = true;
            } else if (m_setpoints.m_elevator_setpoints.bottom_setpoint.isPresent()) {
                bottom_carriage.setPositionSetpoint(m_setpoints.m_elevator_setpoints.bottom_setpoint.get(), true);
                m_bottom_jogging = false;
            } else if (m_bottom_jogging && !m_setpoints.bottom_open_loop_jog.isPresent()) {
                bottom_carriage.setOpenLoop(0, true);
                m_bottom_jogging = false;
            }
        }
        if (top_carriage.isInitialized()) {
            if (m_setpoints.top_open_loop_jog.isPresent()) {
                double jog = m_setpoints.top_open_loop_jog.get();
                top_carriage.setOpenLoop(jog, isZero(jog));
                m_top_jogging = true;
            } else if (m_setpoints.m_elevator_setpoints.top_setpoint.isPresent()) {
                top_carriage.setPositionSetpoint(m_setpoints.m_elevator_setpoints.top_setpoint.get(), true);
                m_top_jogging = false;
            } else if (m_top_jogging && !m_setpoints.top_open_loop_jog.isPresent()) {
                top_carriage.setOpenLoop(0, true);
                m_top_jogging = false;
            }
            
        }

        m_elevator_setpoints = ElevatorSafety.generateSafeSetpoints(m_setpoints.m_elevator_setpoints);
        if (m_elevator_setpoints.top_setpoint.isPresent() && !m_top_jogging && !m_bottom_jogging) {
            top_carriage.setPositionSetpoint(m_elevator_setpoints.top_setpoint.get(), true);
        }
        if (m_elevator_setpoints.bottom_setpoint.isPresent() && !m_bottom_jogging && !m_bottom_jogging) {
            bottom_carriage.setPositionSetpoint(m_elevator_setpoints.bottom_setpoint.get(), true);
        }

        // can_close_intake = ElevatorSafety.canCloseIntake();

        // Top carriage actions.
        if (can_control_top_carriage_grabber
                && m_setpoints.claw_action == RobotSetpoints.TopCarriageClawAction.OPEN) {
            top_carriage.setGrabberOpen(true);
            can_control_top_carriage_pivot = false;
        } else if (can_control_top_carriage_grabber
                && m_setpoints.claw_action == RobotSetpoints.TopCarriageClawAction.CLOSE) {
            top_carriage.setGrabberOpen(false);
        }

        if (can_control_top_carriage_pivot
                && m_setpoints.pivot_action == RobotSetpoints.TopCarriagePivotAction.PIVOT_DOWN) {
            top_carriage.setPivotDown(true);
        } else if (can_control_top_carriage_pivot
                && m_setpoints.pivot_action == RobotSetpoints.TopCarriagePivotAction.PIVOT_UP) {
            top_carriage.setPivotDown(false);
        }

        // Bottom carriage actions.
        if (can_control_bottom_carriage
                && m_setpoints.flapper_action == RobotSetpoints.BottomCarriageFlapperAction.OPEN) {
            bottom_carriage.setFlapperOpen(true);
        } else if (can_control_bottom_carriage
                && m_setpoints.flapper_action == RobotSetpoints.BottomCarriageFlapperAction.CLOSE) {
            bottom_carriage.setFlapperOpen(false);
        }

        /*
        if (can_control_bottom_carriage
                && m_setpoints.bottom_carriage_pusher_action == Commands.BottomCarriagePusherRequest.EXTEND) {
            bottom_carriage.setPusherExtended(true);
        } else if (can_control_bottom_carriage
                && m_setpoints.bottom_carriage_pusher_action == Commands.BottomCarriagePusherRequest.RETRACT) {
            bottom_carriage.setPusherExtended(false);
        }
        */

        // Intake actions.
        if (!can_close_intake || m_setpoints.intake_action == RobotSetpoints.IntakeAction.OPEN || m_setpoints.intake_action == RobotSetpoints.IntakeAction.PREFER_OPEN) {
            // Open intake
            intake.open();
        } else if (m_setpoints.intake_action == RobotSetpoints.IntakeAction.CLOSE || m_setpoints.intake_action == RobotSetpoints.IntakeAction.PREFER_CLOSE) {
            // Close intake
            intake.close();
        }  else if (m_setpoints.intake_action == RobotSetpoints.IntakeAction.NEUTRAL) {
            // Neutral intake
            intake.neutral();
        }

        // Roller actions.
        if (m_setpoints.roller_action == RobotSetpoints.RollerAction.INTAKE) {
            // Run intake inwards.
            intake.setSpeed(Constants.kManualIntakeSpeed);
        } else if (m_setpoints.roller_action == RobotSetpoints.RollerAction.EXHAUST) {
            // Run intake outwards.
            intake.setSpeed(-Constants.kManualIntakeSpeed);
        } else {
            // Stop intake.
            intake.setSpeed(0.0);
        }
    }
}
