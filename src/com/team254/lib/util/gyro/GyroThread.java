package com.team254.lib.util.gyro;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableSet;

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
    private final Timer mTimer = new Timer("Gyro", true);
    // owned by the background thread
    private final GyroInterface mGyroInterface = new GyroInterface();

    // thread communication variables
    private volatile boolean mVolatileHasData = false;
    private volatile double mVolatileAngle = 0;
    private volatile boolean mVolatileShouldReZero = true;

    public void start() {
        synchronized (mTimer) {
            mTimer.schedule(new InitTask(), 0);
        }
    }

    public boolean hasData() {
        return mVolatileHasData;
    }

    public double getAngle() {
        return mVolatileAngle;
    }

    public void rezero() {
        mVolatileShouldReZero = true;
    }

    /**
     * Initializes the gyro, verifying the its self-test results
     */
    private class InitTask extends TimerTask {
        @Override
        public void run() {
            while (true) {
                try {
                    mGyroInterface.initializeGyro();
                    break;
                } catch (GyroInterface.GyroException e) {
                    System.out.println("Gyro failed to initialize: " + e.getMessage());
                    synchronized (mTimer) {
                        mTimer.schedule(new InitTask(), 500);
                    }
                }
            }
            System.out.println("gyo initialized, part ID: 0x" + Integer.toHexString(mGyroInterface.readPartId()));
            synchronized (mTimer) {
                mTimer.scheduleAtFixedRate(new UpdateTask(), 0, (int) (1000.0 / K_READING_RATE));
            }
        }
    }

    /**
     * Updates the actual gyro data (zeroing and accumulation)
     */
    private class UpdateTask extends TimerTask {
        private int mRemainingStartupCycles = K_STARTUP_SAMPLES;
        private boolean mIsZerod = false;
        private double[] mZeroRateSamples = new double[K_ZEROING_SAMPLES];
        private int mZeroRateSampleIndex = 0;
        private boolean mHasEnoughZeroingSamples;
        private double mZeroBias;
        private double mAngle = 0;

        @Override
        public void run() {
            int reading;
            try {
                reading = mGyroInterface.getReading();
            } catch (GyroInterface.GyroException e) {
                System.out.println("Gyro read failed: " + e.getMessage());
                return;
            }
            GyroInterface.StatusFlag status = GyroInterface.extractStatus(reading);
            ImmutableSet<GyroInterface.ErrorFlag> errors = GyroInterface.extractErrors(reading);
            if (GyroInterface.StatusFlag.VALID_DATA != status || !errors.isEmpty()) {
                System.out.println("Gyro read failed. Status: " + status + ". Errors: " + Joiner.on(", ").join(errors));
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
                mIsZerod = true;
                return;
            }

            mAngle += (unbiasedAngleRate - mZeroBias) / K_READING_RATE;
            mVolatileAngle = mAngle;
            mVolatileHasData = true;
        }
    }
}
