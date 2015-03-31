package com.team254.lib.trajectory;

import edu.wpi.first.wpilibj.Timer;

/**
 * PID + Feedforward controller for following a Trajectory.
 *
 * @author Jared341
 */
public class TrajectoryFollower {
    public static class TrajectoryConfig {
        public double dt;
        public double max_acc;
        public double max_vel;

        @Override
        public String toString() {
            return "dt: " + dt + ", max_acc: " + max_acc + ", max_vel: "
                    + max_vel;
        }
    }

    public static class TrajectorySetpoint {
        public double pos;
        public double vel;
        public double acc;

        @Override
        public String toString() {
            return "pos: " + pos + ", vel: " + vel + ", acc: " + acc;
        }
    }

    private double kp_;
    private double ki_;
    private double kd_;
    private double kv_;
    private double ka_;
    private double last_error_;
    private double error_sum_;
    private boolean reset_ = true;
    private double last_timestamp_;
    private TrajectorySetpoint next_state_ = new TrajectorySetpoint();

    private TrajectoryConfig config_ = new TrajectoryConfig();
    private double goal_position_;
    private TrajectorySetpoint setpoint_ = new TrajectorySetpoint();

    public TrajectoryFollower() {
    }

    public void configure(double kp, double ki, double kd, double kv,
                          double ka, TrajectoryConfig config) {
        kp_ = kp;
        ki_ = ki;
        kd_ = kd;
        kv_ = kv;
        ka_ = ka;
        config_ = config;
    }

    public void setGoal(TrajectorySetpoint current_state, double goal_position) {
        goal_position_ = goal_position;
        setpoint_ = current_state;
        reset_ = true;
        error_sum_ = 0.0;
    }

    public double getGoal() {
        return goal_position_;
    }

    public TrajectoryConfig getConfig() {
        return config_;
    }

    public void setConfig(TrajectoryConfig config) {
        config_ = config;
    }

    public double calculate(double position, double velocity) {
        double dt = config_.dt;
        if (!reset_) {
            double now = Timer.getFPGATimestamp();
            dt = now - last_timestamp_;
            last_timestamp_ = now;
        } else {
            last_timestamp_ = Timer.getFPGATimestamp();
        }

        if (isFinishedTrajectory()) {
            setpoint_.pos = goal_position_;
            setpoint_.vel = 0;
            setpoint_.acc = 0;
        } else {
            // Compute the new commanded position, velocity, and acceleration.
            double distance_to_go = goal_position_ - setpoint_.pos;
            double cur_vel = setpoint_.vel;
            double cur_vel2 = cur_vel * cur_vel;
            boolean inverted = false;
            if (distance_to_go < 0) {
                inverted = true;
                distance_to_go *= -1;
                cur_vel *= -1;
            }
            // Compute discriminants of the minimum and maximum reachable
            // velocities over the remaining distance.
            double max_reachable_velocity_disc = cur_vel2 / 2.0
                    + config_.max_acc * distance_to_go;
            double min_reachable_velocity_disc = cur_vel2 / 2.0
                    - config_.max_acc * distance_to_go;
            double cruise_vel = cur_vel;
            if (min_reachable_velocity_disc < 0 || cruise_vel < 0) {
                cruise_vel = Math.min(config_.max_vel,
                        Math.sqrt(max_reachable_velocity_disc));
            }
            double t_start = (cruise_vel - cur_vel) / config_.max_acc; // Accelerate
            // to
            // cruise_vel
            double x_start = cur_vel * t_start + .5 * config_.max_acc * t_start
                    * t_start;
            double t_end = Math.abs(cruise_vel / config_.max_acc); // Decelerate
            // to zero
            // vel.
            double x_end = cruise_vel * t_end - .5 * config_.max_acc * t_end
                    * t_end;
            double x_cruise = Math.max(0, distance_to_go - x_start - x_end);
            double t_cruise = Math.abs(x_cruise / cruise_vel);
            // Figure out where we should be one dt along this trajectory.
            if (t_start >= dt) {
                next_state_.pos = cur_vel * dt + .5 * config_.max_acc
                        * dt * dt;
                next_state_.vel = cur_vel + config_.max_acc * dt;
                next_state_.acc = config_.max_acc;
            } else if (t_start + t_cruise >= dt) {
                next_state_.pos = x_start + cruise_vel * (dt - t_start);
                next_state_.vel = cruise_vel;
                next_state_.acc = 0;
            } else if (t_start + t_cruise + t_end >= dt) {
                double delta_t = dt - t_start - t_cruise;
                next_state_.pos = x_start + x_cruise + cruise_vel * delta_t - .5
                        * config_.max_acc * delta_t * delta_t;
                next_state_.vel = cruise_vel - config_.max_acc * delta_t;
                next_state_.acc = -config_.max_acc;
            } else {
                // Trajectory ends this cycle.
                next_state_.pos = distance_to_go;
                next_state_.vel = 0;
                next_state_.acc = 0;
            }
            if (inverted) {
                next_state_.pos *= -1;
                next_state_.vel *= -1;
                next_state_.acc *= -1;
            }
            setpoint_.pos += next_state_.pos;
            setpoint_.vel = next_state_.vel;
            setpoint_.acc = next_state_.acc;

        }
        double error = setpoint_.pos - position;
        if (reset_) {
            // Prevent jump in derivative term when we have been reset.
            reset_ = false;
            last_error_ = error;
            error_sum_ = 0;
        }
        double output = kp_ * error + kd_
                * ((error - last_error_) / dt - setpoint_.vel)
                + (kv_ * setpoint_.vel + ka_ * setpoint_.acc);
        if (output < 1.0 && output > -1.0) {
            // Only integrate error if the output isn't already saturated.
            error_sum_ += error * dt;
        }
        output += ki_ * error_sum_;

        last_error_ = error;
        return output;
    }

    public boolean isFinishedTrajectory() {
        return Math.abs(setpoint_.pos - goal_position_) < 1E-3
                && Math.abs(setpoint_.vel) < 1E-2;
    }

    public TrajectorySetpoint getCurrentSetpoint() {
        return setpoint_;
    }
}
