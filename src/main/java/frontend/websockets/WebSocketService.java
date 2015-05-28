package frontend.websockets;

import mechanics.GameProfile;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Artem on 3/28/2015.
 */
public class WebSocketService {
    private Map<String, GameWebSocket> userSockets = new HashMap<>();

    public void addUser(GameWebSocket user) {
        userSockets.put(user.getName(), user);
    }

    public void showColor(GameProfile user, String message) {
        userSockets.get(user.getName()).showColor(message);
    }

    public void showOrien(GameProfile user, String newFieldState) {
        userSockets.get(user.getName()).showOrien(newFieldState);
    }

    public void showMove(GameProfile user,  String move1, String move2, String play) {
        userSockets.get(user.getName()).showEnemyTurn(move1, move2, play);
    }

    public void notifyStart(GameProfile user, Boolean amIFirst) {
        GameWebSocket gameWebSocket = userSockets.get(user.getName());
        gameWebSocket.start(user, amIFirst);
    }

    public void notifyWin(GameProfile user) {
        userSockets.get(user.getName()).notifyWin();
    }

}