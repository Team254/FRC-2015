package com.team254.lib.util;

import java.util.HashMap;
import java.util.Set;

public class StateHolder {
    private HashMap<String, Object> states = new HashMap<String, Object>();

    public void put(String key, Object value) {
        states.put(key, value);
    }

    public Object get(String key) {
        return states.get(key);
    }

    public Set<String> keySet() {
        return states.keySet();
    }
}
