package com.team254.frc2015.web.handlers;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import com.team254.lib.util.SystemManager;

public class GetAllStatesServlet extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      response.setContentType("application/json;charset=utf-8");
      response.setStatus(HttpServletResponse.SC_OK);
      JSONObject json = SystemManager.getInstance().get();
      response.getWriter().println(json.toJSONString());
  }
}
