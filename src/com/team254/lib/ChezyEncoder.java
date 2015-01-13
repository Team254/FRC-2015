package com.team254.lib;

import com.team254.lib.util.Serializable;
import com.team254.lib.util.SystemManager;

import edu.wpi.first.wpilibj.Encoder;

public class ChezyEncoder extends Encoder implements Serializable {

  private String name;
  public ChezyEncoder(String name, int aChannel, int bChannel) {
    super(aChannel, bChannel);
    this.name = name;
    SystemManager.getInstance().add(this);
  }

  @Override
  public String serialize() {
    return Integer.toString(get());
  }

  @Override
  public String getName() {
    return name;
  }

}
