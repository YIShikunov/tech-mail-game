package main;

import frontend.*;
import AccountService.AccountService;
import game.GameMechanics;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import utils.WebSocketService;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.Servlet;

public class Main {
    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.out.append("Use port as the first argument");
            System.exit(1);
        }

        String portString = args[0];
        int port = Integer.valueOf(portString);
        System.out.append("Starting at port: ").append(String.valueOf(port)).append('\n');

        AccountService accountService = new AccountService();
        WebSocketService webSocketService = new WebSocketService();
        GameMechanics gameMechanics = new GameMechanics(webSocketService);

        Servlet signin = new SignInServlet(accountService);
        Servlet signUp = new SignUpServlet(accountService);
        Servlet signOut = new SignOutServlet(accountService);
        Servlet game = new GameServlet(gameMechanics, accountService);
        WebSocketGameServlet webSocketGameServlet = new WebSocketGameServlet(accountService,
                gameMechanics, webSocketService);


        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(signin), "/api/v1/auth/signin");
        context.addServlet(new ServletHolder(signUp), "/api/v1/auth/signup");
        context.addServlet(new ServletHolder(signOut), "/api/v1/auth/signout");
        context.addServlet(new ServletHolder(new AdminPageServlet()), AdminPageServlet.adminPageURL);
        context.addServlet(new ServletHolder(webSocketGameServlet), "/gameplay");
        context.addServlet(new ServletHolder(game), "/game.html");

        ResourceHandler resource_handler = new ResourceHandler();
        resource_handler.setDirectoriesListed(true);
        resource_handler.setResourceBase("public_html");

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{resource_handler, context});

        Server server = new Server(port);
        server.setHandler(handlers);

        server.start();
        server.join();
    }
}
