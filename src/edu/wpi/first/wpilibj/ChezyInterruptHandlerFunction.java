package edu.wpi.first.wpilibj;

import edu.wpi.first.wpilibj.InterruptHandlerFunction;

public abstract class ChezyInterruptHandlerFunction<T> extends InterruptHandlerFunction<T> {
	public abstract void interruptFired(int interruptAssertedMask, T param);
}
