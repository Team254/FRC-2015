package com.team254.frc2015.web.handlers;

import com.team254.frc2015.Constants;
import com.team254.lib.util.ConstantsBase.Constant;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

public class ConstantsServlet extends HttpServlet {

    private void buildPage(HttpServletResponse response) throws IOException {
        Constants constants = new Constants();

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter out = response.getWriter();

        out.println("<html>");
        out.println("<body>");
        out.println("<form method='post'>");
        out.println("<table cellspacing='5'>");
        Collection<Constant> cs = constants.getConstants();
        for (Constant c : cs) {
            out.println("<tr>");
            out.println("<td>(" + c.type + ")</td>");
            out.println("<td>" + c.name + "</td>");
            out.println("<td><input type='text' name='" + c.name + "' id='" + c.name + "' value='"
                    + c.value + "'></td>");
            out.println("</tr>");
        }
        out.println("</table>");
        out.println("<input type='submit' value='Save'>");
        out.println("<input type='reset' value='Reset'>");
        out.println("</form>");
        out.println("</body>");
        out.println("</html>");
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {
        buildPage(response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Constants constants = new Constants();
        boolean changed = false;
        for (String key : request.getParameterMap().keySet()) {
            String value = request.getParameter(key);
            Constant c = constants.getConstant(key);
            if (c != null) {
                if (double.class.equals(c.type) || Double.class.equals(c.type)) {
                    double v = Double.parseDouble(value);
                    constants.setConstant(key, v);
                    changed = true;
                } else if (int.class.equals(c.type) || Integer.class.equals(c.type)) {
                    int v = Integer.parseInt(value);
                    constants.setConstant(key, v);
                    changed = true;
                } else if (String.class.equals(c.type)) {
                    constants.setConstant(key, value);
                    changed = true;
                }
            }

        }
        constants.saveToFile();
        buildPage(response);
    }
}
