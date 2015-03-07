package com.team254.frc2015;

import com.team254.frc2015.web.WebServer;
import com.team254.lib.util.Loopable;

public class Updater implements Loopable {

    @Override
    public void update() {
        WebServer.updateAllStateStreams();
    }

}
