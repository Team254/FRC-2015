package com.team254.frc2015.web.handlers;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.team254.frc2015.auto.AutoModeSelector;


public class GetCurrentAutoModeServlet extends HttpServlet {
  
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	  response.setContentType("application/json;charset=utf-8");
      response.setStatus(HttpServletResponse.SC_OK);
      response.setHeader("Access-Control-Allow-Origin", "*");
      response.getWriter().println("\"" + AutoModeSelector.getInstance().getAutoMode().getClass().getName() + "\"");
  }

}
