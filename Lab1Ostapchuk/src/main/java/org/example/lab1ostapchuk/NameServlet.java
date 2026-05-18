package org.example.lab1ostapchuk;

import java.io.*;

import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet(name = "nameServlet", value = "/name-servlet")
public class NameServlet extends HttpServlet {
    private String message;

    public void init() {
        message = "Ostapchuk Mykhailo";
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<h1>" + message + "</h1>");
        out.println("</body></html>");
    }

    public void destroy() {
    }
}