package mechanics.GameState;

import java.util.ArrayList;

public class Field {
    final int id;
    final ArrayList<Integer> adjacent;
    final FieldType type;
    Piece piece;

    public Field(int id, FieldType type, ArrayList<Integer> adjacent) {
        this.id = id;
        this.adjacent = adjacent;
        this.type = type;
    }

    public void putPiece(Piece piece) {
        this.piece = piece;
    }

    public void removePiece() {
        this.piece = null;
    }

    public Piece getPiece() {
        return piece;
    }

    public int getID() {
        return id;
    }

    public ArrayList<Integer> getAdjacent(){
        return adjacent;
    }

    public boolean isAdjacent(Field field) {
        return isAdjacent(field.getID());
    }

    public boolean isAdjacent(int id) {
        return adjacent.contains(id);
    }

}
