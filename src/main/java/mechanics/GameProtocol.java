package mechanics;

import base.mechanics.GameController;
import frontend.websockets.GameWebSocket;
import mechanics.GameState.Element;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.HashMap;

public class GameProtocol {

    protected GameController gameController;

    private GameWebSocket firstPlayerSocket;
    private GameWebSocket secondPlayerSocket;

    protected int turn;
    protected boolean gameActive;

    public GameProtocol() {

    }

    public boolean start(GameWebSocket first, GameWebSocket second) {
        this.firstPlayerSocket = first;
        this.secondPlayerSocket = second;
        this.gameActive = true;
        this.turn = 0;
        this.gameController = new GameControllerImpl();
        this.gameController.init();
        this.notifyStartGame(true, second.getName());
        this.notifyStartGame(false, first.getName());
        return true;
    }

    public void process(boolean isFirstPlayer, String packetString) {
        boolean result = execute(isFirstPlayer, packetString);
        if (!result) {
            notifyError(isFirstPlayer);
        }
    }

    // False means that the packet is bad-formed or something, not that the turn is invalid!
    public boolean execute(boolean isFirstPlayer, String packetString) {
        JSONParser jsonParser = new JSONParser();
        JSONObject packet;
        try {
            packet = new JSONObject((JSONObject)(jsonParser.parse(packetString)));
        } catch (ParseException e) {
            System.out.print(e.toString());
            return false;
        }
        if (!packet.containsKey("typeID")){
            try {
                Integer a = (int) (long) packet.get("typeID");
            } catch (ClassCastException e) {
                System.out.print("TypeID is not int");
                return false;
            }
            System.out.print("No typeID specified!");
            return false;
        }
        Integer type = (int) (long) packet.get("typeID");
        switch (type){
            case 1: return receivePlacement(isFirstPlayer, packet);
            case 3: return receiveTurn(isFirstPlayer, packet);
            case 5: return receiveElementPrompt(isFirstPlayer, packet);
            case 7: return receiveSwapKing(isFirstPlayer, packet);
            default:
                System.out.print("Wrong typeID specified!");
                return false;
        }
    }


    protected boolean firstPlayerReady = false;
    protected boolean secondPlayerReady = false;


    protected boolean sendKingPongPackets(boolean isFirstPlayer, TurnResult result)
    {
        if (result.king1Status != null && result.king2Status != null)
        {
            /*if (result.king1Status.size() < 1 || result.king2Status.size() < 1)
            {
                notifyEndGame(isFirstPlayer, );*/
            if (false) {} else {
                JSONObject player1KingPacket = new JSONObject();
                JSONObject player2KingPacket = new JSONObject();

                player1KingPacket.put("typeID", 10);
                player1KingPacket.put("statusOK", true);
                player1KingPacket.put("isYourKing", isFirstPlayer);
                player2KingPacket.put("typeID", 10);
                player2KingPacket.put("statusOK", true);
                player2KingPacket.put("isYourKing", !isFirstPlayer);

                player1KingPacket.put("Elements", result.king1Status.toArray());
                player2KingPacket.put("Elements", result.king2Status.toArray());
                send(isFirstPlayer, player1KingPacket);
                send(isFirstPlayer, player2KingPacket);
                return true;
            }
        }
        return false;
    }


    protected boolean receivePlacement(boolean isFirstPlayer, JSONObject packet) {
        HashMap<Integer, Element> placement = new HashMap<>();
        boolean status;
        try {
            for (Long fieldID : (ArrayList<Long>) packet.get("element0")) {
                placement.put((int) (long) fieldID, Element.value(0));
            }
            for (Long fieldID : (ArrayList<Long>) packet.get("element1")) {
                placement.put((int) (long) fieldID, Element.value(1));
            }
            for (Long fieldID : (ArrayList<Long>) packet.get("element2")) {
                placement.put((int) (long) fieldID, Element.value(2));
            }
            for (Long fieldID : (ArrayList<Long>) packet.get("element3")) {
                placement.put((int) (long) fieldID, Element.value(3));
            }
            for (Long fieldID : (ArrayList<Long>) packet.get("element4")) {
                placement.put((int) (long) fieldID, Element.value(4)); // TODO: collapse it
            }
        } catch (ClassCastException e) {
            System.out.print("Class cast exception!");
            return false;
        }
        TurnResult result;
        result = gameController.placePieces(isFirstPlayer, placement);

        if (result.status) {
            if (isFirstPlayer)
                firstPlayerReady = true;
            else
                secondPlayerReady = true;
            boolean otherReady = isFirstPlayer ? secondPlayerReady : firstPlayerReady;

            JSONObject firstPlayerResponse = new JSONObject();
            firstPlayerResponse.put("typeID", 2);
            firstPlayerResponse.put("statusOK", true);
            firstPlayerResponse.put("opponentReady", otherReady);
            send(isFirstPlayer, firstPlayerResponse);

            if (otherReady) {
                JSONObject secondPlayerResponse = new JSONObject();
                secondPlayerResponse.put("typeID", 2);
                secondPlayerResponse.put("statusOK", true);
                secondPlayerResponse.put("opponentReady", true);
                send(!isFirstPlayer, secondPlayerResponse);
            }
        } else {
            JSONObject firstPlayerResponse = new JSONObject();
            firstPlayerResponse.put("typeID", 2);
            firstPlayerResponse.put("statusOK", false);
            firstPlayerResponse.put("opponentReady", false);
            firstPlayerResponse.put("errorMessage",
                    result.errorMessage);
            send(isFirstPlayer, firstPlayerResponse);
        }
        return true;
    }

