package edu.wpi.first.wpilibj;

public abstract class ChezyInterruptHandlerFunction<T> extends InterruptHandlerFunction<T> {
    public abstract void interruptFired(int interruptAssertedMask, T param);
}
