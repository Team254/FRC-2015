package com.team254.frc2015.web;

import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.team254.lib.util.SystemManager;

public class StateStreamSocket extends WebSocketAdapter
{
    HashMap<String, Boolean> subscribedKeys = new HashMap<String, Boolean>();
    private boolean running = true;
    public void onWebSocketClose(int statusCode, String reason)
    {
        super.onWebSocketClose(statusCode,reason);
        SystemManager.getInstance().unregisterStateStreamSocket(this);
    }

    public void onWebSocketConnect(Session session)
    {
        super.onWebSocketConnect(session);
        SystemManager.getInstance().registerStateStreamSocket(this);
    }

    public void onWebSocketError(Throwable cause)
    {
        System.err.println("WebSocket Error" + cause);
        SystemManager.getInstance().unregisterStateStreamSocket(this);
    }

    public void onWebSocketText(String message)
    {
      System.out.println(message);
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
              System.out.println("key: " + key);
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
      getRemote().sendStringByFuture(out);
      return true;
    }
    
    private void subscribe(String key) {
      subscribedKeys.put(key, true);
    }
    
    private void unsubscribe(String key) {
      subscribedKeys.remove(key);
    }
}
