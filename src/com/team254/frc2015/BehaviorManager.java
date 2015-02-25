package com.team254.frc2015;

import com.team254.frc2015.subsystems.BottomCarriage;
import com.team254.frc2015.subsystems.Drive;
import com.team254.frc2015.subsystems.Intake;
import com.team254.frc2015.subsystems.TopCarriage;
import edu.wpi.first.wpilibj.PowerDistributionPanel;

import java.util.Optional;

public class BehaviorManager {
    protected Drive drive = HardwareAdaptor.kDrive;
    protected TopCarriage top_carriage = HardwareAdaptor.kTopCarriage;
    protected BottomCarriage bottom_carriage = HardwareAdaptor.kBottomCarriage;
    protected Intake intake = HardwareAdaptor.kIntake;
    protected PowerDistributionPanel pdp = HardwareAdaptor.kPDP;

    public enum ElevatorMode {
        BOTTOM_CARRIAGE_MODE, TOP_CARRIAGE_MODE
    }

    public enum PresetHeight {
        NONE, MANUAL, CARRY, CAPPING, CAN, FLOOR, HUMAN, HOME
    }

    public enum IntakeAction {
        NONE, OPEN, CLOSE, NEUTRAL
    }

    public enum RollerAction {
        NONE, INTAKE, EXHAUST
    }

    public enum ElevatorAction {
        NONE, HP_LOAD_ACTION, FLOOR_LOAD_ACTION, LOAD_PREP_ACTION
    }

    public enum TopCarriagePivotAction {
        NONE, PIVOT_DOWN, PIVOT_UP
    }

    public enum TopCarriageClawAction {
        NONE, OPEN, CLOSE
    }

    public enum BottomCarriageFlapperAction {
        NONE, OPEN, CLOSE
    }

    public enum BottomCarriagePusherAction {
        NONE, EXTEND, RETRACT
    }

    public static class Commands {
        public ElevatorMode elevator_mode;
        public PresetHeight preset_height;
        public double top_jog;
        public double bottom_jog;
        public IntakeAction intake_action;
        public RollerAction roller_action;
        public ElevatorAction elevator_action;
        public TopCarriagePivotAction top_carriage_pivot_action;
        public TopCarriageClawAction top_carriage_claw_action;
        public BottomCarriageFlapperAction bottom_carriage_flapper_action;
        public BottomCarriagePusherAction bottom_carriage_pusher_action;
    }

    private ElevatorMode m_current_mode = ElevatorMode.BOTTOM_CARRIAGE_MODE;
    private static Optional<Double> m_nullopt = Optional.empty();
    private ElevatorSafety.Setpoints m_setpoints = new ElevatorSafety.Setpoints();
    private boolean m_top_jogging = true;
    private boolean m_bottom_jogging = true;

