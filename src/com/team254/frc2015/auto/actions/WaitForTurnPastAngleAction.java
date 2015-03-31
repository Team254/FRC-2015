package com.team254.frc2015.auto.actions;

/**
 * Created by tombot on 3/24/15.
 */
public class WaitForTurnPastAngleAction extends TimeoutAction {

    private double m_angle = 0;
    private boolean m_positive = false;

    public WaitForTurnPastAngleAction(double angle, boolean positive, double timeout) {
        super(timeout);
        m_angle = angle;
        m_positive = positive;
    }


    @Override
    public boolean isFinished() {
        double angle = drive.getPhysicalPose().getHeading();
        return m_positive ? angle >= m_angle : angle <= m_angle || super.isFinished();
    }

}
