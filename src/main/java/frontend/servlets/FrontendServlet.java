package frontend.servlets;

import base.AccountService;
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

public class FrontendServlet extends HttpServlet {

    private GameMechanics gameMechanics;
    private AccountService authService;
    private String port;

    public FrontendServlet(GameMechanics gameMechanics, AccountService authService, String port) {
        this.gameMechanics = gameMechanics;
        this.authService = authService;
        this.port = port;
    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
        Map<String, Object> pageVariables = new HashMap<>();

        pageVariables.put("name", authService.getUsernameBySession(request.getSession().getId()));
        pageVariables.put("port", this.port);

        response.getWriter().println(PageGenerator.getPage("game.tml", pageVariables));

        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
