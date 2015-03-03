package com.team254.frc2015;

import com.team254.frc2015.subsystems.BottomCarriage;
import com.team254.frc2015.subsystems.ElevatorCarriage;
import com.team254.frc2015.subsystems.TopCarriage;
import com.team254.lib.trajectory.TrajectoryFollower.TrajectorySetpoint;

import java.util.Optional;

public class ElevatorSafety {
    static TopCarriage kTopCarriage = HardwareAdaptor.kTopCarriage;
    static BottomCarriage kBottomCarriage = HardwareAdaptor.kBottomCarriage;

    public ElevatorSafety() {
    }

    public static class Setpoints {
        public Optional<Double> bottom_setpoint;
        public Optional<Double> top_setpoint;
    }

    public static boolean isMoveLegal(ElevatorCarriage carriage,
            TrajectorySetpoint setpoint) {
        // Don't allow upwards moves if the top carriage is already near its
        // limit
        return !(carriage == kBottomCarriage
                && setpoint.vel > 0
                && kTopCarriage.getHeight() >= Constants.kTopCarriageMaxPositionInches && kTopCarriage
                    .getBreakbeamTriggered());
    }

    public static Setpoints generateSafeSetpoints(Setpoints setpoints) {
        // Sanity check the setpoints to ensure they are within limits and
        // the bottom is not above the top.
        Setpoints result = setpoints;
        if (result.top_setpoint.isPresent()) {
            if (result.top_setpoint.get() < Constants.kTopCarriageMinPositionInches) {
                result.top_setpoint = Optional
                        .of(Constants.kTopCarriageMinPositionInches);
            } else if (result.top_setpoint.get() > Constants.kTopCarriageMaxPositionInches) {
                result.top_setpoint = Optional
                        .of(Constants.kTopCarriageMaxPositionInches);
            }
        }
        if (result.bottom_setpoint.isPresent()) {
            if (result.bottom_setpoint.get() < Constants.kBottomCarriageMinPositionInches) {
                result.bottom_setpoint = Optional
                        .of(Constants.kBottomCarriageMinPositionInches);
            } else if (result.bottom_setpoint.get() > Constants.kBottomCarriageMaxPositionInches) {
                result.bottom_setpoint = Optional
                        .of(Constants.kBottomCarriageMaxPositionInches);
            }
            if (result.top_setpoint.isPresent()
                    && result.top_setpoint.get() < Constants.kBottomCarriageHeight
                            + result.bottom_setpoint.get()) {
                result.bottom_setpoint = Optional.of(result.top_setpoint.get()
                        - Constants.kBottomCarriageHeight);
            }
        }
        return result;
    }
}
