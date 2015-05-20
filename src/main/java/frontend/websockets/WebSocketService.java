package frontend.websockets;

import mechanics.GameProfile;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Artem on 3/28/2015.
 */
@Deprecated
public class WebSocketService {
    private Map<String, GameWebSocket> userSockets = new HashMap<>();

    public void addUser(GameWebSocket user) {
        userSockets.put(user.getName(), user);
    }

    public void showErrorMessage(GameProfile user, String message) {
        //userSockets.get(user.getName()).showErrorMessage(message);
    }

    public void showMyTurn(GameProfile user, String newFieldState) {
        //userSockets.get(user.getName()).showMyTurn(newFieldState);
    }

    public void showEnemyTurn(GameProfile user, String newFieldState) {
        //userSockets.get(user.getName()).showEnemyTurn(newFieldState);
    }

    public void notifyStartGame(GameProfile user, Boolean amIFirst) {
        //GameWebSocket gameWebSocket = userSockets.get(user.getName());
        //gameWebSocket.startGame(user, amIFirst);
    }

    public void notifyWin(GameProfile user) {
        //userSockets.get(user.getName()).notifyWin();
    }

    public void notifyLoss(GameProfile user) {
        //userSockets.get(user.getName()).notifyLoss();
    }
}