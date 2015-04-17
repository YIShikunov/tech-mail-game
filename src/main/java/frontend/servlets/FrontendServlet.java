package frontend.servlets;

import mechanics.GameMechanics;
import frontend.AccountService.AccountServiceImpl;
import utils.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author v.chibrikov
 */
public class FrontendServlet extends HttpServlet {

    private GameMechanics gameMechanics;
    private AccountServiceImpl authService;

    public FrontendServlet(GameMechanics gameMechanics, AccountServiceImpl authService) {
        this.gameMechanics = gameMechanics;
        this.authService = authService;
    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {

        Map<String, Object> pageVariables = new HashMap<>();

        pageVariables.put("name", authService.getUsernameBySession(request.getSession().getId()));

        response.getWriter().println(PageGenerator.getPage("game.tml", pageVariables));

        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
