package org.example.lab1ostapchuk;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.management.ManagementFactory;
import com.sun.management.OperatingSystemMXBean;

@WebServlet(name = "specsServlet", value = "/specs-servlet")
public class PCSpecsServlet extends HttpServlet {
    private String[] specs;
    public void init(){
        OperatingSystemMXBean bean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
        specs = new String[3];
        specs[0] = "CPU: " + Runtime.getRuntime().availableProcessors() + " cores";
        specs[1] = "RAM: " + bean.getTotalMemorySize()/1048576 + "MB";
        specs[2] = "OS: " + bean.getName();
    }
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException{
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<h1>PC specs</h1>");
        for (String spec : specs){
            out.println("<br/><p>" + spec + "</p>");
        }
        out.println("</body></html>");
    }
}
