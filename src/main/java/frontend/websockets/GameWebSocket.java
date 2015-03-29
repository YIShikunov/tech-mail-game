package frontend.websockets;

import mechanics.GameMechanics;
import mechanics.GameProfile;
import frontend.websockets.WebSocketService;
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
    private String name;
    private Session session;
    private GameMechanics gameMechanics;
    private WebSocketService webSocketService;

    public GameWebSocket(String name, GameMechanics gameMechanics, WebSocketService webSocketService) {
        this.name = name;
        this.gameMechanics = gameMechanics;
        this.webSocketService = webSocketService;
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

    public void startGame(GameProfile user, Boolean amIFirst) {
        try {
            JSONObject jsonStart = new JSONObject();
            jsonStart.put("status", "start");
            jsonStart.put("enemy", user.getEnemy());
            jsonStart.put("amIFirst", amIFirst);
            session.getRemote().sendString(jsonStart.toJSONString());
        } catch (Exception e) {
            System.out.print(e.toString());
        }
    }

    public void showErrorMessage(String message) {
        try {
            JSONObject jsonStart = new JSONObject();
            jsonStart.put("status", "rejected");
            jsonStart.put("message", message);
            session.getRemote().sendString(jsonStart.toJSONString());
        } catch (Exception e) {
            System.out.print(e.toString());
        }
    }

    public void showMyTurn(String newFieldState) {
        try {
            JSONObject jsonStart = new JSONObject();
            jsonStart.put("status", "turn");
            jsonStart.put("state", newFieldState);
            session.getRemote().sendString(jsonStart.toJSONString());
        } catch (Exception e) {
            System.out.print(e.toString());
        }
    }

    public void showEnemyTurn(String newFieldState) {
        try {
            JSONObject jsonStart = new JSONObject();
            jsonStart.put("status", "turn");
            jsonStart.put("state", newFieldState);
            session.getRemote().sendString(jsonStart.toJSONString());
        } catch (Exception e) {
            System.out.print(e.toString());
        }
    }

    public void notifyWin() {
        try {
            JSONObject jsonStart = new JSONObject();
            jsonStart.put("status", "finish");
            jsonStart.put("win", true);
            session.getRemote().sendString(jsonStart.toJSONString());
        } catch (Exception e) {
            System.out.print(e.toString());
        }
    }

    public void notifyLoss() {
        try {
            JSONObject jsonStart = new JSONObject();
            jsonStart.put("status", "finish");
            jsonStart.put("win", false);
            session.getRemote().sendString(jsonStart.toJSONString());
        } catch (Exception e) {
            System.out.print(e.toString());
        }
    }

    @OnWebSocketConnect
    public void onOpen(Session session) {
        setSession(session);
        webSocketService.addUser(this);
        gameMechanics.addUser(name);
    }

    @OnWebSocketMessage
    public void onMessage(String data) {
        gameMechanics.makeTurn(name, data);
    }

    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {

    }


}
