package com.team254.lib.util;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Relay;

public class CheesyCompressor {
    public static int UPDATE_HZ = 25;
    protected Relay m_compressor_relay;
    protected DigitalInput m_pressure_switch;

    Loopable updater = new Loopable() {
        @Override
        public void update() {
            boolean turn_on = !m_pressure_switch.get();
            m_compressor_relay.set(turn_on ? Relay.Value.kOn : Relay.Value.kOff);
        }
    };

    protected Looper looper = new Looper("Compressor", updater, 1.0 / (UPDATE_HZ * 1.0));

    public CheesyCompressor(Relay compressor_relay, DigitalInput pressure_switch) {
        m_compressor_relay = compressor_relay;
        m_pressure_switch = pressure_switch;
        m_compressor_relay.setDirection(Relay.Direction.kForward);
        looper.start();
    }

    public void start() {
        looper.start();
    }

    public void stop() {
        looper.stop();
        m_compressor_relay.set(Relay.Value.kOff);
    }
}
