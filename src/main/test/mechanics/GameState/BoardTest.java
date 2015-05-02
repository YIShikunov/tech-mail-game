package mechanics.GameState;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class BoardTest {

    Board board;
    Board fakeBoard;

    private Piece testPiece() {
        return new Piece(null, Element.BLANK, true);
    }

    @Before
    public void setUp()
    {
        this.board = new Board();

        ///////////////////////////
        ////     ////////      ////
        ////  1            2   ////
        ////     ////////      ////
        ////// ///////////// //////  Small board for testing purposes.
        ////     ////////      ////
        ////  3            4    ////
        ////     ////////      ////
        ///////////////////////////
        this.fakeBoard = new Board(true);

        ArrayList<Integer> field1 = new ArrayList<>();
        ArrayList<Integer> field2 = new ArrayList<>();
        ArrayList<Integer> field3 = new ArrayList<>();
        ArrayList<Integer> field4 = new ArrayList<>();

        field1.add(2);
        field1.add(3);
        field2.add(1);
        field2.add(4);
        field3.add(1);
        field3.add(4);
        field4.add(3);
        field4.add(2);

        fakeBoard.fields.put(1, new Field(1, FieldType.NEUTRAL, field1));
        fakeBoard.fields.put(2, new Field(2, FieldType.NEUTRAL, field2));
        fakeBoard.fields.put(3, new Field(3, FieldType.NEUTRAL, field3));
        fakeBoard.fields.put(4, new Field(4, FieldType.NEUTRAL, field4));
    }

    @After
    public void tearDown()
    {
        this.board = null;
        this.fakeBoard = null;
    }

    @Test
    public void testCreation()
    {
        Assert.assertEquals(44, board.fields.size());
        Assert.assertTrue(board.fields.keySet().contains(1));
        Assert.assertTrue(board.fields.keySet().contains(44));
        Assert.assertFalse(board.fields.keySet().contains(45));
        Assert.assertFalse(board.fields.keySet().contains(0));
        Assert.assertNull(board.fields.get(1).getPiece());
        Assert.assertNull(board.fields.get(5).getPiece());
        Assert.assertNull(board.fields.get(44).getPiece());

        Assert.assertEquals(FieldType.THRONE, board.fields.get(1).getType());
        Assert.assertEquals(FieldType.THRONE, board.fields.get(2).getType());
        Assert.assertEquals(FieldType.BASE, board.fields.get(13).getType());
        Assert.assertEquals(FieldType.BASE, board.fields.get(22).getType());
        Assert.assertEquals(FieldType.NEUTRAL, board.fields.get(40).getType());
        Assert.assertEquals(FieldType.NEUTRAL, board.fields.get(33).getType());

        Assert.assertTrue(board.fields.get(1).isAdjacent(5));
        Assert.assertTrue(board.fields.get(7).isAdjacent(15));
        Assert.assertTrue(board.fields.get(40).isAdjacent(39));
        Assert.assertTrue(board.fields.get(41).isAdjacent(27));
        Assert.assertTrue(board.fields.get(18).isAdjacent(32));

        Assert.assertFalse(board.fields.get(1).isAdjacent(2));
        Assert.assertFalse(board.fields.get(4).isAdjacent(5));
        Assert.assertFalse(board.fields.get(38).isAdjacent(41));
        Assert.assertFalse(board.fields.get(20).isAdjacent(41));
        Assert.assertFalse(board.fields.get(30).isAdjacent(2));
    }

    @Test
    public void testPieceArrangement() {
        ArrayList<Piece> pieces = board.arrangePieces();

        Assert.assertEquals(32, pieces.size());

        int firstPlayerOwnershipCount = 0;
        int kingsCount = 0;
        for (Piece piece : pieces) {
            if (piece.king) {
                kingsCount++;
            }
            if (piece.firstPlayerOwner) {
                firstPlayerOwnershipCount++;
            }
        }
        Assert.assertEquals(16, firstPlayerOwnershipCount);
        Assert.assertEquals(2, kingsCount);

        for (Piece piece : pieces) {
            if (piece.getPosition() == null) {
                Assert.fail();
            }
            if (piece.getPosition().type == FieldType.NEUTRAL) {
                Assert.fail();
            }
            if (piece.getPosition().type == FieldType.THRONE && !piece.king) {
                Assert.fail();
            }
            if (piece != piece.getPosition().getPiece()) {
                Assert.fail();
            }
        }
    }

    @Test
    public void testMovement() {
        Piece piece1 = testPiece();
        Piece piece2 = testPiece();

        fakeBoard.putPiece(piece1, 1);
        fakeBoard.putPiece(piece2, 4);

        Assert.assertEquals(piece1, fakeBoard.fields.get(1).getPiece());
        Assert.assertNull(fakeBoard.fields.get(2).getPiece());
        Assert.assertNull(fakeBoard.fields.get(3).getPiece());
        Assert.assertEquals(piece2, fakeBoard.fields.get(4).getPiece());

        Assert.assertTrue(fakeBoard.movePiece(1, 2));
        Assert.assertFalse(fakeBoard.movePiece(2, 3));
        Assert.assertFalse(fakeBoard.movePiece(2, 4));
        Assert.assertTrue(fakeBoard.movePiece(4, 3));
        Assert.assertTrue(fakeBoard.movePiece(2, 4));
        Assert.assertFalse(fakeBoard.movePiece(3, 4));

        Assert.assertNull(fakeBoard.fields.get(1).getPiece());
        Assert.assertNull(fakeBoard.fields.get(2).getPiece());
        Assert.assertEquals(piece2, fakeBoard.fields.get(3).getPiece());
        Assert.assertEquals(piece1, fakeBoard.fields.get(4).getPiece());
    }
}
