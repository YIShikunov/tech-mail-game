package frontend.servlets;

import base.AccountService.AccountService;
import org.json.simple.JSONObject;
import frontend.AccountService.UserDataSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;

public class SignInServlet extends HttpServlet {
    private AccountService accountService;

    public SignInServlet(AccountService accountService) {
        this.accountService = accountService;
    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("login");
        String password = request.getParameter("password");
        HttpSession session = request.getSession();
        String sessionID = session.getId();

        HashMap<String, Object> pageVariables = signInUser(username, password, sessionID);

        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("code", 0);
        jsonResponse.put("response", pageVariables.get("loginStatus"));

        response.getWriter().println(jsonResponse);
    }

    protected HashMap<String, Object> signInUser(String username, String password, String sessionID)
    {
        UserDataSet profile;
        try {
            profile = accountService.getUserByName(username);
        } catch (SQLException e) {
            profile = null; // Return an server error page?
        }

        HashMap<String, Object> pageVariables = new HashMap<>();

        if (profile != null && profile.getPassword().equals(password)) {
            pageVariables.put("loginStatus", "Hello, "+profile.getUsername()+", email: "+profile.getEmail());
            pageVariables.put("online", 1);
            accountService.addSession(sessionID, profile.getId());
        } else {
            pageVariables.put("loginStatus", "Wrong login/password");
            pageVariables.put("online", 0);
        }
        pageVariables.put("email", profile == null ? "none" : profile.getEmail());
        pageVariables.put("login", profile == null ? "none" : profile.getUsername());

        return pageVariables;
    }
}
