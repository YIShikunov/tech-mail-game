package frontend;

import frontend.websockets.GameWebSocket;
import mechanics.GameProtocol;
import org.eclipse.jetty.util.ConcurrentArrayQueue;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class GameSessionManager implements Runnable {

    //// SINGLETON (for a reason, shut up)
    private static GameSessionManager instance;
    public static synchronized GameSessionManager getInstance() {
        if (instance == null) {
            instance = new GameSessionManager();
        }
        return instance;
    }
    //// SINGLETON

    private volatile boolean stopped = false;

    ConcurrentLinkedQueue<GameWebSocket> unmatchedSockets = new ConcurrentLinkedQueue<>();
    ConcurrentHashMap<String, GameWebSocket> playerNamesToSocket = new ConcurrentHashMap<>();
    ConcurrentLinkedQueue<GameProtocol> runningSessions = new ConcurrentLinkedQueue<>();

    int matchCounter = 0;

    public void run() {
        while (!stopped && !(Thread.interrupted())) {
            /// Marvel at my GENIUS matchmaking
            /// Combines ideas from Elo, StarCraft ladder, and whatever that DotA has.
            if (unmatchedSockets.size() > 1) {
                //startGame(unmatchedSockets.poll(), unmatchedSockets.poll());
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                return;
            }
        }
    }

    public void stop() {
        stopped = true;
    }

    private void startGame(GameWebSocket first, GameWebSocket second) {
        GameProtocol protocol = new GameProtocol();
        first.setProtocol(protocol);
        first.setFirstPlayer(true);
        second.setProtocol(protocol);
        second.setFirstPlayer(false);
        first.startGame();
        second.startGame();
        protocol.start(first, second);
        runningSessions.add(protocol);
    }
    public ArrayList<String> getUnmatchedPlayerNames()
    {
        ArrayList<String> ret = new ArrayList<>();
        for (GameWebSocket i : unmatchedSockets)
        {
            ret.add(i.getName());
        }
        return ret;
    }

    public Boolean startGameForPlayers(String player1, String player2)
    {
        GameWebSocket socket1 = playerNamesToSocket.get(player1);
        GameWebSocket socket2 = playerNamesToSocket.get(player2);
        if (unmatchedSockets.contains(socket1) && unmatchedSockets.contains(socket2))
        {
            unmatchedSockets.remove(socket1);
            unmatchedSockets.remove(socket2);
            if (matchCounter++ % 2 == 0)
                startGame(socket1, socket2);
            else
                startGame(socket2, socket1);
            return true;
        }
        return false;
    }

    public void addSocket(GameWebSocket socket)
    {
        for (GameWebSocket i : unmatchedSockets)
        {
            if (i.getName().equals(socket.getName()))
                unmatchedSockets.remove(i);
        }
        unmatchedSockets.add(socket);
        playerNamesToSocket.put(socket.getName(), socket);
    }



}
