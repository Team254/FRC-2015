package com.team254.frc2015.subsystems;

import com.team254.frc2015.Constants;
import com.team254.frc2015.ElevatorSafety;
import com.team254.frc2015.HardwareAdaptor;
import com.team254.frc2015.subsystems.controllers.ElevatorCarriageForceController;
import com.team254.frc2015.subsystems.controllers.ElevatorHomingController;
import com.team254.frc2015.subsystems.controllers.TrajectoryFollowingPositionController;
import com.team254.lib.trajectory.TrajectoryFollower;
import com.team254.lib.util.CheesySpeedController;
import com.team254.lib.util.Controller;
import com.team254.lib.util.Loopable;
import com.team254.lib.util.StateHolder;
import com.team254.lib.util.Subsystem;

import edu.wpi.first.wpilibj.ChezyInterruptHandlerFunction;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;

public class ElevatorCarriage extends Subsystem implements Loopable {
    public CheesySpeedController m_motor;
    public Solenoid m_brake;
    public Encoder m_encoder;
    public DigitalInput m_home;

    protected Controller m_controller = null;
    protected ElevatorHomingController m_homing_controller;

    protected boolean m_initialized = true; // Change this later for homing
    private Double cached_setpoint = null;

    protected ChezyInterruptHandlerFunction<ElevatorCarriage> isr = new ChezyInterruptHandlerFunction<ElevatorCarriage>() {
        @Override
        public void interruptFired(int interruptAssertedMask,
                ElevatorCarriage param) {
            System.out.println("Interrupt fired on " + param.getName() + "!");
        }

        @Override
        public ElevatorCarriage overridableParamater() {
            return ElevatorCarriage.this;
        }
    };

    protected Limits m_limits = new Limits();
    boolean m_brake_on_target = false;

    public class Limits {
        protected double m_min_position;
        protected double m_max_position;
        protected double m_home_position;
    }

    public ElevatorCarriage(String name, CheesySpeedController motor,
            Solenoid brake, Encoder encoder, DigitalInput home) {
        super(name);
        m_motor = motor;
        m_brake = brake;
        m_encoder = encoder;
        m_home = home;
        reloadConstants();
        m_home.requestInterrupts(isr);
        m_home.setUpSourceEdge(false, true);
    }

    public Controller getCurrentController() {
        return m_controller;
    }

    private double getRelativeHeight() {
        return m_encoder.get() * 2.0 * Constants.kElevatorPulleyRadiusInches
                * Math.PI / Constants.kElevatorEncoderCountsPerRev;
    }

    public double getHeight() {
        return m_limits.m_home_position
                + (getRelativeHeight() - m_homing_controller.getZeroOffset());
    }

    public double getVelocity() {
        return m_encoder.getRate() * 2.0
                * Constants.kElevatorPulleyRadiusInches * Math.PI
                / Constants.kElevatorEncoderCountsPerRev;
    }

    public TrajectoryFollower.TrajectorySetpoint getSetpoint() {
        TrajectoryFollower.TrajectorySetpoint setpoint;
        // Rather than reading encoder velocity, we report the last commanded
        // velocity from a velocity profile. This ensures that the input is
        // smooth when changing setpoints.
        if (m_controller instanceof TrajectoryFollowingPositionController) {
            setpoint = ((TrajectoryFollowingPositionController) m_controller)
                    .getSetpoint();
        } else {
            setpoint = new TrajectoryFollower.TrajectorySetpoint();
            setpoint.pos = getHeight();
        }
        return setpoint;
    }

    public double getGoalHeight() {
        if (m_controller instanceof TrajectoryFollowingPositionController) {
            return ((TrajectoryFollowingPositionController) m_controller)
                    .getGoal();
        } else {
            return -1;
        }
    }

    public double getCommand() {
        return m_motor.get();
    }

    protected void setSpeedUnsafe(double speed) {
        m_motor.set(speed);
    }

    @Override
    public void reloadConstants() {
        m_controller = null;

    }

    protected void setSpeedSafe(double desired_speed) {
        double height = getHeight();
        if (desired_speed > 1E-3 || desired_speed < -1E-3) {
            setBrake(false);
        }
        if (height >= m_limits.m_max_position) {
            desired_speed = Math.min(0, desired_speed);
        } else if (height <= m_limits.m_min_position) {
            desired_speed = Math.max(0, desired_speed);
        }
        setSpeedUnsafe(desired_speed);
    }

