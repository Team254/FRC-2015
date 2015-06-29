package com.team254.frc2015.auto;

import com.team254.frc2015.auto.modes.DoNothingAutoMode;
import com.team254.frc2015.auto.modes.PeacockAutoMode;
import com.team254.frc2015.auto.modes.ThreeToteAutoMode;
import org.json.simple.JSONArray;

import java.util.ArrayList;

public class AutoModeSelector {
    private static AutoModeSelector instance = null;
    private ArrayList<AutoMode> autoModes = new ArrayList<AutoMode>();
    int selectedIndex = 0;
    public static AutoModeSelector getInstance() {
        if (instance == null) {
            instance = new AutoModeSelector();
        }
        return instance;
    }

    public void registerAutonomous(AutoMode auto) {
        autoModes.add(auto);
    }

    public AutoModeSelector() {
        registerAutonomous(new ThreeToteAutoMode());
        registerAutonomous(new PeacockAutoMode());
        registerAutonomous(new DoNothingAutoMode());
    }

    public AutoMode getAutoMode() {
        return autoModes.get(selectedIndex);
    }

    public ArrayList<String> getAutoModeList() {
        ArrayList<String> list = new ArrayList<String>();
        for (AutoMode autoMode : autoModes) {
            list.add(autoMode.getClass().getSimpleName());
        }
        return list;
    }

    public JSONArray getAutoModeJSONList() {
        JSONArray list = new JSONArray();
        list.addAll(getAutoModeList());
        return list;
    }

    public void setFromWebUI(int index) {
        setAutoModeByIndex(index);
    }

    private void setAutoModeByIndex(int which) {
        if (which < 0 || which >= autoModes.size()) {
            which = 0;
        }
        selectedIndex = which;
    }

}
