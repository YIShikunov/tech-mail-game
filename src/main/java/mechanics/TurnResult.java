package mechanics;

import com.sun.org.apache.xpath.internal.operations.Bool;
import javafx.util.Pair;
import mechanics.GameState.Element;
import java.util.ArrayList;

public class TurnResult
{
    public final ArrayList<ArrayList<Integer>> piecesMoved = new ArrayList<>();
    public final ArrayList<ArrayList<Integer>> piecesRevealed = new ArrayList<>();
    public final ArrayList<Integer> piecesDestroyed = new ArrayList<>();
    public final ArrayList<Integer> piecesHidden = new ArrayList<>();
    public ArrayList<Integer> king1Status = null;
    public ArrayList<Integer> king2Status = null;
    public Element king1Element;
    public Element king2Element;
    public boolean status;
    public boolean recolor;
    public String errorMessage;
    public String battleResult;

    public TurnResult()
    {

    }
}
