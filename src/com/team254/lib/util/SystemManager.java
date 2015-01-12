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
  private static SystemManager inst = null;

  private Hashtable<String, Serializable> sysmap;
  
  public static SystemManager getInstance() {
    if (inst == null) {
      inst = new SystemManager();
    }
    return inst;
  }

  public SystemManager() {
    this.sysmap = new Hashtable<String, Serializable>();
  }

  public static void add(Serializable v) {
    sysmap.put(v.getName(), v);
  }

  // Returns a map of all states
  public static HashMap<String, String> get() {
    HashMap<String, String> states = new HashMap<String, String>();

    Collection<String> keys = sysmap.keySet();

    for (String k : keys) {
      String v = sysmap.get(k).serialize();
      states.put(k, v);
    }
    return states;
  }

  public static HashMap<String, String> get(String k) {
    HashMap<String, String> state = new HashMap<String, String>();
    state.put(k, sysmap.get(k).serialize());
    return state;
  }

  // Returns a map of states for the devices specified in args
  public static HashMap<String, String> get(String []args) {
    HashMap<String, String> states = new HashMap<String, String>();
    for (String k : args) {
      String v = sysmap.get(k).serialize();
      states.put(k, v);
    }
    return states;
  }

}
