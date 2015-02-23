package com.team254.frc2015.web.handlers;

import com.team254.frc2015.auto.AutoModeSelector;
import org.json.simple.JSONArray;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class GetAutoModesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        response.setHeader("Access-Control-Allow-Origin", "*");
        JSONArray allAuto = AutoModeSelector.getInstance().getAutoModeJSONList();
        response.getWriter().println(allAuto.toJSONString());
    }

}
