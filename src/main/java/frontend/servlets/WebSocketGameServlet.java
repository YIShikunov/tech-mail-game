package frontend.servlets;

import frontend.AccountService.AccountService;
import mechanics.GameMechanics;
import frontend.websockets.WebSocketService;
import frontend.websockets.CustomWebSocketCreator;
import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

import javax.servlet.annotation.WebServlet;


@WebServlet(name = "WebSocketGameServlet", urlPatterns = {"/game"})
public class WebSocketGameServlet extends WebSocketServlet {
    private final static int IDLE_TIME = 60 * 1000;
    private AccountService authService;
    private GameMechanics gameMechanics;
    private WebSocketService webSocketService;

    public WebSocketGameServlet(AccountService authService,
                                GameMechanics gameMechanics,
                                WebSocketService webSocketService) {
        this.authService = authService;
        this.gameMechanics = gameMechanics;
        this.webSocketService = webSocketService;
    }

    @Override
    public void configure(WebSocketServletFactory factory) {
        factory.getPolicy().setIdleTimeout(IDLE_TIME);
        factory.setCreator(new CustomWebSocketCreator(authService, gameMechanics, webSocketService));
    }
}
