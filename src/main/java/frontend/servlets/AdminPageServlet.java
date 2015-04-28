package frontend.servlets;
import utils.TimeHelper;
import utils.PageGenerator;

import frontend.AccountService.AccountServiceImpl;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

public class AdminPageServlet extends HttpServlet {
    public static final String adminPageURL = "/admin";
    private AccountServiceImpl accountServiceImpl;

    public AdminPageServlet(AccountServiceImpl accountService)
    {
        accountServiceImpl = accountService;
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
        Integer users = accountServiceImpl.getCountUsers();
        Integer logusers = accountServiceImpl.getCountLogUsers();
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
