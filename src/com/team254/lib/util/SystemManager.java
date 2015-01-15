package com.team254.lib.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.HashMap;

import org.json.simple.JSONObject;

import com.team254.frc2015.web.StateStreamSocket;

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

  public void add(Serializable v) {
    sysmap.put(v.getName(), v);
  }
  
  public void add(Collection<Serializable> values) {
    for (Serializable v : values) {
	  sysmap.put(v.getName(), v);
    }
  }

  // Returns a map of all states
  public JSONObject get() {
    JSONObject states = new JSONObject();
    Collection<String> keys = this.sysmap.keySet();

    for (String k : keys) {
      Object v = this.sysmap.get(k).getState();
      states.put(k, v);
    }
    return states;
  }

  public JSONObject get(String k) {
    JSONObject state = new JSONObject();
    state.put(k, sysmap.get(k).getState());
    return state;
  }
  
  // Returns a map of states for the devices specified in args
  public JSONObject get(String []args) {
    JSONObject states = new JSONObject();
    for (String k : args) {
      Serializable v =  this.sysmap.get(k);
      if(v != null) {
        states.put(k, v.getState());
      }
      
    }
    return states;
  }
}
