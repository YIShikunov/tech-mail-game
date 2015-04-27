package frontend.servlets;

import base.AccountService;
import frontend.AccountService.AccountServiceImpl;
import frontend.AccountService.UserProfile;
import utils.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

public class SignInServlet extends HttpServlet {
    private AccountService accountService;

    public SignInServlet(AccountService accountService) {
        this.accountService = accountService;
    }

    public void doGet(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
        String login = request.getParameter("login");
        String password = request.getParameter("password");
        String email = "none";
        HttpSession session = request.getSession();
        String sessionID = session.getId();

        HashMap<String, Object> pageVariables = signInUser(login, password, email, sessionID);

        response.setStatus(HttpServletResponse.SC_OK);

        response.getWriter().println(PageGenerator.getPage("authstatus.html", pageVariables));
    }

    protected HashMap<String, Object> signInUser(String login, String password, String email, String sessionID)
    {

        UserProfile profile = accountService.getUser(login);

        HashMap<String, Object> pageVariables = new HashMap<>();

        if (profile != null && profile.getPassword().equals(password)) {
            pageVariables.put("loginStatus", "Hello, "+profile.getLogin()+"<br>"+profile.getEmail());
            pageVariables.put("online", 1);
            accountService.addSessions(sessionID, profile);
        } else {
            pageVariables.put("loginStatus", "Wrong login/password");
            pageVariables.put("online", 0);
        }
        pageVariables.put("email", email);
        pageVariables.put("login", login == null ? "none" : login);

        return pageVariables;
    }
}
