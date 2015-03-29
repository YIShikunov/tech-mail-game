package main;

import frontend.AccountService.AccountService;
import frontend.AccountService.UserProfile;
import frontend.SignUpServlet;
import frontend.servlets.*;
import frontend.websockets.WebSocketService;
import mechanics.GameMechanics;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class Main {
    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.out.append("Use port as the first argument");
            System.exit(1);
        }

        String portString = args[0];
        int port = Integer.valueOf(portString);
        System.out.append("Starting at port: ").append(String.valueOf(port)).append('\n');
        Server server = new Server(port);

        //AuthServiceImpl authService = new AuthServiceImpl();
        AccountService accountService = new AccountService();
        WebSocketService webSocketService = new WebSocketService();
        GameMechanics gameMechanics = new GameMechanics(webSocketService);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);

        context.addServlet(new ServletHolder(new WebSocketGameServlet(accountService, gameMechanics, webSocketService)), "/gameplay");
        context.addServlet(new ServletHolder(new FrontendServlet(gameMechanics, accountService)), "/game");
        context.addServlet(new ServletHolder(new SignInServlet(accountService)), "/api/v1/auth/signin");
        context.addServlet(new ServletHolder(new SignUpServlet(accountService)), "/api/v1/auth/signup");
        context.addServlet(new ServletHolder(new SignOutServlet(accountService)), "/api/v1/auth/signout");
        context.addServlet(new ServletHolder(new AdminPageServlet()), AdminPageServlet.adminPageURL);

        /// DEBUG ////
        accountService.addUser("123", new UserProfile("123", "123", "123@123"));
        accountService.addUser("234", new UserProfile("234", "234", "123@123"));
        /// DEBUG ////

        ResourceHandler resource_handler = new ResourceHandler();
        resource_handler.setDirectoriesListed(true);
        resource_handler.setResourceBase("public_html");

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{resource_handler, context});

        server.setHandler(handlers);
        server.start();
        server.join();
    }
}
