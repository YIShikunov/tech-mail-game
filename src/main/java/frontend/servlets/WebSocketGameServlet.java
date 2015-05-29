package frontend.servlets;

import base.AccountService.AccountService;
import frontend.websockets.CustomWebSocketCreator;
import messageSystem.MessageSystem;
import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

import javax.servlet.annotation.WebServlet;

/**
 * This class represents a servlet starting a webSocket application
 */
@WebServlet(name = "WebSocketGameServlet", urlPatterns = {"/game"})
public class  WebSocketGameServlet extends WebSocketServlet {
    private final static int IDLE_TIME = 600 * 1000;
    private AccountService accountService;
    private MessageSystem messageSystem;

    public WebSocketGameServlet(AccountService accountService, MessageSystem messageSystem) {
        this.accountService = accountService;
        this.messageSystem = messageSystem;

    }

    @Override
    public void configure(WebSocketServletFactory factory) {
        factory.getPolicy().setIdleTimeout(IDLE_TIME);
        factory.setCreator(new CustomWebSocketCreator(accountService, messageSystem));
    }
}
