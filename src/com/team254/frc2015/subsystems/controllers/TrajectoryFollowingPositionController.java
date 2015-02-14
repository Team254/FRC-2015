package com.team254.frc2015.subsystems.controllers;

import com.team254.lib.trajectory.TrajectoryFollower;
import com.team254.lib.util.Controller;

public class TrajectoryFollowingPositionController extends Controller {
    TrajectoryFollower m_follower;
    double m_goal;
    double m_error;
    double m_on_target_delta;
    double m_result = 0;

    public TrajectoryFollowingPositionController(double kp, double ki,
            double kd, double kv, double ka, double on_target_delta,
            TrajectoryFollower.TrajectoryConfig config) {
        m_follower = new TrajectoryFollower();
        m_follower.configure(kp, ki, kd, kv, ka, config);
        m_on_target_delta = on_target_delta;
    }

    public void setGoal(TrajectoryFollower.TrajectorySetpoint current_state,
            double goal) {
        m_goal = goal;
        m_follower.setGoal(current_state, goal);
    }

    public void setConfig(TrajectoryFollower.TrajectoryConfig config) {
    	m_follower.setConfig(config);
    }

    public TrajectoryFollower.TrajectoryConfig getConfig() {
    	return m_follower.getConfig();
    }

    public void update(double position, double velocity) {
        m_error = m_goal - position;
        m_result = m_follower.calculate(position, velocity);
    }

    public TrajectoryFollower.TrajectorySetpoint getSetpoint() {
        return m_follower.getCurrentSetpoint();
    }

    public double get() {
        return m_result;
    }

    @Override
    public void reset() {
        m_result = 0;
        m_error = 0;
        m_follower.setGoal(m_follower.getCurrentSetpoint(), m_goal);
    }

    @Override
    public boolean isOnTarget() {
        return m_follower.isFinishedTrajectory()
                && Math.abs(m_error) < m_on_target_delta;
    }

}
