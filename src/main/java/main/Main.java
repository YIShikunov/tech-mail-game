package main;

import ResourceLoader.GSResources;
import ResourceLoader.ResourceStatus;
import ResourceLoader.ResourcesService;
import base.AccountService;
import frontend.AccountService.AccountServiceImpl;
import frontend.servlets.*;
import frontend.websockets.GameSessionManager;
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

        AccountService accountService = new AccountServiceImpl();

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);

        context.addServlet(new ServletHolder(new WebSocketGameServlet(accountService)), "/gameplay");
        context.addServlet(new ServletHolder(new FrontendServlet(accountService, portString)), "/game");
        context.addServlet(new ServletHolder(new SignInServlet(accountService)), "/api/v1/auth/signin");
        context.addServlet(new ServletHolder(new SignUpServlet(accountService)), "/api/v1/auth/signup");
        context.addServlet(new ServletHolder(new SignOutServlet(accountService)), "/api/v1/auth/signout");
        context.addServlet(new ServletHolder(new ScoreboardServlet(accountService)), "/scoreboard");
        context.addServlet(new ServletHolder(new AdminPageServlet(accountService)), AdminPageServlet.adminPageURL);

        ResourceHandler resource_handler = new ResourceHandler();
        resource_handler.setDirectoriesListed(true);
        resource_handler.setResourceBase("public_html");

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{resource_handler, context});

        server.setHandler(handlers);
        server.start();
        (new Thread(GameSessionManager.getInstance())).run();
        server.join();
    }
}
