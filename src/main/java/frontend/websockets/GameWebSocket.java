package frontend.websockets;

import mechanics.GameProtocol;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.json.simple.JSONObject;


@WebSocket
public class GameWebSocket {
    final private String name;
    private boolean isFirstPlayer;
    private Session session;
    private GameProtocol protocol;

    private boolean gameStarted = false;

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
    }

    public void setFirstPlayer(boolean isFirstPlayer) {
        this.isFirstPlayer = isFirstPlayer;
    }

    public void startGame() {
        gameStarted = true;
    }

    public void send(JSONObject packet) {
        try {
            System.out.print(packet.toJSONString());
            session.getRemote().sendString(packet.toJSONString());
        } catch (Exception e) {
            System.out.print(e.toString());
        }
    }


    @OnWebSocketConnect
    public void onOpen(Session session) {
        setSession(session);
        GameSessionManager.getInstance().addSocket(this);
    }

    @OnWebSocketMessage
    public void onMessage(String data) {
        System.out.print("onMessage");
        if (gameStarted) {
            System.out.print(data); // debug purposes
            protocol.process(isFirstPlayer, data);
        }
    }

    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {

    }


}