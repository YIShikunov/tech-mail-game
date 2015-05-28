package frontend.websockets;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import mechanics.GameMechanics;
import mechanics.GameProfile;
import frontend.websockets.WebSocketService;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

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

    public void start(GameProfile user, Boolean monitor) {
        try {
            JSONObject jsonStart = new JSONObject();
            if (monitor)
                jsonStart.put("role", "monitor");
            else
                jsonStart.put("role", "joystick");
            session.getRemote().sendString(jsonStart.toJSONString());
        } catch (Exception e) {
            System.out.print(e.toString());
        }
    }

    public void showColor(String color) {
        try {
            JSONObject jsonStart = new JSONObject();
            jsonStart.put("color", color);
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
            jsonStart.put("isMyTurn", false);
            session.getRemote().sendString(jsonStart.toJSONString());
        } catch (Exception e) {
            System.out.print(e.toString());
        }
    }

    public void showEnemyTurn(String newFieldState) {
        try {
            JSONObject jsonStart = new JSONObject();
            jsonStart.put("move", newFieldState);
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


    @OnWebSocketConnect
    public void onOpen(Session session) {
        setSession(session);
        webSocketService.addUser(this);
    }

    @OnWebSocketMessage
    public void onMessage(String data) {
        JSONParser jsonParser = new JSONParser();
        JSONObject packet = null;
        try {
            packet = new JSONObject((JSONObject)(jsonParser.parse(data)));
        } catch (ParseException e) {
            System.out.print(e.toString());
        }
        if (packet.get("type").toString().equals("0")) {
            gameMechanics.addUser(packet.get("obj").toString());
        } else
        if (packet.get("type").toString().equals("1")) {
            System.out.println("Move to " + packet.get("move"));
            gameMechanics.makeMove(packet.get("obj").toString(),packet.get("move").toString());
        }
        if (packet.get("type").toString().equals("2")) {
            System.out.println("Color " + packet.get("color"));
            gameMechanics.makeColor(packet.get("obj").toString(),packet.get("color").toString());
        }
//        System.out.println(packet.toString());
        //gameMechanics.makeTurn(name, data);
    }

    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {

    }


}