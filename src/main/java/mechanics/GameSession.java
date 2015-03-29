package mechanics;

import java.util.HashMap;
import java.util.Map;

public class GameSession {
    private final GameProfile first;
    private final GameProfile second;

    private Boolean isFirstTurn = true;

    private Map<String, GameProfile> users = new HashMap<>();

    public GameSession(String user1, String user2) {
        GameProfile gameProfile1 = new GameProfile(user1);
        gameProfile1.setEnemy(user2);

        GameProfile gameProfile2 = new GameProfile(user2);
        gameProfile1.setEnemy(user1);

        users.put(user1, gameProfile1);
        users.put(user2, gameProfile2);

        this.first = gameProfile1;
        this.second = gameProfile2;
    }

    public GameProfile getEnemy(String user) {
        String enemyName = users.get(user).getEnemy();
        return users.get(enemyName);
    }

    public GameProfile getSelf(String user) {
        return users.get(user);
    }

    public GameProfile getFirst() {
        return first;
    }

    public GameProfile getSecond() {
        return second;
    }

    public Boolean isHisTurn(GameProfile user) {
        return (user == first && isFirstTurn) || (user == second && !isFirstTurn);
    }

    public void flipTurn() {
        this.isFirstTurn = !isFirstTurn;
    }
}
