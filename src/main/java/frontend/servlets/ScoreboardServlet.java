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
        System.out.println("AJAX 'GET' from /scoreboard ");
        doPost(request, response);
    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=utf-8");
        JSONObject jsonResponse = new JSONObject();

        jsonResponse.put("code", 0);
        List<JSONObject> scores = new ArrayList<>();

        JSONObject score1 = new JSONObject();
        score1.put("name", "Alex");
        score1.put("score", 120);
        scores.add(score1);

        JSONObject score2 = new JSONObject();
        score2.put("name", "Mikhail");
        score2.put("score", 180);
        scores.add(score2);

        JSONObject score3 = new JSONObject();
        score3.put("name", "Daniel");
        score3.put("score", 150);
        scores.add(score3);

        JSONObject score4 = new JSONObject();
        score4.put("name", "Maxim");
        score4.put("score", 90);
        scores.add(score4);

        jsonResponse.put("scores", scores);

        System.out.println(scores);

        response.getWriter().println(jsonResponse);
    }

}