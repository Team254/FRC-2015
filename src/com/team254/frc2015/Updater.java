package com.team254.frc2015;

import com.team254.frc2015.web.WebServer;
import com.team254.lib.util.Logger;
import com.team254.lib.util.Loopable;
import com.team254.lib.util.SystemManager;

public class Updater implements Loopable {

    @Override
    public void update() {
        WebServer.updateAllStateStreams();
        Logger.println(SystemManager.getInstance().get().toJSONString());
    }

}
