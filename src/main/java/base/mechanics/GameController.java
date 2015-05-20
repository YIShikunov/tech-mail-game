package base.mechanics;

import mechanics.GameState.Element;
import mechanics.TurnResult;

import java.util.HashMap;

public interface GameController {
    public void init();

    public boolean placePieces(boolean isFirstPlayer, HashMap<Integer, Element> placement);
    public TurnResult makeTurn(boolean isFirstPlayer, int fromID, int toID);
    public boolean answerPrompt(boolean isFirstPlayer, Element element);
    public boolean concede(boolean isFirstPlayer);
    public boolean changeKingElement(boolean isFirstPlayer, Element element);

    public boolean isWaitingForPrompt(boolean isFirstPlayer);
    // public Object allGameData(); - not sure if we need this.
    // TODO pack all game data as seen by a player into a JSON
    //public Object subjectiveGameData(boolean isFirstPlayer); -- Not needed yet.

    public String popErrorMessage();
}
