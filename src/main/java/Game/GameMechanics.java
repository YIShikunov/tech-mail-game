package game;

import utils.WebSocketService;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GameMechanics {
    private WebSocketService webSocketService;

    private Map<String, GameSession> nameToGame = new HashMap<>();

    private Set<GameSession> allSessions = new HashSet<>();

    private String waiting;

    public GameMechanics(WebSocketService webSocketService) {
        this.webSocketService = webSocketService;
    }

    public void addUser(String user) {
        if (waiting != null) {
            startGame(user);
            waiting = user;
        } else {
            waiting = user;
        }
    }

    public void makeTurn(String userName, String action) {
        GameSession thisSession = nameToGame.get(userName);
        GameProfile thisUser = thisSession.getSelf(userName);
        GameProfile hisEnemy = thisSession.getEnemy(userName);

        if (action.equals("legal")) {
            String newFieldState;

            if (!thisSession.isHisTurn(thisUser)){
                webSocketService.showErrorMessage(thisUser, "It is not your turn!");
            } else {
                double resultOfComplicatedGameMechanics = Math.random();
                if (resultOfComplicatedGameMechanics < 0.1) {
                    newFieldState = "A piece was moved!";
                } else if (resultOfComplicatedGameMechanics < 0.5) {
                    newFieldState = "A piece was captured!";
                } else if (resultOfComplicatedGameMechanics < 0.8) {
                    newFieldState = "That was a Kasparov-level turn!";
                } else if (resultOfComplicatedGameMechanics < 0.9) {
                    newFieldState = "Ouch, a lethal was missed...";
                } else {
                    newFieldState = "That was a winning move! Hooray!";
                    finishGame(thisUser.getName());
                    return;
                }
                webSocketService.showMyTurn(thisUser, newFieldState);
                webSocketService.showEnemyTurn(hisEnemy, newFieldState);
                thisSession.flipTurn();
            }
        }
        if (action.equals("illegal")) {
            if (!thisSession.isHisTurn(thisUser))
                webSocketService.showErrorMessage(thisUser, "It is not your turn!");
            else {
                webSocketService.showErrorMessage(thisUser, "This is not a legal turn!");
            }
        }
    }

    public void startGame(String first) {
        String second = waiting;
        GameSession gameSession = new GameSession(first, second);
        allSessions.add(gameSession);
        nameToGame.put(first, gameSession);
        nameToGame.put(second, gameSession);

        webSocketService.notifyStartGame(gameSession.getSelf(first), true);
        webSocketService.notifyStartGame(gameSession.getSelf(second), false);
    }

    public void finishGame(String winner) {
        webSocketService.notifyWin(nameToGame.get(winner).getSelf(winner));
        webSocketService.notifyLoss(nameToGame.get(winner).getEnemy(winner));
        // TODO: remove session
    }
}
