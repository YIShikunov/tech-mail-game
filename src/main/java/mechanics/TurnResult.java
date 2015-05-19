package mechanics;

import javafx.util.Pair;
import mechanics.GameState.Element;
import java.util.ArrayList;

public class TurnResult
{
    public final ArrayList<Pair<Integer, Integer>> piecesMoved = new ArrayList<>();
    public final ArrayList<Pair<Integer, Element>> piecesRevealed = new ArrayList<>();
    public final ArrayList<Integer> piecesDestroyed = new ArrayList<>();
    public final ArrayList<Integer> piecesHidden = new ArrayList<>();
    public boolean status;
    public boolean recolor;
    public String errorMessage;
    public String battleResult;

    public TurnResult()
    {

    }
}
