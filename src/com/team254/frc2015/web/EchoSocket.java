package com.team254.frc2015.web;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;

public class EchoSocket extends WebSocketAdapter
{

    public void onWebSocketClose(int statusCode, String reason)
    {
        super.onWebSocketClose(statusCode,reason);
        System.out.println("WebSocket Close: {} - {} " + statusCode + reason);
    }

    public void onWebSocketConnect(Session session)
    {
        super.onWebSocketConnect(session);
        System.out.println("WebSocket Connect: {}" + session);
        getRemote().sendStringByFuture("You are now connected to " + this.getClass().getName());
    }

    public void onWebSocketError(Throwable cause)
    {
        System.err.println("WebSocket Error" + cause);
    }

    public void onWebSocketText(String message)
    {
        if (isConnected())
        {
            System.out.println("Echoing back text message [{}]" + message);
            getRemote().sendStringByFuture(message);
        }
    }

    @Override
    public void onWebSocketBinary(byte[] arg0, int arg1, int arg2)
    {
        /* ignore */
    }
}
