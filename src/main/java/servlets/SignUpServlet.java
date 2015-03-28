package servlets;

import AccountService.AccountService;
import AccountService.UserProfile;
import templater.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SignUpServlet extends HttpServlet {
    private AccountService accountService;

    public SignUpServlet(AccountService accountService) {
        this.accountService = accountService;
    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        String password = request.getParameter("password");

        Map<String, Object> pageVariables = new HashMap<>();
        if (accountService.addUser(name, new UserProfile(name, password, ""))) {
            pageVariables.put("signUpStatus", "New user created");
        } else {
            pageVariables.put("signUpStatus", "User with name: " + name + " already exists");
        }

        response.getWriter().println(PageGenerator.getPage("signupstatus.html", pageVariables));
        response.setStatus(HttpServletResponse.SC_OK);
    }


    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
        String login = request.getParameter("login");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String status;


        response.setStatus(HttpServletResponse.SC_OK);

        Map<String, Object> pageVariables = new HashMap<>();
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
        pageVariables.put("status", password == null ? "" : status); //------

        response.getWriter().println(PageGenerator.getPage("authresponse.txt", pageVariables));
    }

}
