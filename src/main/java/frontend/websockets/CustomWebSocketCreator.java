package frontend.websockets;

import frontend.AccountService.AccountServiceImpl;
import mechanics.GameMechanics;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;

import java.sql.SQLException;

public class CustomWebSocketCreator implements WebSocketCreator {
    private AccountServiceImpl authService;
    private GameMechanics gameMechanics;
    private WebSocketService webSocketService;

    public CustomWebSocketCreator(AccountServiceImpl authService,
                                  GameMechanics gameMechanics,
                                  WebSocketService webSocketService) {
        this.authService = authService;
        this.gameMechanics = gameMechanics;
        this.webSocketService = webSocketService;
    }

    @Override
    public Object createWebSocket(ServletUpgradeRequest req, ServletUpgradeResponse resp) {
        String sessionId = req.getHttpServletRequest().getSession().getId();
        String name;
        try {
            name = authService.getUsernameBySession(sessionId);
        } catch (SQLException e) {
            throw new RuntimeException();
        }
        return new GameWebSocket(name, gameMechanics, webSocketService);
    }
}
