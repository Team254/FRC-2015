package com.team254.lib.util;

public class Pose {
	public Pose(double leftDistance, double rightDistance, double heading) {
		this.m_left_distance = leftDistance;
		this.m_right_distance = rightDistance;
		this.m_heading = heading;
	}

	private double m_left_distance;
	private double m_right_distance;
	private double m_heading;

	public double getLeftDistance() {
		return m_left_distance;
	}

	public double getHeading() {
		return m_heading;
	}

	public double getRightDistance() {
		return m_right_distance;
	}

	public class RelativePose {
		private Pose m_base_pose;

		public RelativePose(Pose basePose) {
			m_base_pose = basePose;
		}

		public Pose get(Pose pose) {
			return new Pose(pose.getLeftDistance()
					- m_base_pose.getLeftDistance(), pose.getRightDistance()
					- m_base_pose.getRightDistance(), pose.getHeading()
					- m_base_pose.getHeading());
		}
	}
}