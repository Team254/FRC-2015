package com.team254.frc2015;

import java.util.Optional;

import com.team254.frc2015.subsystems.ElevatorCarriage;

public class SafeElevatorSetpointGenerator {
    static ElevatorCarriage kTopCarriage = HardwareAdaptor.kTopCarriage;
    static ElevatorCarriage kBottomCarriage = HardwareAdaptor.kBottomCarriage;

    public SafeElevatorSetpointGenerator() {
    }

    public static class Setpoints {
        Optional<Double> bottom_setpoint;
        Optional<Double> top_setpoint;
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
