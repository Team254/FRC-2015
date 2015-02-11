package com.team254.frc2015.web;

import java.util.HashMap;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;
import org.eclipse.jetty.websocket.api.WebSocketException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.team254.lib.util.SystemManager;

public class StateStreamSocket extends WebSocketAdapter
{
    HashMap<String, Boolean> subscribedKeys = new HashMap<String, Boolean>();
    private boolean running = true;
    
    public boolean canBeUpdated() {
      return running && !subscribedKeys.isEmpty();
    }
 
    public void onWebSocketClose(int statusCode, String reason)
    {
        super.onWebSocketClose(statusCode,reason);
        WebServer.unregisterStateStreamSocket(this);
    }

    public void onWebSocketConnect(Session session)
    {
        super.onWebSocketConnect(session);
        WebServer.registerStateStreamSocket(this);
    }

    public void onWebSocketError(Throwable cause)
    {
        System.err.println("WebSocket Error" + cause);
        WebServer.unregisterStateStreamSocket(this);
    }

    public void onWebSocketText(String message)
    {
        JSONParser parser=new JSONParser();
        Object obj;
        try {
          obj = parser.parse(message);
        } catch (ParseException e) {
          e.printStackTrace();
          return;
        }
        
        JSONObject cmd = (JSONObject) obj;
        if (cmd != null) {
          String action = (String) cmd.get("action");
          if ("pause".equals(action)) {
            running = false;
          } else if ("start".equals(action)) {
            running = true;
          } if ("subscribe".equals(action)) {
            JSONArray keys = (JSONArray) cmd.get("keys");
            for (Object key : keys) {
              subscribe((String)key);
            }
          } else if ("unsubscribe".equals(action)) {
            JSONArray keys = (JSONArray) cmd.get("keys");
            for (Object key : keys) {
              unsubscribe((String)key);
            }
          }
        }
        if (isConnected())
        {
            update();
        }
    }

    @Override
    public void onWebSocketBinary(byte[] arg0, int arg1, int arg2)
    {
    }
    
    public boolean update() {
      if (!isConnected()) {
        return false;
      }
      if (!running || subscribedKeys.keySet().size() == 0) {
        return true;
      }
      String[] keys = subscribedKeys.keySet().toArray(new String[subscribedKeys.size()]);
      JSONObject states = SystemManager.getInstance().get(keys);
      String out = states.toJSONString();
      try {
    	  getRemote().sendStringByFuture(out);
      } catch (WebSocketException e) {
    	  System.err.println("Caught WebSocketException in StateStreamSocket");
    	  return false;
      }
      return true;
    }
    
    private void subscribe(String key) {
      subscribedKeys.put(key, true);
    }
    
    private void unsubscribe(String key) {
      subscribedKeys.remove(key);
    }
}
