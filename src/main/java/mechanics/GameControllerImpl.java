package mechanics;

import ResourceLoader.GSResources;
import ResourceLoader.ResourcesService;
import base.mechanics.GameController;
import com.sun.org.apache.xpath.internal.operations.Bool;
import javafx.util.Pair;
import mechanics.GameState.*;

import java.util.ArrayList;
import java.util.HashMap;

public class GameControllerImpl implements GameController {

    protected enum WaitingFor {
        PLACEMENT,
        FIRST_TURN,
        SECOND_TURN,
        FIRST_ELEMENT_CHOICE,
        SECOND_ELEMENT_CHOICE,
        FIRST_PLAYER_WON,
        SECOND_PLAYER_WON
    }

    protected WaitingFor state;
    protected Board board;

    protected int promptTarget;

    protected String errorMessage;

    protected ArrayList<Pair<Integer, Integer>> piecesMoved;
    protected ArrayList<Pair<Integer, Element>> piecesRevealed;
    protected ArrayList<Integer> piecesDestroyed;

    private HashMap<String, String> messages;

    public void init() {
        state = WaitingFor.PLACEMENT;
        board = new Board();
        board.arrangePieces();
        messages = loadMessages();
    }

    private HashMap<String, String> loadMessages() {
        GSResources settings = ResourcesService.getInstance().getResources("messages.xml");
        HashMap<String, String> messages = new HashMap<>();
        for (GSResources raw : settings.getContentByName("message")) {
            messages.put(raw.getSetting("id"), raw.getSetting("value"));
        }
        return messages;
    }

    public boolean placePieces(boolean isFirstPlayer, HashMap<Integer, Element> placement) {
        if (state != WaitingFor.PLACEMENT) {
            setErrorMessage("NOT_PLACEMENT");
            return false;
        }
        for (int fieldID : placement.keySet())
            if (!board.doesOwn(isFirstPlayer, fieldID)) {
                setErrorMessage("DO_NOT_OWN");
                return false;
            }

        HashMap<Element, Integer> counts = new HashMap<>();
        counts.put(Element.FIRE, 0);
        counts.put(Element.WATER, 0);
        counts.put(Element.WOOD, 0);
        counts.put(Element.EARTH, 0);
        counts.put(Element.METAL, 0);
        for (Element element : placement.values())
            counts.put(element, counts.get(element)+1);
        for (int count : counts.values())
            if (count != 3) {
                setErrorMessage("WRONG_PLACEMENT");
                return false;
            }


        board.placeElements(placement);
        if (board.allSet()) {
            state = WaitingFor.FIRST_TURN;
        }
        return true;
    }

    public boolean makeTurn(boolean isFirstPlayer, int fromID, int toID) {
        if (isFirstPlayer && state != WaitingFor.FIRST_TURN || !isFirstPlayer && state != WaitingFor.SECOND_TURN) {
            setErrorMessage("NOT_YOUR_TURN");
            return false;
        }

        /*Piece king = board.getKing(isFirstPlayer);
        if (king.getElement() == Element.BLANK || !king.hasBackupElement()){
            setErrorMessage("KING_NOT_SET");
            return false;
        }*/ // TODO: resolve king question

        if (!board.doesOwn(isFirstPlayer, fromID))  {
            setErrorMessage("DO_NOT_OWN");
            return false;
        }

        if (board.movePiece(fromID, toID)) {
            if (board.fieldType(toID) == FieldType.THRONE) {
                if (isFirstPlayer)
                    state = WaitingFor.FIRST_ELEMENT_CHOICE;
                else
                    state = WaitingFor.SECOND_ELEMENT_CHOICE;
                promptTarget = toID;
            }
            if (state == WaitingFor.FIRST_TURN)
                state = WaitingFor.SECOND_TURN;
            else if (state == WaitingFor.SECOND_TURN)
                state = WaitingFor.FIRST_TURN;
            return true;
        }

        Outcome outcome = board.attackPiece(fromID, toID);
        if (outcome != Outcome.ERROR) {
            if (board.doesOwn(isFirstPlayer, toID)) {
                if (board.fieldType(toID) == FieldType.THRONE) {
                    if (isFirstPlayer)
                        state = WaitingFor.FIRST_ELEMENT_CHOICE;
                    else
                        state = WaitingFor.SECOND_ELEMENT_CHOICE;
                    promptTarget = toID;
                }
            }
            if (state == WaitingFor.FIRST_TURN)
                state = WaitingFor.SECOND_TURN;
            else if (state == WaitingFor.SECOND_TURN)
                state = WaitingFor.FIRST_TURN;
            return true;
        }

        setErrorMessage("INVALID_TURN");
        return false;
    }

    public boolean answerPrompt(boolean isFirstPlayer, Element element) {
        if (isFirstPlayer && state != WaitingFor.FIRST_ELEMENT_CHOICE ||
                !isFirstPlayer && state != WaitingFor.SECOND_ELEMENT_CHOICE) {
            setErrorMessage("NOT_YOUR_TURN");
            return false;
        }

        if (!board.doesOwn(isFirstPlayer, promptTarget)){
            setErrorMessage("DO_NOT_OWN");
            return false;
        }

        if (state == WaitingFor.FIRST_ELEMENT_CHOICE) {
            state = WaitingFor.SECOND_TURN;
        } else if (state == WaitingFor.SECOND_ELEMENT_CHOICE)
            state = WaitingFor.FIRST_TURN;

        Boolean success = board.changeElement(promptTarget, element);
        if (!success)
            setErrorMessage("WRONG_ELEMENT");
        return success;
    }


    public boolean concede(boolean isFirstPlayer) {
       return false; //TODO: implement
    }

    public boolean isWaitingForPrompt(boolean isFirstPlayer){
        if (isFirstPlayer)
            return state == WaitingFor.FIRST_ELEMENT_CHOICE;
        else
            return state == WaitingFor.SECOND_ELEMENT_CHOICE;
    }

    public Object subjectiveGameData(boolean isFirstPlayer) {
        return new Object();
    } // TODO pack all game data as seen by a player into a JSON

    public boolean changeKingElement(boolean isFirstPlayer, Element element) {
        if (isFirstPlayer && state != WaitingFor.FIRST_TURN || !isFirstPlayer && state != WaitingFor.SECOND_TURN) {
            setErrorMessage("NOT_YOUR_TURN");
            return false;
        }

        Boolean success = board.changeKingElement(isFirstPlayer, element);
        if (!success)
            setErrorMessage("WRONG_ELEMENT");
        return success;
    }

    private void setErrorMessage(String id) {
        errorMessage = messages.get(id);
    }

    public String popErrorMessage() {
        String result = this.errorMessage;
        this.errorMessage = null;
        return result;
    }
}
