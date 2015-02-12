package com.team254.lib;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.team254.lib.util.Pose;
import com.team254.lib.util.Pose.RelativePoseGenerator;

public class PoseTest {

	@Test
	public void testZeroRelativePose() {
		Pose base_pose = new Pose(0,0,0,0,0,0);
		RelativePoseGenerator rel_pose = base_pose.new RelativePoseGenerator();
		Pose new_pose = new Pose(10,11,1,2,5,6);
		Pose diff_pose = rel_pose.get(new_pose);
		assertTrue("Poses should be the same with a zero base", diff_pose.equals(new_pose));
	}
	
	@Test
	public void testNonZeroRelativePose() {
		Pose base_pose = new Pose(1,2,3,4,5,6);
		RelativePoseGenerator rel_pose = base_pose.new RelativePoseGenerator();
		Pose new_pose = new Pose(11,12,13,14,15,16);
		Pose diff_pose = rel_pose.get(new_pose);
		Pose expected_pose = new Pose(10,10,13,14,10,16); // uses new pose velocity
		assertTrue("Poses should be the same with a non zero base", diff_pose.equals(expected_pose));
		
	}
}
