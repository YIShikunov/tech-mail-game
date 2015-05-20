package mechanics;

import ResourceLoader.GSResources;
import ResourceLoader.ResourcesService;
import base.mechanics.GameController;
import javafx.util.Pair;
import mechanics.GameState.*;

import java.security.KeyException;
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

    private void flipTurn()
    {
        if (state == WaitingFor.FIRST_TURN)
            state = WaitingFor.SECOND_TURN;
        else if (state == WaitingFor.SECOND_TURN)
            state = WaitingFor.FIRST_TURN;
    }

    public boolean placePieces(boolean isFirstPlayer, HashMap<Integer, Element> placement) {
        if (state != WaitingFor.PLACEMENT) {
            getErrorMessage("NOT_PLACEMENT");
            return false;
        }
        for (int fieldID : placement.keySet())
            if (!board.doesOwn(isFirstPlayer, fieldID)) {
                getErrorMessage("DO_NOT_OWN");
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
                getErrorMessage("WRONG_PLACEMENT");
                return false;
            }


        board.placeElements(placement);
        if (board.allSet()) {
            state = WaitingFor.FIRST_TURN;
        }
        return true;
    }


    private TurnResult MakeBattle(boolean isFirstPlayer, int fromID, int toID)
    {
        TurnResult res = new TurnResult();
        Piece from = board.fields.get(fromID).getPiece();
        Piece to = board.fields.get(toID).getPiece();

        Element fromKingPreviousElement = from.getElement();
        Element toKingPreviousElement = to.getElement();

        Outcome outcome = board.attackPiece(fromID, toID);
        if (outcome != Outcome.ERROR) {
            if (board.doesOwn(isFirstPlayer, toID)) {
                if (board.fieldType(toID) == FieldType.THRONE) {
                    if (isFirstPlayer)
                        state = WaitingFor.FIRST_ELEMENT_CHOICE;
                    else
                        state = WaitingFor.SECOND_ELEMENT_CHOICE;
                    res.recolor = true;
                    promptTarget = toID;
                }
                if (from.king || to.king)
                {
                    if (from.king)
                    {
                        res.piecesRevealed.add(new Pair<>(fromID, fromKingPreviousElement));
                        res.piecesRevealed.add(new Pair<>(toID, to.getElement()));
                        switch (outcome)
                        {
                            case DESTRUCTION:
                            {
                                res.piecesDestroyed.add(toID);
                                break;
                            }
                            case DRAW:
                            {
                                break;
                            }
                            case LOSS:
                            {
                                break;
                            }
                            case WIN:
                            {
                                res.piecesDestroyed.add(toID);
                                res.piecesMoved.add(new Pair<>(fromID, toID));
                                break;
                            }
                        }
                    }
                    else if(to.king)
                    {
                        res.piecesRevealed.add(new Pair<>(toID, toKingPreviousElement));
                        res.piecesRevealed.add(new Pair<>(fromID, from.getElement()));
                        switch (outcome)
                        {
                            case DESTRUCTION:
                            {
                                res.piecesDestroyed.add(fromID);
                                break;
                            }
                            case DRAW:
                            {
                                break;
                            }
                            case LOSS:
                            {
                                res.piecesDestroyed.add(fromID);
                                break;
                            }

                            case WIN:
                            {
                                break;
                            }
                        }
                    }
                }
                else
                {
                    res.piecesRevealed.add(new Pair<>(fromID, from.getElement()));
                    res.piecesRevealed.add(new Pair<>(toID, to.getElement()));
                    switch (outcome)
                    {
                        case DESTRUCTION:
                        {
                            res.piecesDestroyed.add(fromID);
                            res.piecesDestroyed.add(toID);
                            break;
                        }
                        case DRAW:
                        {
                            break;
                        }
                        case WIN:
                        {
                            res.piecesDestroyed.add(toID);
                            res.piecesMoved.add(new Pair<>(fromID, toID));
                            break;
                        }
                        case LOSS:
                        {
                            res.piecesDestroyed.add(fromID);
                            break;
                        }
                    }
                }
            }
            flipTurn();
            res.status = true;
            res.battleResult = outcome.toString();
            return res;
        }

        res.errorMessage = getErrorMessage("INVALID_TURN");
        res.status = false;
        return res;
    }

    private TurnResult makeMove(boolean isFirstPlayer, int fromID, int toID)
    {
        TurnResult res = new TurnResult();
        if (board.movePiece(fromID, toID)) {
            if (board.fieldType(toID) == FieldType.THRONE) {
                if (isFirstPlayer)
                    state = WaitingFor.FIRST_ELEMENT_CHOICE;
                else
                    state = WaitingFor.SECOND_ELEMENT_CHOICE;
                promptTarget = toID;
                res.recolor = true;
            }
            res.piecesMoved.add(new Pair<>(fromID, toID));
            flipTurn();
            res.status = true;
            return res;
        }
        else
        {
            return null;
        }
    }

    public TurnResult makeTurn(boolean isFirstPlayer, int fromID, int toID) {
        TurnResult res = new TurnResult();
        if (isFirstPlayer && state != WaitingFor.FIRST_TURN || !isFirstPlayer && state != WaitingFor.SECOND_TURN) {
            res.errorMessage = getErrorMessage("NOT_YOUR_TURN");
            res.status = false;
            return res;
        }
        /*Piece king = board.getKing(isFirstPlayer);
        if (king.getElement() == Element.BLANK || !king.hasBackupElement()){
            getErrorMessage("KING_NOT_SET");
            return false;
        }*/ // TODO: resolve king question

        if (!board.doesOwn(isFirstPlayer, fromID))  {
            res.errorMessage = getErrorMessage("DO_NOT_OWN");
            res.status = false;
            return res;
        }

        res = makeMove(isFirstPlayer, fromID, toID);

        if (res == null)
            return MakeBattle(isFirstPlayer, fromID, toID);
        else
            return res;
    }

    public boolean answerPrompt(boolean isFirstPlayer, Element element) {
        if (isFirstPlayer && state != WaitingFor.FIRST_ELEMENT_CHOICE ||
                !isFirstPlayer && state != WaitingFor.SECOND_ELEMENT_CHOICE) {
            getErrorMessage("NOT_YOUR_TURN");
            return false;
        }

        if (!board.doesOwn(isFirstPlayer, promptTarget)){
            getErrorMessage("DO_NOT_OWN");
            return false;
        }

        if (state == WaitingFor.FIRST_ELEMENT_CHOICE) {
            state = WaitingFor.SECOND_TURN;
        } else if (state == WaitingFor.SECOND_ELEMENT_CHOICE)
            state = WaitingFor.FIRST_TURN;

        Boolean success = board.changeElement(promptTarget, element);
        if (!success)
            getErrorMessage("WRONG_ELEMENT");
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

    public boolean changeKingElement(boolean isFirstPlayer, Element element) {
        if (isFirstPlayer && state != WaitingFor.FIRST_TURN || !isFirstPlayer && state != WaitingFor.SECOND_TURN) {
            getErrorMessage("NOT_YOUR_TURN");
            return false;
        }

        Boolean success = board.changeKingElement(isFirstPlayer, element);
        if (!success)
            getErrorMessage("WRONG_ELEMENT");
        return success;
    }

    private String getErrorMessage(String id) {
        String response = messages.get(id);
        if (response == null) {
            // the "totally erroneous and obnoxious output so we would see there is a problem with our resource file"
            return "I HEARD YOU LIKE ERRORS MESSAGES SO I PUT AN ERROR MESSAGE IN YOUR ERROR MESSAGE";
        }
        return response;
    }

    public String popErrorMessage() {
        String result = this.errorMessage;
        this.errorMessage = null;
        return result;
    }
}
