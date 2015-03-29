package frontend.websockets;

import frontend.AccountService.AccountService;
import mechanics.GameMechanics;
import frontend.websockets.WebSocketService;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;

public class CustomWebSocketCreator implements WebSocketCreator {
    private AccountService authService;
    private GameMechanics gameMechanics;
    private WebSocketService webSocketService;

    public CustomWebSocketCreator(AccountService authService,
                                  GameMechanics gameMechanics,
                                  WebSocketService webSocketService) {
        this.authService = authService;
        this.gameMechanics = gameMechanics;
        this.webSocketService = webSocketService;
    }

    @Override
    public Object createWebSocket(ServletUpgradeRequest req, ServletUpgradeResponse resp) {
        String sessionId = req.getHttpServletRequest().getSession().getId();
        String name = authService.getUsernameBySession(sessionId);
        return new GameWebSocket(name, gameMechanics, webSocketService);
    }
}
