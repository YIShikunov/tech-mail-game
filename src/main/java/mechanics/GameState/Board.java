package mechanics.GameState;

import ResourceLoader.GSResources;
import ResourceLoader.ResourcesService;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;

public class Board {

    public HashMap<Integer, Field> fields;
    protected GSResources boardSettings;

    public Board() {
        // TODO YIShikunov: most of this logic is supposed to be in the resource class.
        boardSettings = ResourcesService.getInstance().getResources("board.xml");
        int fieldNumber = Integer.valueOf(boardSettings.getContentByName("field_count").get(0).getSetting("count"));

        ArrayList<GSResources> edges = boardSettings.getContentByName("edges").get(0).getContents();
        HashMap<Integer, ArrayList<Integer>> adjacencyMap = new HashMap<>();
        for (int i = 1; i <= fieldNumber; i++) {
            adjacencyMap.put(i, new ArrayList<Integer>());
        }
        for (GSResources edge : edges) {
            Integer a = Integer.valueOf(edge.getSetting("a"));
            Integer b = Integer.valueOf(edge.getSetting("b"));
            adjacencyMap.get(a).add(b);
            adjacencyMap.get(b).add(a);
        }

        ArrayList<GSResources> types = boardSettings.getContentByName("types").get(0).getContents();
        HashMap<String, Pair<Integer, Integer>> typeMap = new HashMap<>();
        for (GSResources type : types) {
            typeMap.put(type.getSetting("value"),
                        new Pair<>(Integer.valueOf(type.getSetting("from")),
                        Integer.valueOf(type.getSetting("to"))));
        }

        this.fields = new HashMap<>();
        for (int i=1; i <= fieldNumber; i++) {
            FieldType newType = FieldType.ERROR;
            for (String type : typeMap.keySet() ) {
                if (i >= typeMap.get(type).getKey() && i <= typeMap.get(type).getValue()) {
                    newType = FieldType.fromString(type);
                    break;
                }
            }
            Field n = new Field(i, newType, adjacencyMap.get(i));
            this.fields.put(i, n);
        }
    }

    public ArrayList<Piece> arrangePieces() { // TODO: this is a mess, clean it up
        ArrayList<Piece> result = new ArrayList<>();

        ArrayList<Integer> ownedByFirst = new ArrayList<>();
        //ArrayList<Integer> ownedBySecond = new ArrayList<>();
        for (GSResources fieldID : boardSettings.getContentByName("ownership").get(0).getContentByName("first")) {
            ownedByFirst.add(Integer.valueOf(fieldID.getSetting("value")));
        }
        /*for (GSResources fieldID : boardSettings.getContentByName("ownership").get(0).getContentByName("second")) {
            ownedBySecond.add(Integer.valueOf(fieldID.getSetting("value")));
        }*/ //TODO use to scout for mistakes

        for (Field field : this.fields.values()) {
            if (field.type == FieldType.THRONE) {
                Boolean ownership = ownedByFirst.contains(field.getID());
                Piece piece = new Piece(ownership);
                field.putPiece(piece);
                piece.setPosition(field);
                result.add(piece);
            } else if (field.type == FieldType.BASE) {
                Boolean ownership = ownedByFirst.contains(field.getID());
                Piece piece = new Piece(Element.BLANK, ownership);
                field.putPiece(piece);
                piece.setPosition(field);
                result.add(piece);
            }
        }
        return result;
    }

    public Boolean movePiece(Piece piece, Field to) {
        if (piece.getPosition() == null) {
            return false;
        }
        if (!piece.getPosition().isAdjacent(to)) {
            return false;
        }
        if (to.getPiece() != null) {
            return false;
        }
        putPiece(takePiece(piece),to.getID());
        return true;
    }
    public Boolean movePiece(Piece piece, int to) {
        return movePiece(piece, fields.get(to));
    }
    public Boolean movePiece(Field from, Field to) {
        if (from.getPiece() == null) {
            return false;
        }
        return movePiece(from.getPiece(), to);
    }
    public Boolean movePiece(int from, int to) {
        return movePiece(fields.get(from), fields.get(to));
    }

