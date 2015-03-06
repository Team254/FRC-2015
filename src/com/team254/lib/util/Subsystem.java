package com.team254.lib.util;


public abstract class Subsystem implements Tappable {

    String name;

    public Subsystem(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract void reloadConstants();
}
