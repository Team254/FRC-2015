package com.team254.frc2015;

import edu.wpi.first.wpilibj.Joystick;

public class OperatorInterface {
    private BehaviorManager.Commands m_commands = new BehaviorManager.Commands();

    Joystick leftStick = HardwareAdaptor.kLeftStick;
    Joystick rightStick = HardwareAdaptor.kRightStick;
    Joystick buttonBoard = HardwareAdaptor.kButtonBoard;

    public void reset() {
        m_commands = new BehaviorManager.Commands();
    }

    public BehaviorManager.Commands getCommands() {
        // Left stick
        if (leftStick.getRawButton(1)) {
            m_commands.roller_action = BehaviorManager.RollerAction.INTAKE;
        } else if (leftStick.getRawButton(2)) {
            m_commands.roller_action = BehaviorManager.RollerAction.EXHAUST;
        } else {
            m_commands.roller_action = BehaviorManager.RollerAction.NONE;
        }

        // Right stick
        if (rightStick.getRawButton(2)) {
            m_commands.intake_action = BehaviorManager.IntakeAction.CLOSE;
        } else {
            m_commands.intake_action = BehaviorManager.IntakeAction.OPEN;
        }

        // Button board
        if (buttonBoard.getRawAxis(3) > 0.1) {
            m_commands.elevator_mode = BehaviorManager.ElevatorMode.BOTTOM_CARRIAGE_MODE;
        } else {
            m_commands.elevator_mode = BehaviorManager.ElevatorMode.TOP_CARRIAGE_MODE;
        }

        if (buttonBoard.getRawButton(3)) {
            m_commands.bottom_carriage_flapper_action = BehaviorManager.BottomCarriageFlapperAction.OPEN;
        } else {
            m_commands.bottom_carriage_flapper_action = BehaviorManager.BottomCarriageFlapperAction.CLOSE;
        }

        /*if (buttonBoard.getRawButton(7)) {
            m_commands.bottom_carriage_pusher_action = BehaviorManager.BottomCarriagePusherAction.EXTEND;
        } else {
            m_commands.bottom_carriage_pusher_action = BehaviorManager.BottomCarriagePusherAction.RETRACT;
        }*/
        
        if (buttonBoard.getRawButton(8)) {
            m_commands.bottom_jog = Constants.kElevatorJogPwm;
        } else if (buttonBoard.getRawButton(10)) {
            m_commands.bottom_jog = -Constants.kElevatorJogPwm;
        } else {
            m_commands.bottom_jog = 0;
        }

        if (buttonBoard.getRawButton(7)) {
            m_commands.top_jog = Constants.kElevatorJogPwm;
        } else if (buttonBoard.getRawButton(9)) {
            m_commands.top_jog = -Constants.kElevatorJogPwm;
        } else {
            m_commands.top_jog = 0;
        }
        
        if (buttonBoard.getX() < 0) {
            m_commands.preset_height = BehaviorManager.PresetHeight.HOME;
        } else if (buttonBoard.getY() < 0) {
            m_commands.preset_height = BehaviorManager.PresetHeight.HUMAN;
        } else if (buttonBoard.getZ() < 0) {
            m_commands.preset_height = BehaviorManager.PresetHeight.FLOOR;
        } else if (buttonBoard.getRawButton(6)) {
            m_commands.preset_height = BehaviorManager.PresetHeight.CAN;
        } else if (buttonBoard.getRawButton(5)) {
            m_commands.preset_height = BehaviorManager.PresetHeight.CAPPING;
        } else if (buttonBoard.getRawButton(4)) {
            m_commands.preset_height = BehaviorManager.PresetHeight.CARRY;
        } else {
            m_commands.preset_height = BehaviorManager.PresetHeight.NONE;
        }

        if (buttonBoard.getRawButton(1)) {
            m_commands.top_carriage_claw_action = BehaviorManager.TopCarriageClawAction.OPEN;
        } else {
            m_commands.top_carriage_claw_action = BehaviorManager.TopCarriageClawAction.CLOSE;
        }

        if (buttonBoard.getRawButton(2)) {
            m_commands.top_carriage_pivot_action = BehaviorManager.TopCarriagePivotAction.PIVOT_DOWN;
        } else {
            m_commands.top_carriage_pivot_action = BehaviorManager.TopCarriagePivotAction.PIVOT_UP;
        }
        return m_commands;
    }
}
