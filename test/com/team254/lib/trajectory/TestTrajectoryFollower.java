package com.team254.lib.trajectory;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestTrajectoryFollower {

	@Test
	public void test() {
		TrajectoryFollower follower = new TrajectoryFollower();
		TrajectoryFollower.TrajectoryConfig config = new TrajectoryFollower.TrajectoryConfig();
		config.dt = 0.005;
		config.max_acc = 180.0;
		config.max_vel = 60.0;
		TrajectoryFollower.TrajectorySetpoint setpoint = new TrajectoryFollower.TrajectorySetpoint();
		setpoint.acc = 0;
		setpoint.vel = 0;
		setpoint.pos = 0;
		follower.configure(0.1, 0.0, 0.0, 1.0, 0.0, config);
		follower.setGoal(setpoint, 100.0);

		int num_cycles = 0;
		while (!follower.isFinishedTrajectory()) {
			double command = follower.calculate(setpoint.pos, setpoint.vel);
			setpoint = follower.getCurrentSetpoint();
			System.out.println("Result: " + setpoint + "; Command: " + command);
			assert (setpoint.vel > -1E-3);
			assert (command > -1E-3);
			++num_cycles;
		}
		follower.calculate(setpoint.pos, setpoint.vel);
		setpoint = follower.getCurrentSetpoint();
		System.out.println("Took " + num_cycles + " cycles");
		num_cycles = 0;
		assertEquals(setpoint.pos, 100.0, 1E-3);
		assertEquals(setpoint.vel, 0.0, 1E-3);

		// Now go -100 units
		follower.setGoal(setpoint, 0.0);
		while (!follower.isFinishedTrajectory()) {
			double command = follower.calculate(setpoint.pos, setpoint.vel);
			setpoint = follower.getCurrentSetpoint();
			System.out.println("Result: " + setpoint + "; Command: " + command);
			assert (setpoint.vel < 1E-3);
			assert (command < 1E-3);
			++num_cycles;
		}
		follower.calculate(setpoint.pos, setpoint.vel);
		setpoint = follower.getCurrentSetpoint();
		System.out.println("Took " + num_cycles + " cycles");
		num_cycles = 0;
		assertEquals(setpoint.pos, 0.0, 1E-3);
		assertEquals(setpoint.vel, 0.0, 1E-3);

		// Now we will go back to 100, but start with an initial velocity
		// upwards.
		setpoint.pos = 0;
		setpoint.vel = 50.0;
		setpoint.acc = 0;
		follower.setGoal(setpoint, 100.0);
		while (!follower.isFinishedTrajectory()) {
			double command = follower.calculate(setpoint.pos, setpoint.vel);
			setpoint = follower.getCurrentSetpoint();
			System.out.println("Result: " + setpoint + "; Command: " + command);
			assert (setpoint.vel > -1E-3);
			assert (command > -1E-3);
			++num_cycles;
		}
		follower.calculate(setpoint.pos, setpoint.vel);
		setpoint = follower.getCurrentSetpoint();
		System.out.println("Took " + num_cycles + " cycles");
		num_cycles = 0;
		assertEquals(setpoint.pos, 100.0, 1E-3);
		assertEquals(setpoint.vel, 0.0, 1E-3);

		// Now we will go back to 0, but start with an initial velocity upwards.
		setpoint.pos = 100.0;
		setpoint.vel = 50.0;
		setpoint.acc = 0;
		follower.setGoal(setpoint, 0.0);
		while (!follower.isFinishedTrajectory()) {
			double command = follower.calculate(setpoint.pos, setpoint.vel);
			setpoint = follower.getCurrentSetpoint();
			System.out.println("Result: " + setpoint + "; Command: " + command);
			assert (setpoint.vel > -1E-3);
			assert (command > -1E-3);
			++num_cycles;
		}
		follower.calculate(setpoint.pos, setpoint.vel);
		setpoint = follower.getCurrentSetpoint();
		System.out.println("Took " + num_cycles + " cycles");
		num_cycles = 0;
		assertEquals(setpoint.pos, 0.0, 1E-3);
		assertEquals(setpoint.vel, 0.0, 1E-3);

		// Now we will go to 2, but start with an initial velocity that is too
		// fast.
		setpoint.pos = 0.0;
		setpoint.vel = 60.0;
		setpoint.acc = 0;
		follower.setGoal(setpoint, 2.0);
		while (!follower.isFinishedTrajectory()) {
			double command = follower.calculate(setpoint.pos, setpoint.vel);
			setpoint = follower.getCurrentSetpoint();
			System.out.println("Result: " + setpoint + "; Command: " + command);
			assert (setpoint.vel > -1E-3);
			assert (command > -1E-3);
			++num_cycles;
		}
		follower.calculate(setpoint.pos, setpoint.vel);
		setpoint = follower.getCurrentSetpoint();
		System.out.println("Took " + num_cycles + " cycles");
		num_cycles = 0;
		assertEquals(setpoint.pos, 2.0, 1E-3);
		assertEquals(setpoint.vel, 0.0, 1E-3);
	}

}
