package com.team254.frc2015.web;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import com.team254.frc2015.web.handlers.BaseServlet;
import com.team254.frc2015.web.handlers.GetAllStatesServlet;

public class SimplestServer
{
  
    public static void startServer() throws Exception
    {
        Server server = new Server(8080);
  
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);
  
        // Add websocket servlet
        ServletHolder wsHolder = new ServletHolder("echo",new EchoSocketServlet());
        context.addServlet(wsHolder,"/echo");
        
        ServletHolder statesHolder = new ServletHolder("states", new GetAllStatesServlet());
        context.addServlet(statesHolder, "/states");
        
        ServletHolder baseHolder = new ServletHolder("base", new BaseServlet());
        context.addServlet(baseHolder, "/");
  
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
}
