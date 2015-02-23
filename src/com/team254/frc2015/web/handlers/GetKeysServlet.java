package com.team254.frc2015.web.handlers;

import com.team254.lib.util.SystemManager;
import org.json.simple.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class GetKeysServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        response.setHeader("Access-Control-Allow-Origin", "*");
        JSONObject json = SystemManager.getInstance().get();
        JSONObject out = new JSONObject();
        System.out.println(json + " | " + out);
        for (Object key : json.keySet()) {
            Object o = json.get(key);
            if (o != null) {
                out.put(key, o.getClass().getName());
            }
        }
        response.getWriter().println(out.toJSONString());
    }

}
