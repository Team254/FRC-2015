package com.team254.lib.util;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;

/**
 * ConstantsBase
 * <p>
 * Base class for storing robot constants. Anything stored as a public static
 * field will be reflected and be able to set externally
 *
 * @author Tom Bottiglieri
 */
public abstract class ConstantsBase {
    HashMap<String, Boolean> modifiedKeys = new HashMap<String, Boolean>();

    public abstract String getFileLocation();

    public static class Constant {
        public String name;
        public Class type;
        public Object value;

        public Constant(String name, Class type, Object value) {
            this.name = name;
            this.type = type;
            this.value = value;
        }

        public boolean equals(Object o) {
            String itsName = ((Constant) o).name;
            Class itsType = ((Constant) o).type;
            Object itsValue = ((Constant) o).value;
            return o instanceof Constant && this.name.equals(itsName)
                    && this.type.equals(itsType) && this.value.equals(itsValue);
        }
    }

    public File getFile() {
        String filePath = getFileLocation();
        filePath = filePath.replaceFirst("^~", System.getProperty("user.home"));
        return new File(filePath);
    }

    public boolean setConstant(String name, Double value) {
        return setConstantRaw(name, value);
    }

    public boolean setConstant(String name, Integer value) {
        return setConstantRaw(name, value);
    }

    public boolean setConstant(String name, String value) {
        return setConstantRaw(name, value);
    }

    private boolean setConstantRaw(String name, Object value) {
        boolean success = false;
        Field[] declaredFields = this.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            if (java.lang.reflect.Modifier.isStatic(field.getModifiers())
                    && field.getName().equals(name)) {
                try {
                    Object current = field.get(this);
                    field.set(this, value);
                    success = true;
                    if (!value.equals(current)) {
                        modifiedKeys.put(name, true);
                    }
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    System.out.println("Could not set field: " + name);
                }
            }
        }
        return success;
    }

    public Object getValueForConstant(String name) throws Exception {
        Field[] declaredFields = this.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            if (java.lang.reflect.Modifier.isStatic(field.getModifiers())
                    && field.getName().equals(name)) {
                try {
                    return field.get(this);
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    throw new Exception("Constant not found");
                }
            }
        }
        throw new Exception("Constant not found");
    }

    public Constant getConstant(String name) {
        Field[] declaredFields = this.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            if (java.lang.reflect.Modifier.isStatic(field.getModifiers())
                    && field.getName().equals(name)) {
                try {
                    return new Constant(field.getName(), field.getType(),
                            field.get(this));
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return new Constant("", Object.class, 0);
    }

    public Collection<Constant> getConstants() {
        List<Constant> constants = (List<Constant>) getAllConstants();
        int stop = constants.size() - 1;
        for (int i = 0; i < constants.size(); ++i) {
            Constant c = constants.get(i);
            if ("kEndEditableArea".equals(c.name)) {
                stop = i;
            }
        }
        return constants.subList(0, stop);
    }

    private Collection<Constant> getAllConstants() {
        Field[] declaredFields = this.getClass().getDeclaredFields();
        List<Constant> constants = new ArrayList<Constant>(
                declaredFields.length);
        for (Field field : declaredFields) {
            if (java.lang.reflect.Modifier.isStatic(field.getModifiers())) {
                Constant c;
                try {
                    c = new Constant(field.getName(), field.getType(),
                            field.get(this));
                    constants.add(c);
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return constants;
    }

    public JSONObject getJSONObjectFromFile() throws IOException,
            ParseException {
        File file = getFile();
        if (file == null || !file.exists()) {
            return new JSONObject();
        }
        FileReader reader;
        reader = new FileReader(file);
        JSONParser jsonParser = new JSONParser();
        return (JSONObject) jsonParser.parse(reader);
    }

    public void loadFromFile() {
        try {
            JSONObject jsonObject = getJSONObjectFromFile();
            Set keys = jsonObject.keySet();
            for (Object o : keys) {
                String key = (String) o;
                Object value = jsonObject.get(o);
                if (value instanceof Long
                        && getConstant(key).type.equals(int.class)) {
                    value = new BigDecimal((Long) value).intValueExact();
                }
                setConstantRaw(key, value);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void saveToFile() {
        File file = getFile();
        if (file == null) {
            return;
        }
        try {
            JSONObject json = getJSONObjectFromFile();
            FileWriter writer = new FileWriter(file);
            for (String key : modifiedKeys.keySet()) {
                try {
                    Object value = getValueForConstant(key);
                    json.put(key, value);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            writer.write(json.toJSONString());
            writer.close();
        } catch (IOException | ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
