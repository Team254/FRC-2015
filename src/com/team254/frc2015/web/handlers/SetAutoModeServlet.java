package com.team254.frc2015.web.handlers;

import com.team254.frc2015.auto.AutoModeSelector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class SetAutoModeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=utf-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        if (request.getParameterMap().containsKey("mode")) {
            String[] mode = request.getParameterMap().get("mode");
            if (Integer.parseInt(mode[0]) < AutoModeSelector.getInstance().getAutoModeList().size() && Integer.parseInt(mode[0]) >= 0) {
                AutoModeSelector.getInstance().setFromWebUI(Integer.parseInt(mode[0]));
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().println("\"Set auto mode to mode index " + mode[0] + "\"");
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().println("\"Index out of bounds\"");
            }
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("\"SPECIFY ?mode PARAMETER!\"");
        }
    }

}
