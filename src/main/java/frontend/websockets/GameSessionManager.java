package frontend.websockets;

import mechanics.GameProtocol;

/**
 * Created by Artem on 5/19/2015.
 */
public class GameSessionManager {

    //// SINGLETON
    private static GameSessionManager instance;
    public static GameSessionManager getInstance() {
        if (instance == null) {
            instance = new GameSessionManager();
        }
        return instance;
    }
    //// SINGLETON

    ArrayList<GameWebSocket> unmatchedSockets;

    ArrayList<GameProtocol> runningSessions;


    public void addSocket(GameWebSocket socket) {
        unmatchedSockets.add(socket   )
    }



}
