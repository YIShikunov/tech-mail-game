package frontend.servlets;

import frontend.AccountService.AccountServiceImpl;
import frontend.AccountService.UserProfile;
import org.json.simple.JSONObject;
import utils.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ScoreboardServlet extends HttpServlet {
    private AccountServiceImpl accountServiceImpl;

    public ScoreboardServlet(AccountServiceImpl accountServiceImpl) {
        this.accountServiceImpl = accountServiceImpl;
    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
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