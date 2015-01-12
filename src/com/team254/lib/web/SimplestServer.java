package com.team254.lib.web;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;

import com.team254.lib.web.handlers.GetAllStatesHandler;
import com.team254.lib.web.handlers.HelloHandler;

public class SimplestServer
{
  
    public static void startServer() throws Exception
    {
        Server server = new Server(8080);

        ContextHandler context = new ContextHandler("/");
        context.setHandler(new HelloHandler());

        ContextHandler getAllStatesContext = new ContextHandler("/states");
        getAllStatesContext.setHandler(new GetAllStatesHandler());
        
        // Each context has a specific route
        // This combines all the contexts into a collection
        ContextHandlerCollection contexts = new ContextHandlerCollection();
        contexts.setHandlers(new Handler[] {context, getAllStatesContext });
        server.setHandler(contexts);

        server.start();
        server.join();
    }
}
