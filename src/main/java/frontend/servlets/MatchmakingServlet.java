package frontend.servlets;

import base.AccountService.AccountService;
import com.sun.org.apache.xpath.internal.operations.Bool;
import frontend.GameSessionManager;
import org.json.simple.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;


public class MatchmakingServlet extends HttpServlet {
    private AccountService accountServiceImpl;

    public MatchmakingServlet(AccountService accountServiceImpl) {
        this.accountServiceImpl = accountServiceImpl;
    }

    public void doGet(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=utf-8");

        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("code", 0);
        ArrayList<String> listWaiting =  GameSessionManager.getInstance().getUnmatchedPlayerNames();
        try {
            listWaiting.remove(accountServiceImpl.getUsernameBySession(request.getSession().getId()));
        } catch (SQLException e) {}

        jsonResponse.put("players", listWaiting);
        response.getWriter().println(jsonResponse);
    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=utf-8");

        String opponent = request.getParameter("opponent");
        String player;
        Boolean gameStarted = false;
        try
        {
            player = accountServiceImpl.getUsernameBySession(request.getSession().getId());
            gameStarted = GameSessionManager.getInstance().startGameForPlayers(player, opponent);
        } catch (SQLException e)
        {
            System.out.println(e.getStackTrace());
        }
        finally
        {
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("code", 1);
            jsonResponse.put("gameFound", gameStarted);
            response.getWriter().println(jsonResponse.toJSONString());
        }


    }
}