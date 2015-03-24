package com.team254.frc2015.subsystems;

import com.team254.frc2015.Constants;
import com.team254.frc2015.ElevatorSafety;
import com.team254.frc2015.Robot;
import com.team254.frc2015.subsystems.controllers.BangBangFinishLineController;
import com.team254.frc2015.subsystems.controllers.ElevatorSqueezeController;
import com.team254.frc2015.subsystems.controllers.TrajectoryFollowingPositionController;
import com.team254.lib.trajectory.TrajectoryFollower;
import com.team254.lib.util.*;

import edu.wpi.first.wpilibj.*;

public class ElevatorCarriage extends Subsystem implements Loopable {
    public CheesySpeedController m_motor;
    public Solenoid m_brake;
    public Encoder m_encoder;
    public DigitalInput m_home;

    protected Controller m_controller = null;
    protected boolean m_initialized = true;
    protected double m_zero_offset = 0;
    private boolean m_fast_hit_top = false;

    protected ChezyInterruptHandlerFunction<ElevatorCarriage> isr = new ChezyInterruptHandlerFunction<ElevatorCarriage>() {
        @Override
        public void interruptFired(int interruptAssertedMask,
                                   ElevatorCarriage param) {
            if (!m_initialized) {
                m_zero_offset = getRelativeHeight();
                m_initialized = true;
                disableInterrupts();
            }
        }

        @Override
        public ElevatorCarriage overridableParamater() {
            return ElevatorCarriage.this;
        }
    };

    public void findHome() {
        m_initialized = false;
        m_home.requestInterrupts(isr);
        m_home.setUpSourceEdge(false, true); // Pulled high be default, need falling edge
        m_home.enableInterrupts();
    }

    public void disableInterrupts() {
        m_home.disableInterrupts();
    }

    protected Limits m_limits = new Limits();
    boolean m_brake_on_target = false;

    public class Limits {
        protected double m_min_position;
        protected double m_max_position;
        protected double m_rezero_position;
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
    }

    public Controller getCurrentController() {
        return m_controller;
    }

    protected double getRelativeHeight() {
        return m_encoder.get() * 2.0 * Constants.kElevatorPulleyRadiusInches
                * Math.PI / Constants.kElevatorEncoderCountsPerRev;
    }

    public double getHeight() {
        if (m_initialized) {
            return m_limits.m_rezero_position
                    + (getRelativeHeight() - m_zero_offset);
        } else {
            return getRelativeHeight() + m_limits.m_home_position;
        }
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
        } else if (m_controller instanceof BangBangFinishLineController) {
            return ((BangBangFinishLineController) m_controller).getGoal();
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

    protected void setSpeedOpenLoopSafe(double desired_speed) {
        double height = getHeight();
        if (desired_speed > 1E-3 || desired_speed < -1E-3) {
            setBrake(false);
        }
        setSpeedUnsafe(desired_speed);
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
        m_brake.set(on); // brake is normally applied
        if (on) {
            setSpeedUnsafe(0);
        }
    }

    public boolean getBrake() {
        return m_brake.get();
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
        setPositionSetpointUnsafe(setpoint, brake_on_target);
    }
    
    public synchronized void setFastPositionSetpoint(double setpoint) {
        if (!(m_controller instanceof BangBangFinishLineController)) {
            m_controller = new BangBangFinishLineController(0.5);
        }
        m_controller.reset();
        m_fast_hit_top = false;
        ((BangBangFinishLineController) m_controller).setGoal(setpoint);
    }

    public boolean isInitialized() {
        return m_initialized;
    }

    public synchronized void setOpenLoop(double speed, boolean brake) {
        m_controller = null;
        setBrake(brake);
        setSpeedOpenLoopSafe(speed);
    }

    public synchronized void squeeze() {
        if (!(m_controller instanceof ElevatorSqueezeController)) {
            m_controller = new ElevatorSqueezeController();
        }
    }

    public boolean fastHitTop() {
        return m_fast_hit_top;
    }

    private void setSpeedIfValid(double speed) {
        if (m_controller instanceof TrajectoryFollowingPositionController) {
            TrajectoryFollowingPositionController position_controller = (TrajectoryFollowingPositionController) m_controller;
            if (!ElevatorSafety
                    .isMoveLegal(this, position_controller.getSetpoint())) {
                // If this move is illegal, stop immediately.
                TrajectoryFollower.TrajectorySetpoint setpoint = new TrajectoryFollower.TrajectorySetpoint();
                setpoint.pos = getHeight();
                position_controller.setGoal(setpoint, getHeight());
                setSpeedSafe(0.0);
            } else {
                setSpeedSafe(speed);
            }
        } else if(m_controller instanceof BangBangFinishLineController) {
            BangBangFinishLineController bangbang = (BangBangFinishLineController) m_controller;
            if (!ElevatorSafety.isMoveLegal(this, bangbang.getGoal())) {
                // Stop immediately.
                setSpeedSafe(0.0);
                setBrake(true);
                m_fast_hit_top = true;
                System.out.println("Hit top!!!!");
                m_controller = null;
            } else {
                setSpeedSafe(speed);
            }
        }
    }

    @Override
    public synchronized void update() {

       if (m_controller instanceof TrajectoryFollowingPositionController) {
            TrajectoryFollowingPositionController position_controller = (TrajectoryFollowingPositionController) m_controller;
            if (position_controller.isOnTarget()) {
                // No need to brake at bottom of travel.
                setBrake(m_brake_on_target && getHeight() > 1.0);
                position_controller.update(getHeight(), getVelocity());
                if (!m_brake_on_target) {
                    setSpeedIfValid(position_controller.get());
                } else {
                    setOpenLoop(0, true);
                    m_controller = null;
                }
            } else {
                position_controller.update(getHeight(), getVelocity());
                setSpeedIfValid(position_controller.get());
            }
        } else if (m_controller instanceof BangBangFinishLineController) {
            double power = ((BangBangFinishLineController) m_controller).update(getHeight());
            if (power != 0.0) {
                setBrake(false);
                setSpeedIfValid(power);
            } else {
                setBrake(true);
            }
        } else if (m_controller instanceof ElevatorSqueezeController) {
            double power = ((ElevatorSqueezeController) m_controller)
                    .update();
            setBrake(false);
            setSpeedSafe(power);
        } else {
            // Do nothing
        }

        // Failsafe
        if (getBrake()) {
            setSpeedUnsafe(0);
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

    public boolean isOnTarget() {
        return m_initialized && (m_controller != null ? m_controller.isOnTarget() : true);
    }
}
