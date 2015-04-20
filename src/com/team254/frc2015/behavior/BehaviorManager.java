package com.team254.frc2015.behavior;

import com.team254.frc2015.Constants;
import com.team254.frc2015.ElevatorSafety;
import com.team254.frc2015.HardwareAdaptor;
import com.team254.frc2015.behavior.routines.*;
import com.team254.frc2015.subsystems.*;
import com.team254.lib.util.StateHolder;
import com.team254.lib.util.Tappable;

import java.util.Optional;

public class BehaviorManager implements Tappable {

    public boolean isZero(double val) {
        return val == 0 || (val < 0.001 && val > -0.001);
    }

    protected Drive drive = HardwareAdaptor.kDrive;
    protected TopCarriage top_carriage = HardwareAdaptor.kTopCarriage;
    protected BottomCarriage bottom_carriage = HardwareAdaptor.kBottomCarriage;
    protected Intake intake = HardwareAdaptor.kIntake;
    protected AirPeacock airPeacock = HardwareAdaptor.kAirPeacock;
    protected MotorPeacock motorPeacock = HardwareAdaptor.kMotorPeacock;

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
        } else if (commands.horizontal_can_grabber_request == Commands.HorizontalCanGrabberRequests.ACTIVATE && !(m_cur_routine instanceof HorizontalCanPickupRoutine)) {
            setNewRoutine(new HorizontalCanPickupRoutine());
        } else if (commands.vertical_can_grab_request == Commands.VerticalCanGrabberRequests.ACTIVATE && !(m_cur_routine instanceof VerticalCanPickupRoutine)) {
            setNewRoutine(new VerticalCanPickupRoutine());
        } else if (commands.floor_load_mode && !(m_cur_routine instanceof FloorLoadRoutine)) {
            setNewRoutine(new FloorLoadRoutine());
        } else if (!commands.floor_load_mode && m_cur_routine instanceof FloorLoadRoutine) {
            FloorLoadRoutine flr = (FloorLoadRoutine) m_cur_routine;
            if (!flr.isDoneWithSixStack()) {
                setNewRoutine(new AfterFloorClampRoutine());
            } else {
                setNewRoutine(null);
                m_setpoints.bottom_open_loop_jog = Optional.of(0.0);
            }
        } else if (commands.preset_request == Commands.PresetRequest.SCORE && !(m_cur_routine instanceof ScoreRoutine)) {
            setNewRoutine(new ScoreRoutine());
        } else if (commands.preset_request == Commands.PresetRequest.CARRY_SQUEZE) {
            setNewRoutine(new RegraspRoutine());
        } else if (commands.preset_request == Commands.PresetRequest.COOP) {
            setNewRoutine(new CoopRoutine());
        } else if ((m_cur_routine instanceof CoopRoutine) && commands.preset_request != Commands.PresetRequest.NONE && commands.preset_request != Commands.PresetRequest.COOP) {
            setNewRoutine(null);
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
        boolean want_pusher_extend = m_setpoints.coop_pusher_action == RobotSetpoints.CoopPusherAction.EXTEND;

        double bottom_jog_speed = 0.0;
        double top_jog_speed = 0.0;
        // Set elevator m_setpoints and jog
        if (m_setpoints.bottom_open_loop_jog.isPresent()) {
            bottom_jog_speed = m_setpoints.bottom_open_loop_jog.get();
            m_bottom_jogging = true;
        } else if (m_setpoints.m_elevator_setpoints.bottom_setpoint.isPresent()) {
            bottom_carriage.setPositionSetpoint(m_setpoints.m_elevator_setpoints.bottom_setpoint.get(), true);
            m_bottom_jogging = false;
        } else if (m_bottom_jogging && !m_setpoints.bottom_open_loop_jog.isPresent()) {
            bottom_carriage.setOpenLoop(0, true);
            m_bottom_jogging = false;
        }

        if (m_setpoints.top_carriage_squeeze && !want_pusher_extend) {
            top_carriage.squeeze();
        } else {
            if (top_carriage.hasSquezeEnabled()) {
                top_carriage.setOpenLoop(0, true);
            }
        }

        if (m_setpoints.top_open_loop_jog.isPresent()) {
            top_jog_speed = m_setpoints.top_open_loop_jog.get();
            m_top_jogging = true;
        } else if (m_setpoints.m_elevator_setpoints.top_setpoint.isPresent()) {
            top_carriage.setPositionSetpoint(m_setpoints.m_elevator_setpoints.top_setpoint.get(), true);
            m_top_jogging = false;
        } else if (m_top_jogging && !m_setpoints.top_open_loop_jog.isPresent()) {
            top_carriage.setOpenLoop(0, true);
            m_top_jogging = false;
        }

        if (!want_pusher_extend) {
            if (m_bottom_jogging) {
                bottom_carriage.setOpenLoop(bottom_jog_speed, isZero(bottom_jog_speed));
            }

            if (m_top_jogging) {
                top_carriage.setOpenLoop(top_jog_speed, isZero(top_jog_speed));
                if (m_cur_routine instanceof RegraspRoutine) {
                    setNewRoutine(null);
                }
            }
        } else {
            bottom_carriage.setOpenLoop(0, true);
            top_carriage.setOpenLoop(0, true);
        }

        // Bail from scoring
        if (m_cur_routine instanceof ScoreRoutine && m_bottom_jogging) {
            setNewRoutine(null);
        }

        ElevatorSafety.Setpoints m_elevator_setpoints = ElevatorSafety.generateSafeSetpoints(m_setpoints.m_elevator_setpoints);
        if (m_elevator_setpoints.top_setpoint.isPresent() && !m_top_jogging && !m_bottom_jogging) {
            top_carriage.setPositionSetpoint(m_elevator_setpoints.top_setpoint.get(), true);
        }
        if (m_elevator_setpoints.bottom_setpoint.isPresent() && !m_bottom_jogging && !m_bottom_jogging) {
            bottom_carriage.setPositionSetpoint(m_elevator_setpoints.bottom_setpoint.get(), true);
        }

        // Top carriage actions.
        if (can_control_top_carriage_grabber
                && m_setpoints.claw_action == RobotSetpoints.TopCarriageClawAction.OPEN) {
            top_carriage.setGrabberPosition(TopCarriage.GrabberPositions.OPEN);
            can_control_top_carriage_pivot = false;
        } else if (can_control_top_carriage_grabber
                && m_setpoints.claw_action == RobotSetpoints.TopCarriageClawAction.CLOSE) {
            top_carriage.setGrabberPosition(TopCarriage.GrabberPositions.CLOSED);
        } else if (can_control_top_carriage_grabber
                && m_setpoints.claw_action == RobotSetpoints.TopCarriageClawAction.NEUTRAL) {
            top_carriage.setGrabberPosition(TopCarriage.GrabberPositions.VENTED);
        } else if (can_control_top_carriage_grabber
                && m_setpoints.claw_action == RobotSetpoints.TopCarriageClawAction.PREFER_CLOSE) {
            top_carriage.setGrabberPosition(TopCarriage.GrabberPositions.CLOSED);
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

        // Intake actions.
        if (!can_close_intake || m_setpoints.intake_action == RobotSetpoints.IntakeAction.OPEN || m_setpoints.intake_action == RobotSetpoints.IntakeAction.PREFER_OPEN) {
            // Open intake
            intake.open();
        } else if (m_setpoints.intake_action == RobotSetpoints.IntakeAction.CLOSE || m_setpoints.intake_action == RobotSetpoints.IntakeAction.PREFER_CLOSE) {
            // Close intake
            intake.close();
        }

        // Roller actions.
        if (m_setpoints.roller_action == RobotSetpoints.RollerAction.INTAKE) {
            // Run intake inwards.
            intake.setSpeed(Constants.kManualIntakeSpeed);
        } else if (m_setpoints.roller_action == RobotSetpoints.RollerAction.EXHAUST) {
            // Run intake outwards.
            intake.setSpeed(-Constants.kManualExhaustSpeed);
        } else if (m_setpoints.roller_action == RobotSetpoints.RollerAction.EXHAUST_COOP) {
            // Run intake outwards.
            intake.setSpeed(-Constants.kCoopIntakeSpeed);
        } else if (m_setpoints.roller_action == RobotSetpoints.RollerAction.INTAKE_CAN) {
            // Run intake inwards.
            intake.setSpeed(Constants.kCanIntakeSpeed);
        } else if (m_setpoints.roller_action == RobotSetpoints.RollerAction.INTAKE_CAN_SLOW) {
            // Run intake inwards.
            intake.setSpeed(Constants.kCanIntakeSlowSpeed);
        } else {
            // Stop intake.
            intake.setSpeed(0.0);
        }

        // Motor peacock - use commands because this is only open loop
        // Down is positive pwm
        double leftPeacockSpeed = 0;
        if (commands.left_motor_peacock_requests == Commands.MotorPeacockRequests.MOVE_UP) {
            leftPeacockSpeed = -Constants.kPeacockUpManualPWM;
        } else if (commands.left_motor_peacock_requests == Commands.MotorPeacockRequests.MOVE_DOWN) {
            leftPeacockSpeed = Constants.kPeacockDownManualPWM;
        }

        double rightPeacockSpeed = 0;
        if (commands.right_motor_peacock_requests == Commands.MotorPeacockRequests.MOVE_UP) {
            rightPeacockSpeed = -Constants.kPeacockUpManualPWM;
        } else if (commands.right_motor_peacock_requests == Commands.MotorPeacockRequests.MOVE_DOWN) {
            rightPeacockSpeed = Constants.kPeacockDownManualPWM;
        }

        motorPeacock.setOpenLoop(leftPeacockSpeed, rightPeacockSpeed);

        // Pusher
        intake.setCoopPusherOut(m_setpoints.coop_pusher_action == RobotSetpoints.CoopPusherAction.EXTEND);

        // Pinball wizard
        intake.setPinballWizardOut(m_setpoints.pinball_wizard_action == RobotSetpoints.PinballWizardAction.EXTEND);

        // Peacock
        airPeacock.setDown(m_setpoints.deploy_peacock);
    }

    @Override
    public void getState(StateHolder states) {
        states.put("mode", m_cur_routine != null ? m_cur_routine.getName() : "---");
    }

    @Override
    public String getName() {
        return "behaviors";
    }
}
