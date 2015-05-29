package frontend.websockets;

import base.AccountService.AccountService;
import frontend.AccountService.AccountServiceImpl;
import mechanics.GameProtocol;
import messageSystem.MessageSystem;

import java.util.ArrayList;

/**
 * Created by Artem on 5/19/2015.
 */

public class GameSessionManager implements Runnable  {
    private MessageSystem messageSystem;
    private AccountService accountService;

    //// SINGLETON (for a reason, shut up)
    private static GameSessionManager instance;
    public static GameSessionManager getInstance(MessageSystem messageSystem, AccountService accountService) {
        if (instance == null) {
            instance = new GameSessionManager(messageSystem, accountService);
        }
        return instance;
    }
    //// SINGLETON

    public GameSessionManager(MessageSystem messageSystem, AccountService accountService) {
        this.messageSystem = messageSystem;
        this.accountService = accountService;

    }

    private volatile boolean stopped = false;

    public void run() {
        while (!stopped && !(Thread.interrupted())) {
            /// Marvel at my GENIUS matchmaking
            /// Combines ideas from Elo, StarCraft ladder, and whatever that DotA has.
            if (unmatchedSockets.size() > 1) {
                startGame(unmatchedSockets.get(0), unmatchedSockets.get(1));
                unmatchedSockets.remove(0);
                unmatchedSockets.remove(0);
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                return;
            }
        }
    }

    ArrayList<GameWebSocket> unmatchedSockets = new ArrayList<>();

    ArrayList<GameProtocol> runningSessions = new ArrayList<>();

    public void stop() {
        stopped = true;
    }

    private void startGame(GameWebSocket first, GameWebSocket second) {
        GameProtocol protocol = new GameProtocol(messageSystem, accountService);
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
