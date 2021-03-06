package frontend.servlets;

import base.AccountService.AccountService;
import org.json.simple.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class ScoreboardServlet extends HttpServlet {
    private AccountService accountServiceImpl;

    public ScoreboardServlet(AccountService accountServiceImpl) {
        this.accountServiceImpl = accountServiceImpl;
    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=utf-8");

        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("code", 0);
        jsonResponse.put("scores", accountServiceImpl.getScoreBoard());
        response.getWriter().println(jsonResponse);
    }
}