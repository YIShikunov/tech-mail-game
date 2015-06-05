package frontend.servlets;

import base.AccountService.AccountService;
import org.json.simple.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;


public class LoggedInServlet extends HttpServlet {
    private AccountService accountServiceImpl;

    public LoggedInServlet(AccountService accountServiceImpl) {
        this.accountServiceImpl = accountServiceImpl;
    }

    public void doGet(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=utf-8");
        try{
            Boolean isAuth = accountServiceImpl.isAuthorised(request.getSession().toString());
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("code", 0);
            jsonResponse.put("loggedIn", isAuth);
            jsonResponse.put("yourName", accountServiceImpl.getUsernameBySession(request.getSession().toString()));
            response.getWriter().println(jsonResponse);
        } catch (SQLException e)
        {
            System.out.println(e.getStackTrace());
        }
    }
}