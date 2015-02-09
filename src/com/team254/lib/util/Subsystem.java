package com.team254.lib.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

public class Subsystem implements Tappable {

	String name;

	public Subsystem(String name) {
		this.name = name;
		SystemManager.getInstance().add(this.getComponents());
	}

	public String getName() {
		return name;
	}

	@Override
	public final Collection<Serializable> getComponents() {
		Field[] allFields = this.getClass().getFields();
		Collection<Serializable> components = new ArrayList<Serializable>();

		for (Field f : allFields) {
			Serializable cur = null;
			if (Serializable.class.isAssignableFrom(f.getType())) {
				Object obj = null;
				try {
					obj = f.get(this);
				} catch (IllegalArgumentException | IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				cur = (Serializable) obj;
			} else {
				cur = new SerialFieldContainer(name, f, this);
			}
			if (cur != null) {
				components.add(cur);
			}
		}
		return components;
	}

	public void reloadConstants() {
	}

}
