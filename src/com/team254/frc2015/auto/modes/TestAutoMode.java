package com.team254.frc2015.auto.modes;

import java.util.Optional;

import com.team254.frc2015.Constants;
import com.team254.frc2015.ElevatorSafety;
import com.team254.frc2015.auto.AutoMode;
import com.team254.frc2015.auto.AutoModeEndedException;

public class TestAutoMode extends AutoMode {

	private void moveElevator(boolean go_down) {
		ElevatorSafety.Setpoints setpoints = new ElevatorSafety.Setpoints();
        setpoints.bottom_setpoint = Optional.of(go_down ? Constants.kBottomCarriageHomePositionInches : 40.0);
        setpoints.top_setpoint = Optional.of(go_down ? Constants.kTopCarriageHomePositionInches : 65.0);
        setpoints = ElevatorSafety
                .generateSafeSetpoints(setpoints);
        top_carriage.setPositionSetpoint(setpoints.top_setpoint.get(), false);
        bottom_carriage.setPositionSetpoint(setpoints.bottom_setpoint.get(),
                false);
	}
	
	@Override
	public void routine() throws AutoModeEndedException {
		System.out.println("Moving up");
		moveElevator(false);
		waitTime(3);
		System.out.println("Moving down");
		moveElevator(true);
		waitTime(3);
		System.out.println("Moving up");
		moveElevator(false);
		waitTime(3);
		System.out.println("Moving down");
		moveElevator(true);
		waitTime(3);
	}

}
