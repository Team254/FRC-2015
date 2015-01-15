package com.team254.lib.util;

import java.lang.reflect.Field;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Victor;

public class SerialFieldContainer implements Serializable {
	
	private Field f;
	String baseName = "";
	private Object baseObject = null;
	public SerialFieldContainer(String baseName, Field f, Object baseObject) {
		this.f = f;
		this.baseName = baseName;
		this.baseObject = baseObject;
	}
	
	@Override
	public Object getState() {
		
		Object obj = null;
		try {
			obj = f.get(baseObject);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (obj == null) {
			return null;
		}
		if (obj instanceof Victor) {
			return ((Victor) obj).get();
		} else if (obj instanceof Encoder) {
			return ((Encoder) obj).get();
		}
		return obj;
	}
	@Override
	public String getName() {
		return ("".equals(baseName)) ? f.getName() : baseName + "." + f.getName();
	}

	@Override
	public String getType() {
		return f.getClass().getName();
	}

}
