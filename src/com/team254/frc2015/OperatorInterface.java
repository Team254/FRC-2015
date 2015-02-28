package com.team254.frc2015;

import com.team254.frc2015.behavior.BehaviorManager;
import com.team254.frc2015.behavior.Commands;
import edu.wpi.first.wpilibj.Joystick;

import java.util.Optional;

public class OperatorInterface {
    private Commands m_commands = new Commands();

    Joystick leftStick = HardwareAdaptor.kLeftStick;
    Joystick rightStick = HardwareAdaptor.kRightStick;
    Joystick buttonBoard = HardwareAdaptor.kButtonBoard;

    public void reset() {
        m_commands = new Commands();
    }

    public Commands getCommands() {
        // Left stick
        if (leftStick.getRawButton(1)) {
            m_commands.roller_request = Commands.RollerRequest.INTAKE;
        } else if (leftStick.getRawButton(2)) {
            //m_commands.roller_request = Commands.RollerAction.EXHAUST;
        } else {
            m_commands.roller_request = Commands.RollerRequest.NONE;
        }

        // Right stick
        if (rightStick.getRawButton(2)) {
            m_commands.intake_request = Commands.IntakeRequest.CLOSE;
        } else if (leftStick.getRawButton(2)) {
            m_commands.intake_request = Commands.IntakeRequest.OPEN;
        } else {
            m_commands.intake_request = Commands.IntakeRequest.NEUTRAL;
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

        /*if (buttonBoard.getRawButton(7)) {
            m_commands.bottom_carriage_pusher_request = BehaviorManager.BottomCarriagePusherAction.EXTEND;
        } else {
            m_commands.bottom_carriage_pusher_request = BehaviorManager.BottomCarriagePusherAction.RETRACT;
        }*/

        if (buttonBoard.getRawButton(8)) {
            m_commands.bottom_jog = Optional.of(Constants.kElevatorJogPwm);
        } else if (buttonBoard.getRawButton(10)) {
            m_commands.bottom_jog = Optional.of(-Constants.kElevatorJogPwm);
        } else {
            m_commands.bottom_jog = Optional.empty();
        }

        if (buttonBoard.getRawButton(7)) {
            m_commands.top_jog = Optional.of(Constants.kElevatorJogPwm);
        } else if (buttonBoard.getRawButton(9)) {
            m_commands.top_jog = Optional.of(-Constants.kElevatorJogPwm);
        } else {
            m_commands.top_jog = Optional.empty();
        }

        m_commands.cancel_current_routine = buttonBoard.getX() < 0;

        if (buttonBoard.getY() < 0) {
            m_commands.preset_height = Commands.PresetHeight.HUMAN;
        } else if (buttonBoard.getZ() < 0) {
            m_commands.preset_height = Commands.PresetHeight.FLOOR;
        } else if (buttonBoard.getRawButton(6)) {
            m_commands.preset_height = Commands.PresetHeight.CAN;
        } else if (buttonBoard.getRawButton(5)) {
            m_commands.preset_height = Commands.PresetHeight.CAPPING;
        } else if (buttonBoard.getRawButton(4)) {
            m_commands.preset_height = Commands.PresetHeight.CARRY;
        } else {
            m_commands.preset_height = Commands.PresetHeight.NONE;
        }

        m_commands.top_carriage_claw_request = Commands.TopCarriageClawRequest.CLOSE;
        if (buttonBoard.getRawButton(1)) {
            m_commands.can_grabber_request = Commands.CanGrabberRequests.DO_GRAB;
            m_commands.top_carriage_claw_request = Commands.TopCarriageClawRequest.OPEN;
        } else if (buttonBoard.getRawButton(2)) {
            m_commands.can_grabber_request = Commands.CanGrabberRequests.STAGE_FOR_GRAB;
            //m_commands.top_carriage_pivot_request = Commands.TopCarriagePivotRequest.PIVOT_DOWN;
        } else {
            m_commands.can_grabber_request = Commands.CanGrabberRequests.NONE;
            //m_commands.top_carriage_pivot_request = Commands.TopCarriagePivotRequest.PIVOT_UP;
        }
        return m_commands;
    }
}
