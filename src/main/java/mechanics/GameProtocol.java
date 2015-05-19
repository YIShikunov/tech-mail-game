package mechanics;

import base.WebSocketService;
import base.mechanics.GameController;
import mechanics.GameState.Element;
import org.json.simple.JSONObject;

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

    public boolean process(boolean isFirstPlayer, JSONObject packet) {
        if (!packet.containsKey("typeID") || !(packet.get("typeID") instanceof Integer)){
            notifyError(isFirstPlayer);
            return false;
        }
        Integer type = (Integer) packet.get("typeID");
        switch (type){
            case 1: return notifyResponse(isFirstPlayer, 2, receivePlacement(isFirstPlayer, packet));
            case 3: return notifyResponse(isFirstPlayer, 4, receiveTurn(isFirstPlayer, packet));
            case 5: return notifyResponse(isFirstPlayer, 6, receiveElementPrompt(isFirstPlayer, packet));
            case 7: return notifyResponse(isFirstPlayer, 8, receiveSwapKing(isFirstPlayer, packet));
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
        } catch (ClassCastException e)
        {
            return false;
        }
    }

    protected boolean receiveTurn(boolean isFirstPlayer, JSONObject packet) {
        try {
            Integer from = (Integer) packet.get("moveFrom");
            Integer to = (Integer) packet.get("moveTo");
            return gameController.makeTurn(isFirstPlayer, from, to).status;
        } catch (ClassCastException e) {
            return false;
        }
    }

    protected boolean receiveElementPrompt(boolean isFirstPlayer, JSONObject packet) {
        try {
            Integer elementID = (Integer) packet.get("recolor");
            return gameController.answerPrompt(isFirstPlayer, Element.value(elementID));
        } catch (ClassCastException e) {
            return false;
        }
    }

    protected boolean receiveSwapKing(boolean isFirstPlayer, JSONObject packet) {
        try {
            Integer elementID = (Integer) packet.get("recolor");
            return gameController.changeKingElement(isFirstPlayer, Element.value(elementID));
        } catch (ClassCastException e) {
            return false;
        }
    }



    protected void notifyError(boolean isFirstPlayer) {
        // TODO
        // This represents an error as in "incorrectly-formed packet", not "it is not a valid turn"
    }

    protected boolean notifyResponse(boolean isFirstPlayer, int code, boolean success) {
        JSONObject packet = new JSONObject();
        packet.put("statusOK", success);
        packet.put("typeID", code);
        packet.put("errorMessage", gameController.popErrorMessage());

        webSocketService.send(isFirstPlayer, packet);
        return true;
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
