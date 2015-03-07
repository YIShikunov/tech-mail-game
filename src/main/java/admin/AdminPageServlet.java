package admin;
import main.TimeHelper;
import templater.PageGenerator;

import main.AccountService;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AdminPageServlet extends HttpServlet {
    public static final String adminPageURL = "/admin";
    private AccountService accountService;

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);

        Integer users = accountService.getCountUsers();
        Integer logusers = accountService. getCountLogUsers();
        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("users", users == null ? 0 :users );
        pageVariables.put("logusers", logusers == null ? 0 :logusers );
        String timeString = request.getParameter("shutdown");
        if (timeString != null) {
            int timeMS = Integer.valueOf(timeString);
            System.out.print("Server will be down after: "+ timeMS + " ms");
            TimeHelper.sleep(timeMS);
            System.out.print("\nShutdown");
            System.exit(0);
        }
        pageVariables.put("status", "run");
        response.getWriter().println(PageGenerator.getPage("admin.tml", pageVariables));
    }
}