    protected void putPiece(Piece piece, int field) {
        Field to = fields.get(field);
        to.putPiece(piece);
        piece.setPosition(to);
    }
    protected Piece takePiece(Piece piece) {
        if (piece.getPosition() != null)
            piece.getPosition().removePiece();
        piece.setPosition(null);
        return piece;
    }
    protected Piece takePiece(Field field) {
        return takePiece(field.getPiece());
    }

    public boolean placeElements(HashMap<Integer, Element> placement) {
        for (Integer fieldID : placement.keySet()) {
            Field field = fields.get(fieldID);
            if (field.getPiece() == null || field.getPiece().getElement() != Element.BLANK) {
                return false;
            }
            field.getPiece().setElement(placement.get(fieldID));
        }
        return true;
    }

    public Outcome attackPiece(Piece attacker, Piece defender) {
        if (attacker.getPosition() == null || defender.getPosition() == null) {
            return Outcome.ERROR;
        }
        if (!attacker.getPosition().isAdjacent(defender.getPosition())) {
            return Outcome.ERROR;
        }
        if (attacker.firstPlayerOwner == defender.firstPlayerOwner) {
            return Outcome.ERROR;
        }
        if (!attacker.getElement().valid() || !defender.getElement().valid()) {
            return Outcome.ERROR;
        }

        attacker.reveal();
        defender.reveal();

        Outcome outcome = Element.battle(attacker.getElement(), defender.getElement());
        if (outcome == Outcome.WIN) {
            Field position = defender.getPosition();
            destroy(defender);
            if (defender.getPosition() == null)
                movePiece(attacker, position);
        } else if (outcome == Outcome.LOSS) {
            destroy(attacker);
        } else if (outcome == Outcome.DRAW) {
            //do nothing
        } else if (outcome == Outcome.DESTRUCTION) {
            destroy(attacker);
            destroy(defender);
        }

        return outcome;
    }

    public Outcome attackPiece(int attackerFieldID, int defenderFieldID) {
        Piece attacker = fields.get(attackerFieldID).getPiece();
        Piece defender = fields.get(defenderFieldID).getPiece();
        if (attacker == null || defender == null) {
            return Outcome.ERROR;
        }
        return attackPiece(attacker, defender);
    }

    private void destroy(Piece piece) {
        if (piece.king) {
            piece.destroy(piece.getElement());
        } else {
            takePiece(piece);
        }
    }

    public boolean changeElement(Piece piece, Element element) {
        piece.setElement(element);
        piece.conceal();
        return true;
    }

    public boolean changeElement(int fieldID, Element element) {
        if (fields.get(fieldID).getPiece() == null)
            return false;
        return changeElement(fields.get(fieldID).getPiece(), element);
    }

    public boolean doesOwn(boolean isFirstPlayer, int fieldID) {
        Field field = fields.get(fieldID);
        if (field == null) {
            return false;
        } else if (field.getPiece() == null) {
            return false;
        } else if (field.getPiece().firstPlayerOwner != isFirstPlayer) {
            return false;
        }
        return true;
    }

    public boolean allSet() {
        for (int fieldID : fields.keySet()) {
            Field field = fields.get(fieldID);
            if (field.getPiece() != null && !field.getPiece().king && field.getPiece().getElement() == Element.BLANK) {
                return false;
            }
        }
        return true;
    }

    public Piece getKing(boolean isFirstPlayer) {
        for (Field field : fields.values()) {
            Piece piece = field.getPiece();
            if (piece != null && piece.king && piece.firstPlayerOwner==isFirstPlayer) {
                return piece;
            }
        }
        return null;
    }

    public boolean changeKingElement(boolean isFirstPlayer, Element element) {
        for (Field field : fields.values()) { // TODO: with getKing
            Piece piece = field.getPiece();
            if (piece != null && piece.king && piece.firstPlayerOwner==isFirstPlayer && piece.hasElement(element)) {
                piece.setElement(element);
                return true;
            }
        }
        return false;
    }

    public FieldType fieldType(int id) {
        return fields.get(id).getType();
    }


    @SuppressWarnings("unchecked")
    protected Board(Boolean testing) {
        // Empty board for testing purposes.
        // Only used in UnitTests
        boardSettings = ResourcesService.getInstance().getResources("board.xml");
        this.fields = new HashMap<>();
    }
}
