package mechanics;

import frontend.websockets.WebSocketService;
import mechanics.GameProfile;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class GameMechanics {
    private WebSocketService webSocketService;

    private Map<String, GameSession> nameToGame = new HashMap<>();

    private Set<GameSession> allSessions = new HashSet<>();

    private String waiting = null;

    public GameMechanics(WebSocketService webSocketService) {
        this.webSocketService = webSocketService;
    }

    public void addUser(String user) {
        System.out.println("add "+user);
        if (waiting != null) {
            startGame(user);
            waiting = null;
        } else {
            waiting = user;
        }
    }

    public void makeMove(String userName, String move1, String move2, String play) {
        GameSession thisSession = nameToGame.get(userName);
        GameProfile thisUser = thisSession.getSelf(userName);
        GameProfile hisEnemy = thisSession.getEnemy(userName);
        webSocketService.showMove(hisEnemy, move1, move2, play);
    }

    public void makeColor(String userName, String action) {
        GameSession thisSession = nameToGame.get(userName);
        GameProfile hisEnemy = thisSession.getEnemy(userName);
        if (hisEnemy != null)
            webSocketService.showColor(hisEnemy, action);
    }

    public void makeOrien(String userName, String action) {
        GameSession thisSession = nameToGame.get(userName);
        GameProfile hisEnemy = thisSession.getEnemy(userName);
        if (hisEnemy != null)
            webSocketService.showOrien(hisEnemy, action);
    }

    public void startGame(String first) {
        String second = waiting;
        GameSession gameSession = new GameSession(first, second);
        allSessions.add(gameSession);
        nameToGame.put(first, gameSession);
        nameToGame.put(second, gameSession);

        webSocketService.notifyStart(gameSession.getSelf(first), true);
        webSocketService.notifyStart(gameSession.getSelf(second), false);
    }

    public void finishGame(String winner) {
    }
}
