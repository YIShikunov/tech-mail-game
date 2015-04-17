package main;

import base.GSResources;
import base.resourcesService;
import frontend.AccountService.AccountServiceImpl;
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
        /*if (args.length != 1) {
            System.out.append("Use port as the first argument");
            System.exit(1);
        }
        String portString = args[0];*/
        int port = Integer.valueOf(portString);
        System.out.append("Starting at port: ").append(String.valueOf(port)).append('\n');
        Server server = new Server(port);

        //AuthServiceImpl authService = new AuthServiceImpl();
        AccountServiceImpl accountServiceImpl = new AccountServiceImpl();

        WebSocketService webSocketService = new WebSocketService();
        GameMechanics gameMechanics = new GameMechanics(webSocketService);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);

        context.addServlet(new ServletHolder(new WebSocketGameServlet(accountServiceImpl, gameMechanics, webSocketService)), "/gameplay");
        context.addServlet(new ServletHolder(new FrontendServlet(gameMechanics, accountServiceImpl)), "/game");
        context.addServlet(new ServletHolder(new SignInServlet(accountServiceImpl)), "/api/v1/auth/signin");
        context.addServlet(new ServletHolder(new SignUpServlet(accountServiceImpl)), "/api/v1/auth/signup");
        context.addServlet(new ServletHolder(new SignOutServlet(accountServiceImpl)), "/api/v1/auth/signout");
        context.addServlet(new ServletHolder(new AdminPageServlet(accountServiceImpl)), AdminPageServlet.adminPageURL);

        /// DEBUG ////

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
