package mechanics;

import com.sun.org.apache.xpath.internal.operations.Bool;
import javafx.util.Pair;
import mechanics.GameState.Element;
import java.util.ArrayList;

public class TurnResult
{
    public final ArrayList<Pair<Integer, Integer>> piecesMoved = new ArrayList<>();
    public final ArrayList<Pair<Integer, Element>> piecesRevealed = new ArrayList<>();
    public final ArrayList<Integer> piecesDestroyed = new ArrayList<>();
    public final ArrayList<Integer> piecesHidden = new ArrayList<>();
    public ArrayList<Integer> king1Status = null;
    public ArrayList<Integer> king2Status = null;
    public boolean status;
    public boolean recolor;
    public String errorMessage;
    public String battleResult;

    public TurnResult()
    {

    }
}
