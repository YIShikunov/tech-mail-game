package frontend.websockets;

import base.AccountService;
import mechanics.GameMechanics;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;

import java.sql.SQLException;

public class CustomWebSocketCreator implements WebSocketCreator {
    private AccountService accountService;
    private WebSocketService webSocketService;

    public CustomWebSocketCreator(AccountService authService,
                                  WebSocketService webSocketService) {
        this.accountService = authService;
        this.webSocketService = webSocketService;
    }

    @Override
    public Object createWebSocket(ServletUpgradeRequest req, ServletUpgradeResponse resp) {
        String sessionId = req.getHttpServletRequest().getSession().getId();
        String name;
        try {
            name = accountService.getUsernameBySession(sessionId);
        } catch (SQLException e) {
            throw new RuntimeException();
        }
        return new GameWebSocket(name);
    }
}
