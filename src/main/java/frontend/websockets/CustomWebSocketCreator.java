package frontend.websockets;

import base.AccountService.AccountService;
import messageSystem.MessageSystem;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;

import java.sql.SQLException;

public class CustomWebSocketCreator implements WebSocketCreator {
    private AccountService accountService;
    private MessageSystem messageSystem;

    public CustomWebSocketCreator(AccountService authService, MessageSystem messageSystem) {
        this.accountService = authService;
        this.messageSystem = messageSystem;
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
        return new GameWebSocket(name, messageSystem, accountService);
    }
}
