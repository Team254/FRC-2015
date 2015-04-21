package com.team254.frc2015.subsystems.controllers;

import com.team254.frc2015.Constants;
import com.team254.frc2015.subsystems.Drive;
import com.team254.lib.util.DriveSignal;
import com.team254.lib.util.Pose;
import com.team254.lib.util.SynchronousPID;

public class DriveFinishLineController implements  Drive.DriveController {
    BangBangFinishLineController m_controller;

    Pose m_setpoint = new Pose(0,0,0,0,0,0);
    double m_heading = 0;
    private SynchronousPID mTurnPid;
    public DriveSignal m_cached_drive = new DriveSignal(0, 0);

    public DriveFinishLineController(double distance, double heading, double tolerance) {
        m_controller = new BangBangFinishLineController(tolerance);
        m_controller.setGoal(distance);
        m_heading = heading;
        mTurnPid = new SynchronousPID();
        mTurnPid.setPID(
                Constants.kDriveStraightKp,
                Constants.kDriveStraightKi,
                Constants.kDriveStraightKd);
        mTurnPid.setSetpoint(heading);
    }

    @Override
    public DriveSignal update(Pose pose) {
        m_setpoint = pose;
        double position = (pose.getLeftDistance() + pose.getRightDistance()) / 2.0;
        double throttle = m_controller.update(position);
        double turn = mTurnPid.calculate(pose.getHeading());
        m_cached_drive.leftMotor = throttle + turn;
        m_cached_drive.rightMotor = throttle - turn;
        return m_cached_drive;
    }

    @Override
    public Pose getCurrentSetpoint() {
        return m_setpoint;
    }

    @Override
    public boolean onTarget() {
        return m_controller.isOnTarget();
    }
}
