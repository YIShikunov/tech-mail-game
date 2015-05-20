package frontend.servlets;

import base.AccountService;
import frontend.websockets.CustomWebSocketCreator;
import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

import javax.servlet.annotation.WebServlet;

/**
 * This class represents a servlet starting a webSocket application
 */
@WebServlet(name = "WebSocketGameServlet", urlPatterns = {"/game"})
public class  WebSocketGameServlet extends WebSocketServlet {
    private final static int IDLE_TIME = 60 * 1000;
    private AccountService accountService;

    public WebSocketGameServlet(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public void configure(WebSocketServletFactory factory) {
        factory.getPolicy().setIdleTimeout(IDLE_TIME);
        factory.setCreator(new CustomWebSocketCreator(accountService));
    }
}
