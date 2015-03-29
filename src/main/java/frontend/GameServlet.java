package frontend;

import AccountService.AccountService;
import game.GameMechanics;
import templater.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Artem on 3/29/2015.
 */
public class GameServlet extends HttpServlet {

    private GameMechanics gameMechanics;
    private AccountService accountService;

    public GameServlet(GameMechanics gameMechanics, AccountService accountService) {
        this.accountService = accountService;
        this.gameMechanics = gameMechanics;
    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    public void doPost(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("name", accountService.getUsernameBySession(request.getSession().getId()));

        response.getWriter().println(PageGenerator.getPage("game.tml", pageVariables));
        /*response.setContentType("text/html:charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);*/
    }
}
