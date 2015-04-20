package com.team254.lib.util;


import edu.wpi.first.wpilibj.DigitalInput;

public class BinaryInputDecoder {
    DigitalInput m_bit_0;
    DigitalInput m_bit_1;
    DigitalInput m_bit_2;

    BinaryInputDecoder(DigitalInput bit0, DigitalInput bit1, DigitalInput bit2) {
        m_bit_0 = bit0;
        m_bit_1 = bit1;
        m_bit_2 = bit2;
    }

    private static int bit(boolean value) {
        return value ? 1 : 0;
    }

    public int get() {
        return decode(!m_bit_0.get(), !m_bit_1.get(), !m_bit_2.get());
    }

    public static int decode(boolean bit0, boolean bit1, boolean bit2) {
        return (bit(bit0) << 0) | (bit(bit1) << 1) | (bit(bit2) << 2);
    }

}
