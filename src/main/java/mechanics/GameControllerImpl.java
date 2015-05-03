package mechanics;

import base.mechanics.GameController;
import mechanics.GameState.Board;
import mechanics.GameState.Element;
import mechanics.GameState.FieldType;
import mechanics.GameState.Outcome;

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

    public void init() {
        state = WaitingFor.PLACEMENT;
        board = new Board();
        board.arrangePieces();
    }

    public boolean placePieces(boolean isFirstPlayer, HashMap<Integer, Element> placement) {
        if (state != WaitingFor.PLACEMENT)
            return false;
        for (int fieldID : placement.keySet())
            if (!board.doesOwn(isFirstPlayer, fieldID))
                return false;

        HashMap<Element, Integer> counts = new HashMap<>();
        counts.put(Element.FIRE, 0);
        counts.put(Element.WATER, 0);
        counts.put(Element.WOOD, 0);
        counts.put(Element.EARTH, 0);
        counts.put(Element.METAL, 0);
        for (Element element : placement.values())
            counts.put(element, counts.get(element)+1);
        for (int count : counts.values())
            if (count != 3)
                return false;


        board.placeElements(placement);
        if (board.allSet()) {
            state = WaitingFor.FIRST_TURN;
        }
        return true;
    }

    public boolean makeTurn(boolean isFirstPlayer, int fromID, int toID) { // TODO: a bit messy
        if (isFirstPlayer && state != WaitingFor.FIRST_TURN)
            return false;
        if (!isFirstPlayer && state != WaitingFor.SECOND_TURN)
            return false;
        if (!board.doesOwn(isFirstPlayer, fromID)) {
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
        return false;
    }

    public boolean answerPrompt(boolean isFirstPlayer, Element element) {
        if (isFirstPlayer && state != WaitingFor.FIRST_ELEMENT_CHOICE)
            return false;
        if (!isFirstPlayer && state != WaitingFor.SECOND_ELEMENT_CHOICE)
            return false;
        if (!board.doesOwn(isFirstPlayer, promptTarget))
            return false;

        if (state == WaitingFor.FIRST_ELEMENT_CHOICE) {
            state = WaitingFor.SECOND_TURN;
        } else if (state == WaitingFor.SECOND_ELEMENT_CHOICE)
            state = WaitingFor.FIRST_TURN;
        return board.changeElement(promptTarget, element);
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
        if (isFirstPlayer && state != WaitingFor.FIRST_TURN)
            return false;
        if (!isFirstPlayer && state != WaitingFor.SECOND_TURN)
            return false;
        return board.changeKingElement(isFirstPlayer, element);
    }
}