    public void update(Commands commands) {
        if (!bottom_carriage.isInitialized()) {
            m_bottom_jogging = false;
        }
        if (!top_carriage.isInitialized()) {
            m_top_jogging = false;
        }
        boolean wants_mode_changed = false;
        if (commands.elevator_mode != m_current_mode) {
            m_current_mode = commands.elevator_mode;
            wants_mode_changed = true;
        }
        boolean can_close_intake = true;
        boolean can_control_top_carriage_pivot = true;
        boolean can_control_top_carriage_grabber = true;
        boolean can_control_bottom_carriage = true;
        m_setpoints.top_setpoint = m_nullopt;
        m_setpoints.bottom_setpoint = m_nullopt;
        if (commands.preset_height == PresetHeight.CARRY) {
            // Carrying
            m_setpoints.bottom_setpoint = Optional.of(18.0);
            top_carriage.setSqueezeSetpoint(.2);
            m_top_jogging = false;
            m_bottom_jogging = false;
        } else if (commands.preset_height == PresetHeight.CAPPING) {
            // Capping
            m_setpoints.top_setpoint = Optional
                    .of(Constants.kTopCarriageMaxPositionInches);
            m_setpoints.bottom_setpoint = Optional.of(0.25);
            m_top_jogging = false;
            m_bottom_jogging = false;
        } else if (commands.preset_height == PresetHeight.CAN) {
            // Can loading
            m_setpoints.top_setpoint = Optional.of(5.5);
            m_setpoints.bottom_setpoint = Optional.of(3.5);
            m_top_jogging = false;
            m_bottom_jogging = false;
        } else if (commands.preset_height == PresetHeight.FLOOR) {
            // Floor load
            m_setpoints.bottom_setpoint = Optional.of(0.25);
            double bottom_movement = bottom_carriage.getHeight() - .25;
            double new_top_setpoint = top_carriage.getHeight() - bottom_movement + 13.0;
            if (new_top_setpoint > 28.0) {
                m_setpoints.top_setpoint = Optional.of(new_top_setpoint);
            } else {
                m_setpoints.top_setpoint = Optional.of(top_carriage.getHeight());
            }
            m_top_jogging = false;
            m_bottom_jogging = false;
        } else if (commands.preset_height == PresetHeight.HUMAN) {
            // Human Loading
            m_setpoints.bottom_setpoint = Optional.of(33.0);
            m_setpoints.top_setpoint = Optional.of(Constants.kTopCarriageMaxPositionInches);
            m_top_jogging = false;
            m_bottom_jogging = false;

        } else if (commands.preset_height == PresetHeight.HOME) {
            // Home
            m_setpoints.bottom_setpoint = Optional
                    .of(Constants.kBottomCarriageHomePositionInches);
            m_setpoints.top_setpoint = Optional
                    .of(Constants.kTopCarriageHomePositionInches);
            m_top_jogging = false;
            m_bottom_jogging = false;
        } else {
            if (commands.bottom_jog != 0) {
                m_bottom_jogging = true;
            }
            if (commands.top_jog != 0) {
                m_top_jogging = true;
            }
            if (m_bottom_jogging && commands.bottom_jog != 0) {
                bottom_carriage.setOpenLoop(commands.bottom_jog, false);
            } else if (m_bottom_jogging) {
                bottom_carriage.setOpenLoop(0, true);
            }
            if (m_top_jogging && commands.top_jog != 0) {
                top_carriage.setOpenLoop(commands.top_jog, false);
            } else if (m_top_jogging) {
                top_carriage.setOpenLoop(0, true);
            }
        }
        m_setpoints = ElevatorSafety.generateSafeSetpoints(m_setpoints);
        if (m_setpoints.top_setpoint.isPresent() && !m_top_jogging && !m_bottom_jogging) {
            top_carriage.setPositionSetpoint(m_setpoints.top_setpoint.get(), true);
        }
        if (m_setpoints.bottom_setpoint.isPresent() && !m_bottom_jogging && !m_bottom_jogging) {
            bottom_carriage.setPositionSetpoint(m_setpoints.bottom_setpoint.get(), true);
        }

        // can_close_intake = ElevatorSafety.canCloseIntake();

        // Top carriage actions.
        if (can_control_top_carriage_grabber
                && commands.top_carriage_claw_action == TopCarriageClawAction.OPEN) {
            top_carriage.setGrabberOpen(true);
            can_control_top_carriage_pivot = false;
        } else if (can_control_top_carriage_grabber
                && commands.top_carriage_claw_action == TopCarriageClawAction.CLOSE) {
            top_carriage.setGrabberOpen(false);
        }
        if (can_control_top_carriage_pivot
                && commands.top_carriage_pivot_action == TopCarriagePivotAction.PIVOT_DOWN) {
            top_carriage.setPivotDown(true);
        } else if (can_control_top_carriage_pivot
                && commands.top_carriage_pivot_action == TopCarriagePivotAction.PIVOT_UP) {
            top_carriage.setPivotDown(false);
        }

        // Bottom carriage actions.
        if (can_control_bottom_carriage
                && commands.bottom_carriage_flapper_action == BottomCarriageFlapperAction.OPEN) {
            bottom_carriage.setFlapperOpen(true);
        } else if (can_control_bottom_carriage
                && commands.bottom_carriage_flapper_action == BottomCarriageFlapperAction.CLOSE) {
            bottom_carriage.setFlapperOpen(false);
        }
        if (can_control_bottom_carriage
                && commands.bottom_carriage_pusher_action == BottomCarriagePusherAction.EXTEND) {
            bottom_carriage.setPusherExtended(true);
        } else if (can_control_bottom_carriage
                && commands.bottom_carriage_pusher_action == BottomCarriagePusherAction.RETRACT) {
            bottom_carriage.setPusherExtended(false);
        }

        // Intake actions.
        if (!can_close_intake || commands.intake_action == IntakeAction.OPEN) {
            // Open intake
            intake.open();
        } else if (commands.intake_action == IntakeAction.CLOSE) {
            // Close intake
            intake.close();
        }  else if (commands.intake_action == IntakeAction.NEUTRAL) {
            // Neutral intake
            intake.neutral();
        }

        // Roller actions.
        if (commands.roller_action == RollerAction.INTAKE) {
            // Run intake inwards.
            intake.setSpeed(0.35);
        } else if (commands.roller_action == RollerAction.EXHAUST) {
            // Run intake outwards.
            intake.setSpeed(-0.35);
        } else {
            // Stop intake.
            intake.setSpeed(0.0);
        }
    }
}
