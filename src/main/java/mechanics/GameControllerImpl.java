package mechanics;

import ResourceLoader.GSResources;
import ResourceLoader.ResourcesService;
import base.mechanics.GameController;
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

    private synchronized void flipTurn()
    {
        if (state == WaitingFor.FIRST_TURN)
            state = WaitingFor.SECOND_TURN;
        else if (state == WaitingFor.SECOND_TURN)
            state = WaitingFor.FIRST_TURN;
    }

    public synchronized TurnResult placePieces(boolean isFirstPlayer, HashMap<Integer, Element> placement) {
        TurnResult result = new TurnResult();
        if (state != WaitingFor.PLACEMENT) {
            result.status = false;
            result.errorMessage = getErrorMessage("NOT_PLACEMENT");
            return result;
        }
        for (int fieldID : placement.keySet())
            if (!board.doesOwn(isFirstPlayer, fieldID)) {
                result.status = false;
                result.errorMessage = getErrorMessage("DO_NOT_OWN");
                return result;
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
                result.status = false;
                result.errorMessage = getErrorMessage("WRONG_PLACEMENT");
                return result;
            }


        board.placeElements(placement);
        if (board.allSet()) {
            state = WaitingFor.FIRST_TURN;
        }
        result.status = true;
        return result;
    }


    private synchronized TurnResult makeBattle(boolean isFirstPlayer, int fromID, int toID)
    {
        TurnResult res = new TurnResult();
        Piece from = board.fields.get(fromID).getPiece();
        Piece to = board.fields.get(toID).getPiece();

        Element fromKingPreviousElement = Element.ERROR;
        Element toKingPreviousElement = Element.ERROR;

        if (from != null)
        {
            fromKingPreviousElement = from.getElement();
        }
        if (to != null)
        {
            toKingPreviousElement = to.getElement();
        }

        Outcome outcome = board.attackPiece(fromID, toID);
        if (outcome != Outcome.ERROR) {

            if (board.fieldType(toID) == FieldType.THRONE && (board.doesOwn(isFirstPlayer, toID)) ) {
                if (isFirstPlayer)
                    state = WaitingFor.FIRST_ELEMENT_CHOICE;
                else
                    state = WaitingFor.SECOND_ELEMENT_CHOICE;
                res.recolor = true;
                promptTarget = toID;
            }
            if (from.king || to.king)
            {
                res.king1Status = getKingStatus(true);
                res.king2Status = getKingStatus(false);
                res.king1Element = board.getKing(true).getElement();
                res.king2Element = board.getKing(false).getElement();

                if (from.king && to.king)
                {
                    ArrayList<Integer> tmp = new ArrayList<>();
                    tmp.add(toID);
                    tmp.add(toKingPreviousElement.id);
                    res.piecesRevealed.add(tmp);
                    tmp = new ArrayList<>();
                    tmp.add(fromID);
                    tmp.add(fromKingPreviousElement.id);
                    res.piecesRevealed.add(tmp);
                }
                else if (from.king)
                {
                    ArrayList<Integer> tmp = new ArrayList<>();
                    tmp.add(toID);
                    tmp.add(to.getElement().id);
                    res.piecesRevealed.add(tmp);
                    tmp = new ArrayList<>();
                    tmp.add(fromID);
                    tmp.add(fromKingPreviousElement.id);
                    res.piecesRevealed.add(tmp);

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
                            tmp = new ArrayList<>();
                            tmp.add(fromID);
                            tmp.add(toID);
                            res.piecesMoved.add(tmp);
                            break;
                        }
                    }
                }
                else if (to.king)
                {
                    ArrayList<Integer> tmp = new ArrayList<>();
                    tmp.add(toID);
                    tmp.add(toKingPreviousElement.id);
                    res.piecesRevealed.add(tmp);
                    tmp = new ArrayList<>();
                    tmp.add(fromID);
                    tmp.add(from.getElement().id);
                    res.piecesRevealed.add(tmp);
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
                ArrayList<Integer> tmp = new ArrayList<>();
                tmp.add(toID);
                tmp.add(to.getElement().id);
                res.piecesRevealed.add(tmp);
                tmp = new ArrayList<>();
                tmp.add(fromID);
                tmp.add(from.getElement().id);
                res.piecesRevealed.add(tmp);
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

                        tmp = new ArrayList<>();
                        tmp.add(fromID);
                        tmp.add(toID);
                        res.piecesMoved.add(tmp);
                        break;
                    }
                    case LOSS:
                    {
                        res.piecesDestroyed.add(fromID);
                        break;
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

    private synchronized ArrayList<Integer> getKingStatus(boolean isFirstPlayer)
    {
        ArrayList<Integer> ret = new ArrayList<>();
        for (Element i: Element.values())
        {
            if (i == Element.BLANK || i == Element.ERROR)
                continue;
            if (board.getKing(isFirstPlayer).hasElement(i))
                ret.add(i.id);
        }
        return ret;
    }

    private synchronized TurnResult makeMove(boolean isFirstPlayer, int fromID, int toID)
    {
        TurnResult res = new TurnResult();
        if (board.movePiece(fromID, toID)) {
            if (board.fieldType(toID) == FieldType.THRONE && !board.fields.get(toID).getPiece().king) {
                if (isFirstPlayer)
                    state = WaitingFor.FIRST_ELEMENT_CHOICE;
                else
                    state = WaitingFor.SECOND_ELEMENT_CHOICE;
                promptTarget = toID;
                res.recolor = true;
            }
            ArrayList<Integer> tmp = new ArrayList<>();
            tmp.add(fromID);
            tmp.add(toID);
            res.piecesMoved.add(tmp);
            flipTurn();
            res.status = true;
            return res;
        }
        else
        {
            return null;
        }
    }

    public synchronized TurnResult makeTurn(boolean isFirstPlayer, int fromID, int toID) {
        TurnResult res = new TurnResult();
        if (isFirstPlayer && state != WaitingFor.FIRST_TURN || !isFirstPlayer && state != WaitingFor.SECOND_TURN) {
            res.errorMessage = getErrorMessage("NOT_YOUR_TURN");
            res.status = false;
            return res;
        }

        if (!board.doesOwn(isFirstPlayer, fromID))  {
            res.errorMessage = getErrorMessage("DO_NOT_OWN");
            res.status = false;
            return res;
        }

        res = makeMove(isFirstPlayer, fromID, toID);

        if (res == null)
            res = makeBattle(isFirstPlayer, fromID, toID);

        checkAndSetVictory();
        return res;
    }

    public synchronized TurnResult answerPrompt(boolean isFirstPlayer, Element element) {
        TurnResult result = new TurnResult();
        if (isFirstPlayer && state != WaitingFor.FIRST_ELEMENT_CHOICE ||
                !isFirstPlayer && state != WaitingFor.SECOND_ELEMENT_CHOICE) {
            result.errorMessage = getErrorMessage("NOT_YOUR_TURN");
            result.status = false;
            return result;
        }

        if (!board.doesOwn(isFirstPlayer, promptTarget)){
            result.errorMessage = getErrorMessage("DO_NOT_OWN");
            result.status = false;
            return result;
        }

        Boolean success = board.changeElement(promptTarget, element);
        if (!success)
        {
            result.status = false;
            result.errorMessage = getErrorMessage("WRONG_ELEMENT");
            return result;
        }

        if (state == WaitingFor.FIRST_ELEMENT_CHOICE) {
            state = WaitingFor.SECOND_TURN;
        } else if (state == WaitingFor.SECOND_ELEMENT_CHOICE)
            state = WaitingFor.FIRST_TURN;

        result.status = true;
        return result;
    }


    public synchronized TurnResult concede(boolean isFirstPlayer) {
       return null; //TODO: implement
    }

    public synchronized boolean isWaitingForPrompt(boolean isFirstPlayer){
        if (isFirstPlayer)
            return state == WaitingFor.FIRST_ELEMENT_CHOICE;
        else
            return state == WaitingFor.SECOND_ELEMENT_CHOICE;
    }

    public synchronized TurnResult changeKingElement(boolean isFirstPlayer, Element element) {
        TurnResult result = new TurnResult();
        if (isFirstPlayer && state != WaitingFor.FIRST_TURN || !isFirstPlayer && state != WaitingFor.SECOND_TURN) {
            result.errorMessage = getErrorMessage("NOT_YOUR_TURN");
            result.status = false;
            return result;
        }

        Boolean success = board.changeKingElement(isFirstPlayer, element);
        if (!success)
        {
            result.errorMessage = getErrorMessage("WRONG_ELEMENT");
            result.status = false;
        }
        result.status = true;
        return result;
    }

    private void checkAndSetVictory() {
        if (getKingStatus(false).size() < 1)
            state = WaitingFor.FIRST_PLAYER_WON;
        else if (getKingStatus(true).size() < 1)
            state = WaitingFor.SECOND_PLAYER_WON;
    }

    public boolean isGameFinished() {
        return (state == WaitingFor.FIRST_PLAYER_WON || state == WaitingFor.SECOND_PLAYER_WON);
    }

    public boolean getWinner() {
        return state == WaitingFor.FIRST_PLAYER_WON;
    }


    public String getErrorMessage(String id) {
        String response = messages.get(id);
        if (response == null) {
            // the "totally erroneous and obnoxious output so we would see there is a problem with our resource file"
            return "I HEARD YOU LIKE ERRORS MESSAGES SO I PUT AN ERROR MESSAGE IN YOUR ERROR MESSAGE";
        }
        return response;
    }
}
