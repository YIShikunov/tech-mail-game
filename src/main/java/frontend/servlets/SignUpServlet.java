package frontend.servlets;

import frontend.AccountService.AccountServiceImpl;
import utils.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

public class SignUpServlet extends HttpServlet {
    private AccountServiceImpl accountServiceImpl;

    public SignUpServlet(AccountServiceImpl accountServiceImpl) {
        this.accountServiceImpl = accountServiceImpl;
    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("login");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        response.setStatus(HttpServletResponse.SC_OK);

        HashMap<String, Object> pageVariables = addUser(username, email, password);

        response.getWriter().println(PageGenerator.getPage("authresponse.txt", pageVariables));
    }

    protected HashMap<String, Object> addUser(String username, String email, String password)
    {
        HashMap<String, Object> pageVariables = new HashMap<>();
        String status;

        pageVariables.put("email", email == null ? "" : email);
        pageVariables.put("password", password == null ? "" : password);
        pageVariables.put("login", password == null ? "" : username);


        if (accountServiceImpl.addUser(username, email, password)) {
            pageVariables.put("signUpStatus", "New user created");
            status = "Congratulations!!!";
        } else {
            pageVariables.put("signUpStatus", "User with name: " + username + " already exists");
            status = "This login is busy";
        }
        pageVariables.put("status", password == null ? "" : status);
        return pageVariables;
    }

}