    protected JSONObject stashedResponse;
    protected TurnResult stashedResult;

    protected boolean receiveTurn(boolean isFirstPlayer, JSONObject packet) {
        TurnResult result;
        try {
            Integer from = (int) (long) packet.get("moveFrom");
            Integer to = (int) (long) packet.get("moveTo");
            result = gameController.makeTurn(isFirstPlayer, from, to);
        } catch (ClassCastException e) {
            return false;
        }

        if (result.status) {
            JSONObject response = turnResultToJSON(result);
            response.put("typeID", 4);
            response.put("turn", turn);
            send(isFirstPlayer, response);
            if (result.recolor) {
                stashedResponse = response;
                stashedResult = result;
            } else {
                send(!isFirstPlayer, response);
                sendKingPongPackets(!isFirstPlayer, result);
                sendKingPongPackets(isFirstPlayer, result);
            }
        } else {
            JSONObject response = new JSONObject();
            response.put("typeID", 4);
            response.put("statusOK", false);
            response.put("errorMessage", result.errorMessage);
            send(isFirstPlayer, response);
        }
        return true;
    }

    protected boolean receiveElementPrompt(boolean isFirstPlayer, JSONObject packet) {
        TurnResult result;
        try {
            Integer elementID = (int) (long) packet.get("baseRecolor");
            result = gameController.answerPrompt(isFirstPlayer, Element.value(elementID));
        } catch (ClassCastException e) {
            return false;
        }

        if (result.status) {
            JSONObject response = new JSONObject();
            response.put("statusOK", true);
            response.put("typeID", 6);
            send(isFirstPlayer, response);
            send(!isFirstPlayer, stashedResponse);
            sendKingPongPackets(isFirstPlayer, stashedResult);
            sendKingPongPackets(!isFirstPlayer, stashedResult);
            stashedResult = null;
        } else {
            JSONObject response = new JSONObject();
            response.put("statusOK", false);
            response.put("typeID", 6);
            response.put("errorMessage", result.errorMessage);
            send(isFirstPlayer, response);
        }
        return true;
    }

    protected boolean receiveSwapKing(boolean isFirstPlayer, JSONObject packet) {
        TurnResult result;
        try {
            Integer elementID = (int) (long) packet.get("kingRecolor");
            result = gameController.changeKingElement(isFirstPlayer, Element.value(elementID));
        } catch (ClassCastException e) {
            return false;
        }

        if (result.status) {
            JSONObject response = new JSONObject();
            response.put("statusOK", true);
            response.put("typeID", 8);
            send(isFirstPlayer, response);
        } else {
            JSONObject response = new JSONObject();
            response.put("statusOK", false);
            response.put("typeID", 8);
            response.put("errorMessage", result.errorMessage);
            send(isFirstPlayer, response);
        }
        return true;
    }



    protected void notifyError(boolean isFirstPlayer) {
        JSONObject response = new JSONObject();
        response.put("typeID", -42);
        response.put("errorMessage", "Your data is invalid.");
        response.put("statusOK", false);
        send(isFirstPlayer, response);
    }

    protected void notifyStartGame(boolean isFirstPlayer, String enemyName) {
        JSONObject packet = new JSONObject();
        packet.put("statusOK", true);
        packet.put("typeID", 0);
        packet.put("opponent", enemyName);
        packet.put("youStart", isFirstPlayer);

        if (isFirstPlayer)
            firstPlayerSocket.send(packet);
        else
            secondPlayerSocket.send(packet);
    }

    public synchronized void notifyEndGame(boolean isFirstPlayer, boolean isWon, boolean isError)
    {
        JSONObject packet = new JSONObject();
        if (!isError)
        {
            packet.put("statusOK", !isError);
            packet.put("typeID", -1);
            packet.put("iAmWinner", isWon);
            send(isFirstPlayer, packet);
        }
        packet = new JSONObject();
        packet.put("statusOK", !isError);
        packet.put("typeID", -1);
        packet.put("iAmWinner", !isWon);
        send(!isFirstPlayer, packet);
    }

    protected void send(boolean isFirstPlayer, JSONObject packet) {
        if (isFirstPlayer) {
            firstPlayerSocket.send(packet);
        } else {
            secondPlayerSocket.send(packet);
        }
    }

    JSONObject turnResultToJSON(TurnResult turnResult) {
        JSONObject result = new JSONObject();
        result.put("piecesMoved", turnResult.piecesMoved);
        result.put("piecesRevealed", turnResult.piecesRevealed);
        result.put("piecesDestroyed", turnResult.piecesDestroyed);
        result.put("piecesHidden", turnResult.piecesHidden);
        result.put("statusOK", turnResult.status);
        result.put("recolor", turnResult.recolor);
        result.put("errorMessage", turnResult.errorMessage);
        result.put("battleResult", turnResult.battleResult);
        return result;
    }
}
