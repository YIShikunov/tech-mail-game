package frontend;

import frontend.websockets.GameWebSocket;
import mechanics.GameProtocol;
import org.eclipse.jetty.util.ConcurrentArrayQueue;

import java.util.ArrayList;
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

    ConcurrentLinkedQueue<GameProtocol> runningSessions = new ConcurrentLinkedQueue<>();

    public void run() {
        while (!stopped && !(Thread.interrupted())) {
            /// Marvel at my GENIUS matchmaking
            /// Combines ideas from Elo, StarCraft ladder, and whatever that DotA has.
            if (unmatchedSockets.size() > 1) {
                startGame(unmatchedSockets.poll(), unmatchedSockets.poll());
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

    public void addSocket(GameWebSocket socket) {
        unmatchedSockets.add(socket);
    }



}
