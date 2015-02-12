package com.team254.lib.util.gyro;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableSet;
import edu.wpi.first.wpilibj.SPI;

import java.nio.ByteBuffer;

/**
 * Deals with the SPI interface to the gyro. Datasheet for the command used here can be found at:
 * "http://www.analog.com/static/imported-files/data_sheets/ADXRS453.pdf
 */
public class GyroInterface {
    // states according to ST1/ST0 bits
    public static enum StatusFlag {
        INVALID_DATA,
        VALID_DATA,
        SELF_TEST_DATA,
        RW_RESPONSE,
    }

    public static enum ErrorFlag {
        PLL_FAILURE(7),
        QUADRATURE_ERROR(6),
        NONVOLATILE_MEMORY_FAULT(5),
        RESET_INITIALIZE_FAILURE(4),
        POWER_FAILURE(3),
        CONTINUOUS_SELF_SELF_FAILURE(2),
        GENERATED_FAULT(1);
        private final int mBit;

        ErrorFlag(int bit) {
            mBit = bit;
        }
    }

    private final ImmutableSet<ErrorFlag> ALL_ERRORS = ImmutableSet.copyOf(ErrorFlag.values());

    private final SPI mSPI;

    private static final int SENSOR_DATA_CMD = 0x20000000;
    private static final int CHK_GENERATE_FAULTS_BIT = 0x02;

    public GyroInterface() {
        mSPI = new SPI(SPI.Port.kOnboardCS0);
        mSPI.setClockRate(4_000_000);
        mSPI.setChipSelectActiveLow();
        mSPI.setClockActiveHigh();
        mSPI.setSampleDataOnRising();
        mSPI.setMSBFirst();
    }

    /**
     * Initializes the gyro
     * @throws GyroException If the initialization routine fails for any reason
     */
    public void initializeGyro() throws GyroException {
        // start a self-check
        int result = doTransaction(SENSOR_DATA_CMD | CHK_GENERATE_FAULTS_BIT);
        if (result != 1) {
            System.out.println(
                    "Unexpected self-check response: 0x" + Integer.toHexString(result) +
                            " errors: " + Joiner.on(", ").join(extractErrors(result)));
        }
        // wait for the fault conditions to occur
        try {
            Thread.sleep(50);
        } catch (InterruptedException ignored) {}

        // clear latched non-fault data
        doTransaction(SENSOR_DATA_CMD);

        // actually read the self-test data
        int selfCheckResult = doTransaction(SENSOR_DATA_CMD);
        if (extractStatus(selfCheckResult) != StatusFlag.SELF_TEST_DATA) {
            throw new GyroException("Gyro not in self test: 0x" + Integer.toHexString(selfCheckResult));
        }
        if (!extractErrors(selfCheckResult).containsAll(ALL_ERRORS)) {
            throw new GyroException(
                    "Gyro self-test didn't include all errors: 0x" + Integer.toHexString(selfCheckResult));
        }

        // clear the latched self-test data
        selfCheckResult = doTransaction(SENSOR_DATA_CMD);
        if (extractStatus(selfCheckResult) != StatusFlag.SELF_TEST_DATA) {
            throw new GyroException("Gyro second self test read failed: 0x" + Integer.toHexString(selfCheckResult));
        }
    }

    public short doRead(byte address) {
        int command = (0x8 << 28) | (address << 17);
        while (true) {
            int result;
            try {
                result = doTransaction(command);
            } catch (GyroException e) {
                e.printStackTrace(System.out);
                continue;
            }
            if ((result & 0xEFE00000) != 0x4E000000) {
                System.out.println("Unexpected gyro read response: 0x" + Integer.toHexString(result) + " ... retrying");
                continue;
            }
            return (short)((result >> 5) & 0xFFFF);
        }
    }

    public static double extractAngleRate(int result) {
        short reading = (short)((result >> 10) & 0xFFFF);
        return reading * 2.0 * Math.PI / 360.0 / 80.0;
    }

    public short readPartId() {
        return doRead((byte) 0x0C);
    }

    public int readSerialNumber() {
        return (((int)doRead((byte) 0x0E)) << 16) | (int)doRead((byte) 0x10);
    }

    public int getReading() throws GyroException {
        return doTransaction(SENSOR_DATA_CMD);
    }

    /**
     * @param command The word to write
     * @return The result of the transaction
     * @throws GyroInterface.GyroException if the transaction fails
     */
    private int doTransaction(int command) throws GyroException {
        // ensure the parity bit
        if (!isOddParity(command & ~0x01)) {
            command |= 0x01;
        }
        ByteBuffer resultBuffer = ByteBuffer.allocate(4);
        int transactionSize = mSPI.transaction(ByteBuffer.allocate(4).putInt(command).array(), resultBuffer.array(), 4);
        if (transactionSize != 4) {
            throw new GyroException("Transaction failed with size: " + transactionSize);
        }
        int result = resultBuffer.getInt(0);
        // check the high-byte parity of the response
        if (!isOddParity(result & 0xffff0000)) {
            throw new GyroException("High bytes parity failure");
        }
        if (!isOddParity(result)) {
            throw new GyroException("Whole word parity failure");
        }
        return result;
    }

    private static boolean isOddParity(int word) {
        boolean isOdd = false;
        for (int i = 0; i < 32; ++i) {
            if ((word & (1 << i)) != 0) {
                isOdd = !isOdd;
            }
        }
        return isOdd;
    }

    public static StatusFlag extractStatus(int result) {
        int stBits = (result >> 26) & 0b11;
        switch (stBits) {
            case 0b00: return StatusFlag.INVALID_DATA;
            case 0b01: return StatusFlag.VALID_DATA;
            case 0b10: return StatusFlag.SELF_TEST_DATA;
            case 0b11: return StatusFlag.RW_RESPONSE;
            default: throw new RuntimeException("wtf");
        }
    }

    public static ImmutableSet<ErrorFlag> extractErrors(int result) {
        ImmutableSet.Builder<ErrorFlag> builder = ImmutableSet.<ErrorFlag>builder();
        for (ErrorFlag errorFlag : ErrorFlag.values()) {
            if ((result & (1 << errorFlag.mBit)) != 0) {
                builder.add(errorFlag);
            }
        }
        return builder.build();
    }

    public class GyroException extends Exception {
        public GyroException(String s) {
            super(s);
        }
    }
}
