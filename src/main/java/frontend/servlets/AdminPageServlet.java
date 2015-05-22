package frontend.servlets;
import base.AccountService.AccountService;
import utils.TimeHelper;
import utils.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

public class AdminPageServlet extends HttpServlet {
    public static final String adminPageURL = "/admin";
    private AccountService accountService;

    public AdminPageServlet(AccountService accountService)
    {
        this.accountService = accountService;
    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);

        HashMap<String, Object> pageVariables = getAdminInfo();
        tryShutdown(request.getParameter("shutdown"));

        response.getWriter().println(PageGenerator.getPage("admin.tml", pageVariables));
    }

    protected HashMap<String, Object> getAdminInfo() {
        Integer users = accountService.getCountUsers();
        Integer logusers = accountService.getCountLoggedInUsers();
        HashMap<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("users", users == null ? 0 :users );
        pageVariables.put("logusers", logusers == null ? 0 :logusers );
        pageVariables.put("status", "run");
        return pageVariables;
    }

    protected void tryShutdown(String time) {
        if (time != null) {
            int timeMS = Integer.valueOf(time);
            System.out.print("Server will be down after: "+ timeMS + " ms");
            TimeHelper.sleep(timeMS);
            System.out.print("\nShutdown");
            System.exit(0);
        }
    }
}
