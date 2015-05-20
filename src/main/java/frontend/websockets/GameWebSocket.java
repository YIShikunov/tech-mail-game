package frontend.websockets;

import mechanics.GameMechanics;
import mechanics.GameProfile;
import frontend.websockets.WebSocketService;
import mechanics.GameProtocol;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.json.simple.JSONObject;

/**
 * Created by Artem on 3/29/2015.
 */

@WebSocket
public class GameWebSocket {
    final private String name;
    final private boolean isFirstPlayer;
    final private Session session;
    final private GameProtocol protocol;

    public GameWebSocket(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public Session getSession() {
        return session;
    }

    public void setProtocol(GameProtocol protocol) {
        this.protocol = protocol;
        protocol.setWebSocket(isFirstPlayer, this);
    }

    public void setFirstPlayer(boolean isFirstPlayer) {
        this.isFirstPlayer = isFirstPlayer;
    }

    @OnWebSocketConnect
    public void onOpen(Session session) {
        setSession(session);
        GameSessionManager.getInstance().addSocket(this);
    }

    @OnWebSocketMessage
    public void onMessage(String data) {
        System.out.print(data); // debug purposes
        protocol.process(isFirstPlayer, data);
    }

    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {

    }


}