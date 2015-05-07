package frontend.servlets;

import base.AccountService;
import frontend.AccountService.AccountServiceImpl;
import org.json.simple.JSONObject;
import frontend.AccountService.UserDataSet;
import utils.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;

public class SignUpServlet extends HttpServlet {
    private AccountService accountService;

    public SignUpServlet(AccountService accountService) {
        this.accountService = accountService;
    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("login");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        response.setStatus(HttpServletResponse.SC_OK);

        HashMap<String, Object> pageVariables = addUser(username, email, password);

        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("code", 0);
        jsonResponse.put("response", pageVariables.get("status"));

        response.getWriter().println(jsonResponse);
    }

    protected HashMap<String, Object> addUser(String username, String email, String password)
    {
        HashMap<String, Object> pageVariables = new HashMap<>();
        String status;

        pageVariables.put("email", email == null ? "" : email);
        pageVariables.put("password", password == null ? "" : password);
        pageVariables.put("login", password == null ? "" : username);


        if (accountService.addUser(username, email, password)) {
            pageVariables.put("signUpStatus", "New user created");
            status = "Congratulations!!!";
            GiveRandomScore(username);
        } else {
            pageVariables.put("signUpStatus", "User with name: " + username + " already exists");
            status = "This login is busy";
        }
        pageVariables.put("status", password == null ? "" : status);
        return pageVariables;
    }

    protected void GiveRandomScore(String username) {
        try {
            UserDataSet user = accountService.getUserByName(username);
            user.setRandomScore();
            accountService.updateUser(user);
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }

}
