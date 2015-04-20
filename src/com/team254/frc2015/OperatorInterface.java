package com.team254.frc2015;

import com.team254.frc2015.behavior.Commands;
import com.team254.lib.util.Latch;
import edu.wpi.first.wpilibj.Joystick;

import java.util.Optional;

public class OperatorInterface {
    private Commands m_commands = new Commands();

    Joystick leftStick = HardwareAdaptor.kLeftStick;
    Joystick rightStick = HardwareAdaptor.kRightStick;
    Joystick buttonBoard = HardwareAdaptor.kButtonBoard;
    Latch horizontalCanLatch = new Latch();
    Latch coopLatch = new Latch();
    Latch verticalCanLatch = new Latch();

    public void reset() {
        m_commands = new Commands();
    }

    public Commands getCommands() {
        // Left stick
        if (leftStick.getRawButton(1)) {
            m_commands.roller_request = Commands.RollerRequest.INTAKE;
        } else if (leftStick.getRawButton(2)) {
            m_commands.roller_request = Commands.RollerRequest.EXHAUST;
        } else {
            m_commands.roller_request = Commands.RollerRequest.NONE;
        }

        // Right stick
        if (rightStick.getRawButton(2)) {
            m_commands.intake_request = Commands.IntakeRequest.OPEN;
        } else {
            m_commands.intake_request = Commands.IntakeRequest.CLOSE;
        }

        // Button board
        if (buttonBoard.getRawAxis(3) > 0.1) {
            m_commands.elevator_mode = Commands.ElevatorMode.BOTTOM_CARRIAGE_MODE;
        } else {
            m_commands.elevator_mode = Commands.ElevatorMode.TOP_CARRIAGE_MODE;
        }

        if (buttonBoard.getRawButton(3)) {
            m_commands.bottom_carriage_flapper_request = Commands.BottomCarriageFlapperRequest.OPEN;
        } else {
            m_commands.bottom_carriage_flapper_request = Commands.BottomCarriageFlapperRequest.CLOSE;
        }

        if (buttonBoard.getRawButton(8)) { // Bottom carriage jog up
            if (buttonBoard.getRawButton(11)) { // Slow
                m_commands.bottom_jog = Optional.of(Constants.kElevatorJogSlowPwm);
            } else if (buttonBoard.getRawButton(12)) { // Fast
                m_commands.bottom_jog = Optional.of(Constants.kElevatorJogFastPwm);
            } else { // Medium
                m_commands.bottom_jog = Optional.of(Constants.kElevatorJogFastPwm); // disable old man mode
            }
        } else if (buttonBoard.getRawButton(10)) { // Bottom carriage jog down
            if (buttonBoard.getRawButton(11)) { // Slow
                m_commands.bottom_jog = Optional.of(-Constants.kElevatorJogSlowPwm);
            } else if (buttonBoard.getRawButton(12)) { // Fast
                m_commands.bottom_jog = Optional.of(-Constants.kElevatorJogFastPwm);
            } else { // Medium
                m_commands.bottom_jog = Optional.of(-Constants.kElevatorJogMediumPwm);
            }
        } else {
            m_commands.bottom_jog = Optional.empty();
        }

        if (buttonBoard.getRawButton(7)) { // top up
            if (buttonBoard.getRawButton(11)) { // Slow
                m_commands.top_jog = Optional.of(Constants.kElevatorJogSlowPwm);
            } else if (buttonBoard.getRawButton(12)) { // Fast
                m_commands.top_jog = Optional.of(Constants.kElevatorJogFastPwm);
            } else { // Medium
                m_commands.top_jog = Optional.of(Constants.kElevatorJogFastPwm); // disable old man mode
            }
        } else if (buttonBoard.getRawButton(9)) { // top down
            if (buttonBoard.getRawButton(11)) { // Slow
                m_commands.top_jog = Optional.of(-Constants.kElevatorJogSlowPwm);
            } else if (buttonBoard.getRawButton(12)) { // Fast
                m_commands.top_jog = Optional.of(-Constants.kElevatorJogFastPwm);
            } else { // Medium
                m_commands.top_jog = Optional.of(-Constants.kElevatorJogMediumPwm);
            }
        } else {
            m_commands.top_jog = Optional.empty();
        }

        m_commands.cancel_current_routine = buttonBoard.getX() < 0; // Button 6

        if (coopLatch.update(buttonBoard.getY() < 0)) { // Button 5
            m_commands.preset_request = Commands.PresetRequest.COOP;
        } else if (buttonBoard.getRawButton(5)) { // Button 2
            m_commands.preset_request = Commands.PresetRequest.SCORE;
        } else if (buttonBoard.getRawButton(4)) { // Button 1
            //m_commands.preset_request = Commands.PresetRequest.CARRY_SQUEZE;
        } else {
            m_commands.preset_request = Commands.PresetRequest.NONE;
        }

        if (verticalCanLatch.update(buttonBoard.getRawButton(6))) { // Button 3
            m_commands.vertical_can_grab_request = Commands.VerticalCanGrabberRequests.ACTIVATE;
        } else {
            m_commands.vertical_can_grab_request = Commands.VerticalCanGrabberRequests.NONE;
        }

        m_commands.top_carriage_claw_request = Commands.TopCarriageClawRequest.CLOSE;
        if (buttonBoard.getRawButton(1)) {
            m_commands.top_carriage_claw_request = Commands.TopCarriageClawRequest.OPEN;
        }

        if (horizontalCanLatch.update(buttonBoard.getRawButton(2))) {
            m_commands.horizontal_can_grabber_request = Commands.HorizontalCanGrabberRequests.ACTIVATE;
        } else {
            m_commands.horizontal_can_grabber_request = Commands.HorizontalCanGrabberRequests.NONE;
        }

        m_commands.floor_load_mode = buttonBoard.getRawAxis(3) > 0.1;
        m_commands.deploy_peacock = buttonBoard.getRawButton(11) && buttonBoard.getZ() < 0; // button 4;

        if (buttonBoard.getZ() < 0) { // left motor peacock
            if (buttonBoard.getRawButton(11)) {
                m_commands.left_motor_peacock_requests = Commands.MotorPeacockRequests.MOVE_DOWN;
            } else {
                m_commands.left_motor_peacock_requests = Commands.MotorPeacockRequests.MOVE_UP;
            }
        } else {
            m_commands.left_motor_peacock_requests = Commands.MotorPeacockRequests.NONE;
        }

        if (buttonBoard.getRawButton(4)) { // right motor peacock
            if (buttonBoard.getRawButton(11)) {
                m_commands.right_motor_peacock_requests = Commands.MotorPeacockRequests.MOVE_DOWN;
            } else {
                m_commands.right_motor_peacock_requests = Commands.MotorPeacockRequests.MOVE_UP;
            }
        } else {
            m_commands.right_motor_peacock_requests = Commands.MotorPeacockRequests.NONE;
        }

        return m_commands;
    }
}
