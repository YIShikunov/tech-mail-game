package utils;

import AccountService.AccountService;
import game.GameMechanics;
import game.GameWebSocket;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;

/**
 * Created by Artem on 3/29/2015.
 */
public class CustomWebSocketCreator implements WebSocketCreator {
    private AccountService accountService;
    private GameMechanics gameMechanics;
    private WebSocketService webSocketService;

    public CustomWebSocketCreator(AccountService accountService,
                                  GameMechanics gameMechanics,
                                  WebSocketService webSocketService) {
        this.accountService = accountService;
        this.gameMechanics = gameMechanics;
        this.webSocketService = webSocketService;
    }

    @Override
    public Object createWebSocket(ServletUpgradeRequest req, ServletUpgradeResponse resp) {
        String sessionID = req.getHttpServletRequest().getSession().getId();
        String name = accountService.getUsernameBySession(sessionID);
        return new GameWebSocket(name, gameMechanics, webSocketService);
    }
}
