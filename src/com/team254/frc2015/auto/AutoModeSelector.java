package com.team254.frc2015.auto;

import com.team254.frc2015.auto.modes.Test3BinAutoMode;
import com.team254.frc2015.auto.modes.TestDriveAutoMode;
import com.team254.frc2015.auto.modes.TestElevatorAutoMode;
import com.team254.frc2015.auto.modes.TestTurnAutoMode;
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
	
	public  void registerAutonomous(AutoMode auto) {
		autoModes.add(auto);
	}
	
	public AutoModeSelector() {
		registerAutonomous(new Test3BinAutoMode());
		registerAutonomous(new TestDriveAutoMode());
		registerAutonomous(new TestElevatorAutoMode());
		registerAutonomous(new TestTurnAutoMode());
	}
	
	public AutoMode getAutoMode() {
		return (AutoMode) autoModes.get(selectedIndex);
	}
	
	public ArrayList<String> getAutoModeList() {
		ArrayList<String> list = new ArrayList<String>();
		for (int i = 0; i < autoModes.size(); i++){
			list.add(autoModes.get(i).getClass().getSimpleName());
		}
		return list;
	}
	
	public JSONArray getAutoModeJSONList() {
		JSONArray list = new JSONArray();
		list.addAll(getAutoModeList());
		return list;
	}
	
	public void setAutoModeByIndex(int which) {
		selectedIndex = which;
	}

}
