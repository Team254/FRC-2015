package com.team254.lib;

import com.team254.lib.util.Serializable;
import com.team254.lib.util.SystemManager;

import edu.wpi.first.wpilibj.Talon;

public class ChezyTalon extends Talon implements Serializable {
  private String name;

  public ChezyTalon(int channel, String name) {
    super(channel);
    this.name = name;
    SystemManager.getInstance().add(this);
  }

  public String serialize() {
    double val = this.get();
    return Double.toString(val);
  }

  public String getName() {
    return this.name;
  }
}
