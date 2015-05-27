package frontend.servlets;

import frontend.AccountService.AccountService;
import mechanics.GameMechanics;
import frontend.websockets.WebSocketService;
import frontend.websockets.CustomWebSocketCreator;
import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

import javax.servlet.annotation.WebServlet;

/**
 * This class represents a servlet starting a webSocket application
 */
@WebServlet(name = "WebSocketGameServlet")
public class WebSocketGameServlet extends WebSocketServlet {
    private final static int IDLE_TIME = 60 * 1000;
    private GameMechanics gameMechanics;
    private WebSocketService webSocketService;

    public WebSocketGameServlet(GameMechanics gameMechanics,
                                WebSocketService webSocketService) {
        this.gameMechanics = gameMechanics;
        this.webSocketService = webSocketService;
    }

    @Override
    public void configure(WebSocketServletFactory factory) {
        factory.getPolicy().setIdleTimeout(IDLE_TIME);
        factory.setCreator(new CustomWebSocketCreator(gameMechanics, webSocketService));
    }
}
