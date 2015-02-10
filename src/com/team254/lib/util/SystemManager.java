package com.team254.lib.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.HashMap;
import java.util.regex.Pattern;

import org.json.simple.JSONObject;

import com.team254.frc2015.web.StateStreamSocket;

/**
 *
 * @author bg Manages a set of serializable objects
 */
public class SystemManager {
	private static SystemManager inst = null;

	private Hashtable<String, Tappable> sysmap;

	public static SystemManager getInstance() {
		if (inst == null) {
			inst = new SystemManager();
		}
		return inst;
	}

	public SystemManager() {
		this.sysmap = new Hashtable<String, Tappable>();
	}

	public void add(Tappable v) {
		sysmap.put(v.getName(), v);
	}

	public void add(Collection<Tappable> values) {
		for (Tappable v : values) {
			sysmap.put(v.getName(), v);
		}
	}

	// Returns a map of all states
	public JSONObject get() {
		JSONObject states = new JSONObject();
		Collection<String> keys = this.sysmap.keySet();

		for (String k : keys) {
			Collection<Serializable> serials = this.sysmap.get(k)
					.getComponents();
			for (Serializable s : serials) {
				states.put(k + "." + s.getName(), s.getState());
			}

		}
		return states;
	}

	private Object getValueForKey(String k) {
		String[] pieces = k.split(Pattern.quote("."));
		if (pieces.length != 2) {
			return null;
		}
		String base = pieces[0];
		String key = pieces[1];

		Tappable t = sysmap.get(base);
		Collection<Serializable> states = t.getComponents();

		for (Serializable s : states) {
			if (s.getName().equals(key)) {
				return s.getState();
			}
		}
		return null;
	}

	public JSONObject get(String k) {
		JSONObject state = new JSONObject();
		state.put(k, getValueForKey(k));
		return state;
	}

	// Returns a map of states for the devices specified in args
	public JSONObject get(String[] args) {
		JSONObject states = new JSONObject();
		for (String k : args) {
			states.put(k, getValueForKey(k));
		}
		return states;
	}
}
