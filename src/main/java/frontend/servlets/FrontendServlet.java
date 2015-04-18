package frontend.servlets;

import ResourceLoader.GSResources;
import ResourceLoader.resourcesService;
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

        GSResources serverSettings = resourcesService.getInstance().getResources("settings");
        String portString = "8080";
        if (serverSettings.getSetting("__status__").equals("OK"))
        {
            portString = serverSettings.getSetting("port");
        }
        else
        {
            System.out.println(serverSettings.getSetting("__status__"));
        }

        Map<String, Object> pageVariables = new HashMap<>();

        pageVariables.put("name", authService.getUsernameBySession(request.getSession().getId()));
        pageVariables.put("port", portString);

        response.getWriter().println(PageGenerator.getPage("game.tml", pageVariables));

        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
