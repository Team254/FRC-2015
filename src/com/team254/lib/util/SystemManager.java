package com.team254.lib.util;

import java.util.Collection;
import java.util.Hashtable;
import java.util.HashMap;

/**
 *
 * @author bg
 * Manages a set of serializable objects
 */
public class SystemManager {
  private Hashtable<String, Serializable> sysmap;

  public void add(String id, Serializable v) {
    sysmap.put(id, v);
  }

  // Returns a map of all states
  public HashMap<String, String> get() {
    HashMap<String, String> states = new HashMap<String, String>();

    Collection<String> keys = this.sysmap.keySet();

    for (String k : keys) {
      String v = this.sysmap.get(k).serialize();
      states.put(k, v);
    }
    return states;
  }

  public HashMap<String, String> get(String k) {
    HashMap<String, String> state = new HashMap<String, String>();
    state.put(k, sysmap.get(k).serialize());
    return state;
  }
  
  // Returns a map of states for the devices specified in args
  public HashMap<String, String> get(String []args) {
    HashMap<String, String> states = new HashMap<String, String>();
    for (String k : args) {
      String v = this.sysmap.get(k).serialize();
      states.put(k, v);
    }
    return states;
  }


}
