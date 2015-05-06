package mechanics;

import base.WebSocketService;
import base.mechanics.GameController;
import mechanics.GameState.Element;
import org.json.simple.JSONObject;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;

public class GameProtocol {

    protected GameController gameController;
    protected WebSocketService webSocketService;

    protected String firstPlayerName;
    protected String secondPlayerName;

    protected int turn;

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
        // False means the packet is bad-formed, not that the turn is invalid!
        if (!packet.containsKey("typeID") || !(packet.get("typeID") instanceof Integer)){
            return false;
        }
        Integer type = (Integer) packet.get("typeID");
        switch (type){
            case 1: return receivePlacement(isFirstPlayer, packet);
            case 3: return receiveTurn(isFirstPlayer, packet);
            case 5: return receiveElementPropmt(isFirstPlayer, packet);
            case 7: return receiveSwapKing(isFirstPlayer, packet);
            default: return false;
        }
    }

    protected boolean receivePlacement(boolean isFirstPlayer, JSONObject packet) {
        HashMap<Integer, Element> placement = new HashMap<>();
        try {
            for (Integer fieldID : (ArrayList<Integer>) packet.get("element0")) {
                placement.put(fieldID, Element.value(0));
            }
            for (Integer fieldID : (ArrayList<Integer>) packet.get("element1")) {
                placement.put(fieldID, Element.value(1));
            }
            for (Integer fieldID : (ArrayList<Integer>) packet.get("element2")) {
                placement.put(fieldID, Element.value(2));
            }
            for (Integer fieldID : (ArrayList<Integer>) packet.get("element3")) {
                placement.put(fieldID, Element.value(3));
            }
            for (Integer fieldID : (ArrayList<Integer>) packet.get("element4")) {
                placement.put(fieldID, Element.value(4)); // TODO: collapse it
            }
            return gameController.placePieces(isFirstPlayer, placement);
        } catch (ClassCastException e) {
            return false;
        }catch (JSONException e) {
            return false;
        }
    }

    protected boolean receiveTurn(boolean isFirstPlayer, JSONObject packet) {
        try {
            Integer from = packet.get();
        } catch (ClassCastException e) {
            return false;
        }
    }


    protected void notifyError(boolean isFirstPlayer) {
        // TODO
        // This represents an error as in "incorrectly-firmed packet", not "it is not a valid turn"
    }


    protected void notifyStartGame(boolean isFirstPlayer, String enemyName) {
        JSONObject packet = new JSONObject();
        packet.put("statusOK", true);
        packet.put("typeID", 0);
        packet.put("opponent", enemyName);
        packet.put("youStart", isFirstPlayer);

        webSocketService.send(isFirstPlayer, packet);
    }

    protected void notifyAcceptPlacement(boolean isFirstPlayer) {

    }



}
