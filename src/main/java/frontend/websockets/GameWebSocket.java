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

import java.lang.reflect.Array;
import java.util.ArrayList;

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

    public void showOrien(String land) {
        try {
            JSONObject jsonStart = new JSONObject();
            if (land.equals("true")) {
                jsonStart.put("land", "yes");
            } else {
                jsonStart.put("land", "no");
            }
            session.getRemote().sendString(jsonStart.toJSONString());
        } catch (Exception e) {
            System.out.print(e.toString());
        }
    }

    public void showEnemyTurn( String move1, String move2, String play) {
        try {
            JSONObject jsonStart = new JSONObject();
            int x = Integer.valueOf(move1);
            int y = Integer.valueOf(move2);
            ArrayList<Integer> list = new ArrayList<>();
            list.add(x); list.add(y);
            jsonStart.put("move", list);
            if (play.equals("true")) {
                jsonStart.put("play", true);
            } else {
                jsonStart.put("play", false);
            }
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
        if (packet.get("type").toString().equals("2")) {
            System.out.println("Color " + packet.get("color"));
            gameMechanics.makeColor(packet.get("obj").toString(),packet.get("color").toString());
        }
        if (packet.get("type").toString().equals("3")) {
            System.out.println("posX:" + packet.get("posX")+";posY:"+packet.get("posY")+"; PLAY:"+packet.get("play"));
            gameMechanics.makeMove(packet.get("obj").toString(),packet.get("posX").toString(),
                    packet.get("posY").toString(), packet.get("play").toString());
        }
        if (packet.get("type").toString().equals("4")) {
            System.out.println("Landscape:" + packet.get("land"));
            gameMechanics.makeOrien(packet.get("obj").toString(), packet.get("land").toString());
        }
    }

    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {

    }


}