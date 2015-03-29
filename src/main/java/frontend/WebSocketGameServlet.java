package frontend;

import AccountService.AccountService;
import game.GameMechanics;
import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;
import utils.CustomWebSocketCreator;
import utils.WebSocketService;

import javax.servlet.annotation.WebServlet;

/**
 * Created by Artem on 3/28/2015.
 */

@WebServlet(name="WebSocketGameServlet", urlPatterns = {"/gameplay"})
public class WebSocketGameServlet extends WebSocketServlet {
    private final static int IDLE_TIME = 60 * 1000;
    private AccountService accountService;
    private GameMechanics gameMechanics;
    private WebSocketService webSocketService;

    public WebSocketGameServlet(AccountService accountService,
                                GameMechanics gameMechanics,
                                WebSocketService webSocketService) {
        this.accountService = accountService;
        this.gameMechanics = gameMechanics;
        this.webSocketService = webSocketService;
    }

    @Override
    public void configure(WebSocketServletFactory webSocketServerFactory) {
        webSocketServerFactory.getPolicy().setIdleTimeout(IDLE_TIME);
        webSocketServerFactory.setCreator(new CustomWebSocketCreator(accountService, gameMechanics, webSocketService));
    }
}