package com.team254.lib;

import static org.junit.Assert.*;

import org.junit.Test;

import com.team254.lib.util.Latch;

public class LatchTest {

  @Test
  public void testTrueHold() {
    Latch l = new Latch();
    boolean result = l.update(false);
    assertFalse("Latch should be false when first updated", result);
    result = l.update(true);
    assertTrue("Latch should be true when first updated with true", result);
    result = l.update(true);
    assertFalse("Latch should be false when updated with true again", result);
    result = l.update(false);
    assertFalse("Latch should be false when updated with false again", result);
  }

}
