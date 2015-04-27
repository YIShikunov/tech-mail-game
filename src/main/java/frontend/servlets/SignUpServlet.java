package frontend.servlets;

import base.AccountService;
import frontend.AccountService.AccountServiceImpl;
import frontend.AccountService.UserProfile;
import utils.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

public class SignUpServlet extends HttpServlet {
    private AccountService accountService;

    public SignUpServlet(AccountService accountService) {
        this.accountService = accountService;
    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
        String login = request.getParameter("login");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        response.setStatus(HttpServletResponse.SC_OK);

        HashMap<String, Object> pageVariables = addUser(email, password, login);

        response.getWriter().println(PageGenerator.getPage("authresponse.txt", pageVariables));
    }

    protected HashMap<String, Object> addUser(String email, String password, String login)
    {
        HashMap<String, Object> pageVariables = new HashMap<>();
        String status;

        pageVariables.put("email", email == null ? "" : email);
        pageVariables.put("password", password == null ? "" : password);
        pageVariables.put("login", password == null ? "" : login);


        if (accountService.addUser(login, new UserProfile(login, password, email))) {
            pageVariables.put("signUpStatus", "New user created");
            status = "Congratulations!!!";
        } else {
            pageVariables.put("signUpStatus", "User with name: " + login + " already exists");
            status = "This login is buzy";
        }
        pageVariables.put("status", password == null ? "" : status);
        return pageVariables;
    }

}
