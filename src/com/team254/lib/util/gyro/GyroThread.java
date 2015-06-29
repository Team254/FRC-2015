package com.team254.lib.util.gyro;

import com.team254.lib.util.Util;
import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.TimerEventHandler;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Thread which is responsible for reading the gyro
 */
public class GyroThread {

    private static final int K_READING_RATE = 200;
    private static final int K_ZEROING_SAMPLES = 6 * K_READING_RATE;
    private static final int K_STARTUP_SAMPLES = 2 * K_READING_RATE;

    // synchronized access object
    private final Timer mTimer = new Timer("Gyro");
    // owned by the background thread
    private final GyroInterface mGyroInterface = new GyroInterface();

    private UpdateHandler mUpdateHandler = new UpdateHandler();
    private Notifier mNotifier = new Notifier(mUpdateHandler, this);

    // thread communication variables
    private volatile boolean mVolatileHasData = false;
    private volatile double mVolatileAngle = 0;
    private volatile double mVolatileRate = 0;
    private volatile boolean mVolatileShouldReZero = true;

    // owned by the accesser of this object
    private double mZeroHeading = 0;

    public void start() {
        synchronized (mTimer) {
            mTimer.schedule(new InitTask(), 0);
        }
    }

    public boolean hasData() {
        return mVolatileHasData;
    }

    public double getAngle() {
        return mVolatileAngle - mZeroHeading;
    }

    public double getRate() {
        return mVolatileRate;
    }

    public void rezero() {
        mVolatileShouldReZero = true;
    }

    public void reset() {
        mZeroHeading = mVolatileAngle;
    }

    /**
     * Initializes the gyro, verifying the its self-test results
     */
    private class InitTask extends TimerTask {
        @Override
        public void run() {
            boolean initialized = false;
            while (!initialized) {
                try {
                    mGyroInterface.initializeGyro();
                    initialized = true;
                } catch (GyroInterface.GyroException e) {
                    System.out.println("Gyro failed to initialize: " + e.getMessage());
                }
                if (!initialized) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            System.out.println("gyo initialized, part ID: 0x" + Integer.toHexString(mGyroInterface.readPartId()));
            synchronized (mNotifier) {
                mNotifier.startPeriodic(1.0 / K_READING_RATE);
            }
        }
    }

    /**
     * Updates the actual gyro data (zeroing and accumulation)
     */
    private class UpdateHandler implements TimerEventHandler {
        private int mRemainingStartupCycles = K_STARTUP_SAMPLES;
        private boolean mIsZerod = false;
        private double[] mZeroRateSamples = new double[K_ZEROING_SAMPLES];
        private int mZeroRateSampleIndex = 0;
        private boolean mHasEnoughZeroingSamples;
        private double mZeroBias;
        private double mAngle = 0;
        private double mLastTime = 0;

        @Override
        public void update(Object param) {
            int reading;
            try {
                reading = mGyroInterface.getReading();
            } catch (GyroInterface.GyroException e) {
                System.out.println("Gyro read failed: " + e.getMessage());
                return;
            }
            GyroInterface.StatusFlag status = GyroInterface.extractStatus(reading);
            List<GyroInterface.ErrorFlag> errors = GyroInterface.extractErrors(reading);
            if (GyroInterface.StatusFlag.VALID_DATA != status || !errors.isEmpty()) {
                System.out.println("Gyro read failed. Status: " + status + ". Errors: " + Util.joinStrings(", ", errors));
                return;
            }

            if (mRemainingStartupCycles > 0) {
                mRemainingStartupCycles--;
                return;
            }

            if (mVolatileShouldReZero) {
                mVolatileShouldReZero = false;
                mVolatileHasData = false;
                mIsZerod = false;
            }

            double unbiasedAngleRate = GyroInterface.extractAngleRate(reading);
            mZeroRateSamples[mZeroRateSampleIndex] = unbiasedAngleRate;
            mZeroRateSampleIndex++;
            if (mZeroRateSampleIndex >= K_ZEROING_SAMPLES) {
                mZeroRateSampleIndex = 0;
                mHasEnoughZeroingSamples = true;
            }
            if (!mIsZerod) {
                if (!mHasEnoughZeroingSamples) {
                    return;
                }
                mZeroBias = 0;
                for (Double sample : mZeroRateSamples) {
                    mZeroBias += sample / K_ZEROING_SAMPLES;
                }
                mAngle = 0;
                mVolatileAngle = 0;
                GyroThread.this.reset();
                mIsZerod = true;
                return;
            }

            double now = edu.wpi.first.wpilibj.Timer.getFPGATimestamp();
            double timeElapsed = mLastTime == 0 ? 1.0 / K_READING_RATE : now - mLastTime;
            mLastTime = now;

            mVolatileRate = unbiasedAngleRate - mZeroBias;
            mAngle += mVolatileRate * timeElapsed;
            mVolatileAngle = mAngle;
            mVolatileHasData = true;
        }
    }
}
