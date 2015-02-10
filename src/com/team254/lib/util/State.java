package com.team254.lib.util;

public class State implements Serializable{
	String m_name;
	Double m_double_value = null;
	Integer m_int_value = null;
	Boolean m_boolean_value = null;
	
	public State(String name, Double value) {
		m_name = name;
		m_double_value = value;
	}
	
	public State(String name, Integer value) {
		m_name = name;
		m_int_value = value;
	}
	
	public State(String name, Boolean value) {
		m_name = name;
		m_boolean_value = value;
	}
	@Override
	public Object getState() {
		if (m_double_value != null) {
			return m_double_value;
		}
		if (m_int_value != null) {
			return m_int_value;
		}
		if (m_boolean_value != null) {
			return m_boolean_value;
		}
		return null;
	}

	@Override
	public String getName() {
		return m_name;
	}

	@Override
	public String getType() {
		if (m_double_value != null) {
			return Double.class.getName();
		}
		if (m_int_value != null) {
			return Integer.class.getName();
		}
		if (m_boolean_value != null) {
			return Boolean.class.getName();
		}
		return null;
	}

}
