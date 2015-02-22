package com.team254.frc2015.subsystems;

import com.team254.frc2015.Constants;
import com.team254.frc2015.subsystems.controllers.DriveStraightController;
import com.team254.frc2015.subsystems.controllers.TurnInPlaceController;
import com.team254.lib.util.CheesySpeedController;
import com.team254.lib.util.DriveSignal;
import com.team254.lib.util.Loopable;
import com.team254.lib.util.Pose;
import com.team254.lib.util.StateHolder;
import com.team254.lib.util.Subsystem;
import com.team254.lib.util.gyro.GyroThread;

import edu.wpi.first.wpilibj.Encoder;

public class Drive extends Subsystem implements Loopable {

    public interface DriveController {
        DriveSignal update(Pose pose);
        Pose getCurrentSetpoint();
    }

	public CheesySpeedController m_left_motor;
	public CheesySpeedController m_right_motor;
	public Encoder m_left_encoder;
	public Encoder m_right_encoder;
	public GyroThread m_gyro;
	private DriveController m_controller = null;

	protected final double m_inches_per_tick = Constants.kDriveWheelSizeInches
			* Math.PI / Constants.kElevatorEncoderCountsPerRev;
	protected final double m_wheelbase_width = 26.0; // Get from CAD
	protected final double m_turn_slip_factor = 1.2; // Measure empirically
	private Pose m_cached_pose = new Pose(0,0,0,0,0,0); // Don't allocate poses at 200Hz!

	public Drive(String name, CheesySpeedController left_drive,
			CheesySpeedController right_drive, Encoder left_encoder,
			Encoder right_encoder, GyroThread gyro) {
		super(name);
		this.m_left_motor = left_drive;
		this.m_right_motor = right_drive;
		this.m_left_encoder = left_encoder;
		this.m_right_encoder = right_encoder;
		this.m_left_encoder.setDistancePerPulse(m_inches_per_tick);
		this.m_right_encoder.setDistancePerPulse(m_inches_per_tick);
		this.m_gyro = gyro;
	}

	public void setOpenLoop(DriveSignal signal) {
		m_controller = null;
		setDriveOutputs(signal);
	}

	public void setDistanceSetpoint(double distance) {
		setDistanceSetpoint(distance, Constants.kDriveMaxSpeedInchesPerSec);
	}

	public void setDistanceSetpoint(double distance, double velocity) {
		// 0 < vel < max_vel
		double vel_to_use = Math.min(Constants.kDriveMaxSpeedInchesPerSec, Math.max(velocity, 0));
        m_controller = new DriveStraightController(
                getPoseToContinueFrom(),
                distance,
                vel_to_use);
	}

	public void setTurnSetPoint(double heading) {
		setTurnSetPoint(heading, Constants.kTurnMaxSpeedRadsPerSec);
	}

    public void setTurnSetPoint(double heading, double velocity) {
        velocity = Math.min(Constants.kTurnMaxSpeedRadsPerSec, Math.max(velocity, 0));
        m_controller = new TurnInPlaceController(getPoseToContinueFrom(), heading, velocity);
    }

	@Override
	public void getState(StateHolder states) {
		states.put("gyro_angle", m_gyro.getAngle());
		states.put("left_encoder", m_left_encoder.getDistance());
        states.put("left_encoder_rate", m_left_encoder.getRate());
        states.put("right_encoder_rate", m_right_encoder.getRate());
        states.put("right_encoder", m_right_encoder.getDistance());

        Pose setPointPose = m_controller == null
                ? getPhysicalPose()
                : m_controller.getCurrentSetpoint();
        states.put(
                "drive_set_point_pos",
                DriveStraightController.encoderDistance(setPointPose));
        states.put("turn_set_point_pos", setPointPose.getHeading());
	}

	@Override
	public void update() {
        if (m_controller == null) {
            return;
        }
        setDriveOutputs(m_controller.update(getPhysicalPose()));
	}

    private void setDriveOutputs(DriveSignal signal) {
        m_left_motor.set(signal.leftMotor);
        m_right_motor.set(-signal.rightMotor);
    }

    private Pose getPoseToContinueFrom() {
        if (m_controller != null) {
            // taking over from a previous controller, continue from its most recent desired state
            return m_controller.getCurrentSetpoint();
        }
        // first closed-loop action, continue from the robot's current physical state
        return getPhysicalPose();
    }

    /**
     * @return The pose according to the current sensor state
     */
    private Pose getPhysicalPose() {
        m_cached_pose.reset(
                m_left_encoder.getDistance(),
                m_right_encoder.getDistance(),
                m_left_encoder.getRate(),
                m_right_encoder.getRate(),
                m_gyro.getAngle(),
                m_gyro.getRate());
        return m_cached_pose;
    }

    @Override
    public void reloadConstants() {
        // TODO Auto-generated method stub
        
    }
}
