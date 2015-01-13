package com.team254.lib;

import com.team254.lib.util.Serializable;
import com.team254.lib.util.SystemManager;

import edu.wpi.first.wpilibj.Talon;

public class ChezyTalon extends Talon implements Serializable {
  public String name;
  
  public ChezyTalon(String name, int channel) {
    super(channel);
    this.name = name;
    SystemManager.getInstance().add(this);
  }

  @Override
  public String serialize() {
    double val = this.get();
    return Double.toString(val);
  }
  
  public String getName() {
    return this.name;
  }
}
