package main;

import ResourceLoader.GSResources;
import ResourceLoader.ResourceStatus;
import ResourceLoader.ResourcesService;
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

import java.util.ArrayList;


public class Main {
    public static void main(String[] args) throws Exception {
        ArrayList<GSResources> serverSettings = ResourcesService.getInstance().getResources("settings.xml").getContentByName("server");
        String portString = "8080";
        if (serverSettings.size() < 1)
        {
            System.out.println("Server Settings not found");
            System.exit(1);
        }
        GSResources serverSetting = serverSettings.get(0);
        if (serverSetting.getStatus() == ResourceStatus.OK)
        {
            portString = serverSetting.getSetting("port");
        }
        else
        {
            System.out.println(serverSetting.getStatusText());
        }

        int port = Integer.valueOf(portString);
        System.out.append("Starting at port: ").append(String.valueOf(port)).append('\n');
        Server server = new Server(port);

        AccountServiceImpl accountServiceImpl = new AccountServiceImpl();

        WebSocketService webSocketService = new WebSocketService();
        GameMechanics gameMechanics = new GameMechanics(webSocketService);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);

        context.addServlet(new ServletHolder(new WebSocketGameServlet(accountServiceImpl, gameMechanics, webSocketService)), "/gameplay");
        context.addServlet(new ServletHolder(new FrontendServlet(gameMechanics, accountServiceImpl, portString)), "/game");
        context.addServlet(new ServletHolder(new SignInServlet(accountServiceImpl)), "/signin");
        context.addServlet(new ServletHolder(new SignUpServlet(accountServiceImpl)), "/signup");
        context.addServlet(new ServletHolder(new SignOutServlet(accountServiceImpl)), "/signout");
        context.addServlet(new ServletHolder(new ScoreboardServlet(accountServiceImpl)), "/scoreboard");
        context.addServlet(new ServletHolder(new AdminPageServlet(accountServiceImpl)), AdminPageServlet.adminPageURL);

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
