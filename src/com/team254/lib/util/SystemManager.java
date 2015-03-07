package com.team254.lib.util;

import org.json.simple.JSONObject;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * @author bg Manages a set of serializable objects
 */
public class SystemManager {
    private static SystemManager inst = null;

    private class TappableHolder {
        Tappable m_tappable;
        StateHolder m_state_holder = new StateHolder();

        public TappableHolder(Tappable t) {
            m_tappable = t;
        }

        public StateHolder getStateHolder() {
            return m_state_holder;
        }

        public void update() {
            m_tappable.getState(m_state_holder);
        }
    }

    private HashMap<String, TappableHolder> m_tappables;

    public static SystemManager getInstance() {
        if (inst == null) {
            inst = new SystemManager();
        }
        return inst;
    }

    public SystemManager() {
        this.m_tappables = new HashMap<String, TappableHolder>();
    }

    public void add(Tappable v) {
        TappableHolder th = new TappableHolder(v);
        m_tappables.put(v.getName(), th);
    }

    public void add(Collection<Tappable> values) {
        for (Tappable v : values) {
            TappableHolder th = new TappableHolder(v);
            m_tappables.put(v.getName(), th);
        }
    }

    private void updateStates(String system_key) {
        m_tappables.get(system_key).update();
    }

    private void updateAllStates() {
        Set<String> keys = m_tappables.keySet();
        for (String key : keys) {
            updateStates(key);
        }
    }

    // Returns a map of all states
    public JSONObject get() {
        JSONObject states = new JSONObject();
        Collection<String> system_keys = this.m_tappables.keySet();

        updateAllStates();

        for (String system_key : system_keys) {
            TappableHolder th = m_tappables.get(system_key);
            StateHolder sh = th.getStateHolder();
            Set<String> th_keys = sh.keySet();
            for (String th_key : th_keys) {
                states.put(system_key + "." + th_key, sh.get(th_key));
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

        TappableHolder th = m_tappables.get(base);
        if (th == null) {
            return null;
        }

        StateHolder sh = th.getStateHolder();
        if (sh == null) {
            return null;
        }

        return sh.get(key);
    }

    public JSONObject get(String k) {
        return get(new String[]{k});
    }

    // Returns a map of states for the devices specified in args
    public JSONObject get(String[] args) {
        updateAllStates();
        JSONObject states = new JSONObject();
        for (String k : args) {
            states.put(k, getValueForKey(k));
        }
        return states;
    }
}
