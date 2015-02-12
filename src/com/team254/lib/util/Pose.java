package com.team254.lib.util;

public class Pose {
	public Pose(double leftDistance, double rightDistance, double leftVelocity,
			double rightVelocity, double heading, double headingVelocity) {
		this.m_left_distance = leftDistance;
		this.m_right_distance = rightDistance;
		this.m_left_velocity = leftVelocity;
		this.m_right_velocity = rightVelocity;
		this.m_heading = heading;
		this.m_heading_velocity = headingVelocity;
	}

	private double m_left_distance;
	private double m_right_distance;
	private double m_left_velocity;
	private double m_right_velocity;
	private double m_heading;
	private double m_heading_velocity;

	public double getLeftDistance() {
		return m_left_distance;
	}

	public double getHeading() {
		return m_heading;
	}

	public double getRightDistance() {
		return m_right_distance;
	}

	public double getLeftVelocity() {
		return m_left_velocity;
	}

	public double getRightVelocity() {
		return m_right_velocity;
	}

	public double getHeadingVelocity() {
		return m_heading_velocity;
	}

	public class RelativePoseGenerator {
		private Pose m_base_pose;

		public RelativePoseGenerator() {
			m_base_pose = Pose.this;
		}

		public Pose get(Pose pose) {
			return new Pose(pose.getLeftDistance()
					- m_base_pose.getLeftDistance(), pose.getRightDistance()
					- m_base_pose.getRightDistance(), pose.getLeftVelocity(),
					pose.getRightVelocity(), pose.getHeading()
							- m_base_pose.getHeading(),
					pose.getHeadingVelocity());
		}
	}
}