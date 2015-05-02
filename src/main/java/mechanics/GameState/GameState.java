package mechanics.GameState;

import java.util.ArrayList;

public class GameState {

    enum State {
        PLACEMENT,
        WAITING_FIRST,
        WAITING_SECOND,
        FIRST_CHOOSE_ELEMENT,
        SECOND_CHOOSE_ELEMENT,
        END_GAME_SCREEN;

        /*public int firstPlayerPlaced;
        public int secondPlayerPlaced;

        State() {
            this.firstPlayerPlaced = 0;
            this.secondPlayerPlaced = 0;
        }

        public Element elementToPlace(boolean firstPlayer) {
            int piecesPlaced;
            if (firstPlayer) {
                piecesPlaced = this.firstPlayerPlaced;
            } else {
                piecesPlaced = this.secondPlayerPlaced;
            }
            switch(piecesPlaced / (int)3) {
                case 0: return Element.FIRE;
                case 1: return Element.METAL;
                case 2: return Element.WOOD;
                case 3: return Element.EARTH;
                case 4: return Element.WATER;
            }
            return Element.ERROR;
        }*/

    }

    Board board;
    ArrayList<Piece> pieces;
    State state;

    public GameState() {
        board = new Board();
        pieces = board.arrangePieces();
        state = State.PLACEMENT;
    }

    public void placeNextPiece(boolean firstPlayer, int fieldID) {

    }

    public void movePiece(boolean firstPlayer, int fromFieldID, int toFIeldID) {

    }

    public void chooseElement(boolean firstPlayer, Element element) {

    }

    /* getters and stuff to represent field */
}