    protected void setBrake(boolean on) {
        m_brake.set(!on); // brake is normally applied
        if (on) {
            setSpeedUnsafe(0);
        }
    }

    public boolean getBrake() {
        return !m_brake.get();
    }

    public synchronized void setPositionSetpointUnsafe(double setpoint,
            boolean brake_on_target) {
        m_brake_on_target = brake_on_target;
        TrajectoryFollower.TrajectorySetpoint prior_setpoint = getSetpoint();
        if (!(m_controller instanceof TrajectoryFollowingPositionController)) {
            TrajectoryFollower.TrajectoryConfig config = new TrajectoryFollower.TrajectoryConfig();
            config.dt = Constants.kControlLoopsDt;
            config.max_acc = Constants.kElevatorMaxAccelInchesPerSec2;
            config.max_vel = Constants.kElevatorMaxSpeedInchesPerSec;
            m_controller = new TrajectoryFollowingPositionController(
                    Constants.kElevatorCarriagePositionKp,
                    Constants.kElevatorCarriagePositionKi,
                    Constants.kElevatorCarriagePositionKd,
                    Constants.kElevatorCarriagePositionKv,
                    Constants.kElevatorCarriagePositionKa,
                    Constants.kElevatorOnTargetError, config);
        }
        ((TrajectoryFollowingPositionController) m_controller).setGoal(
                prior_setpoint, setpoint);
    }

    public synchronized void setPositionSetpoint(double setpoint,
            boolean brake_on_target) {
        if (!m_initialized) {
            cached_setpoint = setpoint;
        } else {
            setPositionSetpointUnsafe(setpoint, brake_on_target);
        }

    }

    public synchronized void setOpenLoop(double speed, boolean brake) {
        m_controller = null;
        setBrake(brake);
        setSpeedSafe(speed);
    }

    public synchronized void setSqueezeSetpoint(double squeeze_power) {
        if (!(m_controller instanceof ElevatorCarriageForceController)) {
            m_controller = new ElevatorCarriageForceController(this);
        }
        ((ElevatorCarriageForceController) m_controller)
                .setSqueezePower(squeeze_power);
    }

    private void setSpeedIfValid(double speed) {
        TrajectoryFollowingPositionController position_controller = (TrajectoryFollowingPositionController) m_controller;
        if (!ElevatorSafety
                .isMoveLegal(this, position_controller.getSetpoint())) {
            // If this move is illegal, stop immediately.
            TrajectoryFollower.TrajectorySetpoint setpoint = new TrajectoryFollower.TrajectorySetpoint();
            setpoint.pos = getHeight();
            position_controller.setGoal(setpoint, getHeight());
            setSpeedSafe(0.0);
        } else {
            setSpeedSafe(position_controller.get());
        }
    }

    @Override
    public synchronized void update() {
        if (!m_initialized) {
            double new_setpoint = m_homing_controller.update(getSetpoint().pos,
                    getRelativeHeight());
            setPositionSetpointUnsafe(new_setpoint, false);
            if (m_homing_controller.isReady()) {
                m_initialized = true;
                if (cached_setpoint != null) {
                    setPositionSetpointUnsafe(cached_setpoint, true);
                } else {
                    setPositionSetpointUnsafe(getHeight(), true);
                }
            }
        }
        if (m_controller instanceof TrajectoryFollowingPositionController) {
            TrajectoryFollowingPositionController position_controller = (TrajectoryFollowingPositionController) m_controller;
            if (position_controller.isOnTarget()) {
                // No need to brake at bottom of travel.
                setBrake(m_brake_on_target && getHeight() > 1.0);
                if (!m_brake_on_target) {
                    position_controller.update(getHeight(), getVelocity());
                    setSpeedIfValid(position_controller.get());
                }
            } else {
                position_controller.update(getHeight(), getVelocity());
                setSpeedIfValid(position_controller.get());
            }
        } else if (m_controller instanceof ElevatorCarriageForceController) {
            double power = ((ElevatorCarriageForceController) m_controller)
                    .update();
            setBrake(false);
            setSpeedSafe(power);
        } else {
            // do nothing.
        }
    }

    @Override
    public void getState(StateHolder states) {
        states.put("height", getHeight());
        states.put("setpoint", getSetpoint().pos);
        states.put("home_dio", getHomeSensorHovered());
        states.put("current", m_motor.getSignedCurrent());
        states.put("pwm", m_motor.get());
    }

    public boolean getHomeSensorHovered() {
        return !m_home.get();
    }
}
