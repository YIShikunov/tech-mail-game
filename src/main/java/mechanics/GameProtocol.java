package mechanics;

import base.WebSocketService;
import base.mechanics.GameController;
import org.json.simple.JSONObject;

public class GameProtocol {

    private GameController gameController;
    private WebSocketService webSocketService;

    private String firstPlayerName;
    private String secondPlayerName;

    private int turn;

    public GameProtocol(WebSocketService webSocketService) {
        this.webSocketService = webSocketService;
    }

    public boolean init(String firstPlayerName, String secondPlayerName) {
        this.firstPlayerName = firstPlayerName;
        this.secondPlayerName = secondPlayerName;
        this.turn = 0;
        this.gameController = new GameControllerImpl();
        this.gameController.init();
        this.notifyStartGame(true, secondPlayerName);
        this.notifyStartGame(false, firstPlayerName);
        return true;
    }

    public boolean receive(boolean isFirstPlayer, JSONObject packet) {
        if (!packet.containsKey("typeID") || !(packet.get("typeID") instanceof Integer)){
            notifyError(isFirstPlayer);
            return false;
        }
        Integer type = (Integer) packet.get("typeID");
        // TODO: check for further errors in packages
        switch (type){
            case 1: return receivePlacement(isFirstPlayer, packet);
            case 3: return receiveTurn(isFirstPlayer, packet);
            default: return false; //TODO: rest of cases
        }
    }

    private boolean receivePlacement(boolean isFirstPlayer, JSONObject packet) {
        return false; // TODO
    }

    private boolean receiveTurn(boolean isFirstPlayer, JSONObject packet) {
        return false; // TODO
    }


    private void notifyError(boolean isFirstPlayer) {
        // TODO
        // This represents an error as in "incorrectly-firmed packet", not "it is not a valid turn"
    }


    private void notifyStartGame(boolean isFirstPlayer, String enemyName) {
        JSONObject packet = new JSONObject();
        packet.put("statusOK", true);
        packet.put("typeID", 0);
        packet.put("opponent", enemyName);
        packet.put("youStart", isFirstPlayer);

        webSocketService.send(isFirstPlayer, packet);
    }

    private void notifyAcceptPlacement(boolean isFirstPlayer) {

    }



}
