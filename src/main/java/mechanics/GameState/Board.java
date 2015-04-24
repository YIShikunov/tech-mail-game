package mechanics.GameState;

import ResourceLoader.GSResources;
import ResourceLoader.ResourcesService;

import java.util.ArrayList;
import java.util.HashMap;

public class Board {
    HashMap<Integer, Field> fields;

    public Board() {
        GSResources boardSettings = ResourcesService.getInstance().getResources("board.xml");
        int fieldNumber = Integer.valueOf(boardSettings.getContentByName("field_count").get(0).getSetting("count"));
        ArrayList<GSResources> edges = boardSettings.getContentByName("edge");
        HashMap<Integer, ArrayList<Integer>> adjacencyMap = new HashMap<>();
        for (int i = 1; i <= fieldNumber; i++) {
            adjacencyMap.put(fieldNumber, new ArrayList<Integer>());
        }
        for (GSResources edge : edges) {
            Integer a = Integer.valueOf(edge.getSetting("a"));
            Integer b = Integer.valueOf(edge.getSetting("b"));
            adjacencyMap.get(a).add(b);
            adjacencyMap.get(b).add(a);
        }
        // TODO: WIP
    }

    public ArrayList<Piece> putPieces() {
        // Put all pieces in their initial positions, create a list for GameState
        return new ArrayList<Piece>();
    }


}
