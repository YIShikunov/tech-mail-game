package frontend.servlets;

import frontend.AccountService.AccountServiceImpl;
import frontend.AccountService.UserDataSet;
import utils.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

public class SignInServlet extends HttpServlet {
    private AccountServiceImpl accountServiceImpl;

    public SignInServlet(AccountServiceImpl accountServiceImpl) {
        this.accountServiceImpl = accountServiceImpl;
    }

    public void doGet(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("login");
        String password = request.getParameter("password");
        String email = "none";
        HttpSession session = request.getSession();
        String sessionID = session.getId();

        HashMap<String, Object> pageVariables = signInUser(username, email, password, sessionID);

        response.setStatus(HttpServletResponse.SC_OK);

        response.getWriter().println(PageGenerator.getPage("authstatus.html", pageVariables));
    }

    protected HashMap<String, Object> signInUser(String username, String email, String password, String sessionID)
    {

        UserDataSet profile = accountServiceImpl.getUserByName(username);

        HashMap<String, Object> pageVariables = new HashMap<>();

        if (profile != null && profile.getPassword().equals(password)) {
            pageVariables.put("loginStatus", "Hello, "+profile.getUsername()+"<br>"+profile.getEmail());
            pageVariables.put("online", 1);
            accountServiceImpl.addSession(sessionID, profile.getId());
        } else {
            pageVariables.put("loginStatus", "Wrong login/password");
            pageVariables.put("online", 0);
        }
        pageVariables.put("email", email);
        pageVariables.put("login", username == null ? "none" : username);

        return pageVariables;
    }
}
