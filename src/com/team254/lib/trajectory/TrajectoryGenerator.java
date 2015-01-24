package com.team254.lib.trajectory;

/**
 * Factory class for creating Trajectories.
 *
 * @author Jared341
 */
public class TrajectoryGenerator {

	// /// INNER CLASSES /////
	public static class Config {

		public double dt;
		public double max_vel;
		public double max_acc;
	}

	// /// METHODS /////
	/**
	 * Generate a trajectory from a start state to a goal state.
	 *
	 * Read the notes on each of the Strategies defined above, as certain
	 * arguments are ignored for some strategies.
	 *
	 * @param config
	 *            Definition of constraints and sampling rate
	 * @param start_vel
	 *            The starting velocity
	 * @param start_heading
	 *            The starting heading
	 * @param goal_pos
	 *            The goal position
	 * @param goal_vel
	 *            The goal velocity
	 * @return A Trajectory that satisfies the relevant constraints and end
	 *         conditions.
	 */
	public static Trajectory generate(Config config, double start_vel,
			double goal_pos, double goal_vel) {
		Trajectory traj;

		// How fast can we go given maximum acceleration and deceleration?
		double start_discount = .5 * start_vel * start_vel / config.max_acc;
		double end_discount = .5 * goal_vel * goal_vel / config.max_acc;

		double adjusted_max_vel = Math.min(
				config.max_vel,
				Math.sqrt(config.max_acc * goal_pos - start_discount
						- end_discount));
		double t_rampup = (adjusted_max_vel - start_vel) / config.max_acc;
		double x_rampup = start_vel * t_rampup + .5 * config.max_acc * t_rampup
				* t_rampup;
		double t_rampdown = (adjusted_max_vel - goal_vel) / config.max_acc;
		double x_rampdown = adjusted_max_vel * t_rampdown - .5 * config.max_acc
				* t_rampdown * t_rampdown;
		double x_cruise = goal_pos - x_rampdown - x_rampup;

		// The +.5 is to round to nearest
		int time = (int) ((t_rampup + t_rampdown + x_cruise / adjusted_max_vel)
				/ config.dt + .5);

		// Compute the length of the linear filters and impulse.
		int f1_length = (int) Math.ceil((adjusted_max_vel / config.max_acc)
				/ config.dt);
		double impulse = (goal_pos / adjusted_max_vel) / config.dt - start_vel
				/ config.max_acc / config.dt + start_discount + end_discount;
		traj = secondOrderFilter(f1_length, 1, config.dt, start_vel,
				adjusted_max_vel, impulse, time);

		return traj;
	}

	private static Trajectory secondOrderFilter(int f1_length, int f2_length,
			double dt, double start_vel, double max_vel, double total_impulse,
			int length) {
		if (length <= 0) {
			return null;
		}
		Trajectory traj = new Trajectory(length);

		Trajectory.Segment last = new Trajectory.Segment();
		// First segment is easy
		last.pos = 0;
		last.vel = start_vel;
		last.acc = 0;
		last.dt = dt;

		// f2 is the average of the last f2_length samples from f1, so while we
		// can recursively compute f2's sum, we need to keep a buffer for f1.
		double[] f1 = new double[length];
		f1[0] = (start_vel / max_vel) * f1_length;
		double f2;
		for (int i = 0; i < length; ++i) {
			// Apply input
			double input = Math.min(total_impulse, 1);
			if (input < 1) {
				// The impulse is over, so decelerate
				input -= 1;
				total_impulse = 0;
			} else {
				total_impulse -= input;
			}

			// Filter through F1
			double f1_last;
			if (i > 0) {
				f1_last = f1[i - 1];
			} else {
				f1_last = f1[0];
			}
			f1[i] = Math.max(0.0, Math.min(f1_length, f1_last + input));

			f2 = 0;
			// Filter through F2
			for (int j = 0; j < f2_length; ++j) {
				if (i - j < 0) {
					break;
				}

				f2 += f1[i - j];
			}
			f2 = f2 / f1_length;

			// Velocity is the normalized sum of f2 * the max velocity
			traj.segments_[i].vel = f2 / f2_length * max_vel;

			traj.segments_[i].pos = (last.vel + traj.segments_[i].vel) / 2.0
					* dt + last.pos;

			// Acceleration and jerk are the differences in velocity and
			// acceleration, respectively.
			traj.segments_[i].acc = (traj.segments_[i].vel - last.vel) / dt;
			traj.segments_[i].dt = dt;

			last = traj.segments_[i];
		}

		return traj;
	}

}
