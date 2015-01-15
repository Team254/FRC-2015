package com.team254.frc2015.web;

import java.net.URL;
import java.util.ArrayList;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;

import com.team254.frc2015.web.handlers.BaseServlet;
import com.team254.frc2015.web.handlers.GetAllStatesServlet;
import com.team254.frc2015.web.handlers.GetKeysServlet;
import com.team254.lib.util.TaskQueue;

public class WebServer
{
    private static Server server;
    private static Thread serverThread;
    private static ArrayList<StateStreamSocket> updateStreams = new ArrayList<StateStreamSocket>();
    private static TaskQueue streamUpdate = new TaskQueue(200);
    
    public static void startServer()
    {
        if (server != null) {
          return;
        }
        server = new Server(8080);
  
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);
  
        // Add websocket servlet
        ServletHolder wsHolder = new ServletHolder("echo",new StateStreamServlet());
        context.addServlet(wsHolder,"/state");
        
        ServletHolder statesHolder = new ServletHolder("states", new GetAllStatesServlet());
        context.addServlet(statesHolder, "/all_states");
        
        ServletHolder keysHolder = new ServletHolder("keys", new GetKeysServlet());
        context.addServlet(keysHolder, "/keys");
        
        String appDir = WebServer.class.getClassLoader().getResource("app/").toExternalForm();
        ServletHolder holderPwd = new ServletHolder("default", new DefaultServlet());
        holderPwd.setInitParameter("resourceBase", appDir);
        holderPwd.setInitParameter("dirAllowed", "true");
        context.addServlet(holderPwd, "/");
        
        serverThread = new Thread(new Runnable() {
          @Override
          public void run() {
            try
            {
                server.start();
                server.join();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
          }
        });
        serverThread.setPriority(Thread.MIN_PRIORITY);
        serverThread.start();
        streamUpdate.start();
    }
    
    public static void registerStateStreamSocket(StateStreamSocket s) {
      updateStreams.add(s);
    }
    
    public static void unregisterStateStreamSocket(StateStreamSocket s) {
      updateStreams.remove(s);
    }
    
    public static void updateAllStateStreams() {     
      boolean runUpdate = false;
      for (StateStreamSocket s : updateStreams) {
        runUpdate |= (s != null && s.canBeUpdated());
        if (runUpdate) {
          break;
        }
      } 
      if (runUpdate) {
        streamUpdate.addTask(new Runnable() {
          public void run() {
            for (StateStreamSocket s : updateStreams) {
              if (s != null && s.isConnected() && !s.canBeUpdated()) {
                ;
              } else if (s == null || !s.isConnected() || !s.update()) {
                updateStreams.remove(s);
              }
            }
          }
        });
      }
    }
}
