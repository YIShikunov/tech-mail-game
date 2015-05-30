package mechanics;

import mechanics.GameState.Element;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

public class GameControllerImplTest {

    GameControllerImpl session = new GameControllerImpl();

    @Before
    public void setUp() {

    }

    @After
    public void tearDown() {

    }

    private void legalPlacement() {
        HashMap<Integer, Element> firstPlayerSetUp = new HashMap<>();
        firstPlayerSetUp.put(3, Element.FIRE);
        firstPlayerSetUp.put(4, Element.FIRE);
        firstPlayerSetUp.put(5, Element.WATER);
        firstPlayerSetUp.put(6, Element.WATER);
        firstPlayerSetUp.put(7, Element.WATER);
        firstPlayerSetUp.put(8, Element.WOOD);
        firstPlayerSetUp.put(9, Element.WOOD);
        firstPlayerSetUp.put(10, Element.WOOD);
        firstPlayerSetUp.put(11, Element.EARTH);
        firstPlayerSetUp.put(12, Element.EARTH);
        firstPlayerSetUp.put(13, Element.EARTH);
        firstPlayerSetUp.put(14, Element.METAL);
        firstPlayerSetUp.put(15, Element.METAL);
        firstPlayerSetUp.put(16, Element.METAL);
        firstPlayerSetUp.put(17, Element.FIRE);

        HashMap<Integer, Element> secondPlayerSetUp = new HashMap<>();
        secondPlayerSetUp.put(32, Element.FIRE);
        secondPlayerSetUp.put(31, Element.FIRE);
        secondPlayerSetUp.put(30, Element.WATER);
        secondPlayerSetUp.put(29, Element.WATER);
        secondPlayerSetUp.put(28, Element.WATER);
        secondPlayerSetUp.put(27, Element.WOOD);
        secondPlayerSetUp.put(26, Element.WOOD);
        secondPlayerSetUp.put(25, Element.WOOD);
        secondPlayerSetUp.put(24, Element.EARTH);
        secondPlayerSetUp.put(23, Element.EARTH);
        secondPlayerSetUp.put(22, Element.EARTH);
        secondPlayerSetUp.put(21, Element.METAL);
        secondPlayerSetUp.put(20, Element.METAL);
        secondPlayerSetUp.put(19, Element.METAL);
        secondPlayerSetUp.put(18, Element.FIRE);

        session.placePieces(true, firstPlayerSetUp);
        session.placePieces(false, secondPlayerSetUp);
    }

    @Test
    public void mockUpGame() {
        session.init();
        Assert.assertEquals(session.state, GameControllerImpl.WaitingFor.PLACEMENT);
        legalPlacement();

        Assert.assertEquals(session.state, GameControllerImpl.WaitingFor.FIRST_TURN);
        Assert.assertTrue(session.makeTurn(true, 14, 37).status);
        Assert.assertEquals(session.state, GameControllerImpl.WaitingFor.SECOND_TURN);
        Assert.assertTrue(session.makeTurn(false, 28, 38).status);
        Assert.assertTrue(session.makeTurn(true, 12, 42).status);
        Assert.assertTrue(session.makeTurn(false, 38, 40).status);
        Assert.assertTrue(session.makeTurn(true, 42, 40).status);
        Assert.assertEquals(session.state, GameControllerImpl.WaitingFor.SECOND_TURN);

        Assert.assertEquals(session.board.fields.get(40).getPiece().visible, true);
        Assert.assertEquals(session.board.fields.get(40).getPiece().firstPlayerOwner, true);

        Assert.assertTrue(session.makeTurn(false, 21, 28).status);
        Assert.assertTrue(session.makeTurn(true, 40, 42).status);
        Assert.assertTrue(session.makeTurn(false, 28, 38).status);
        Assert.assertTrue(session.makeTurn(true, 42, 12).status);
        Assert.assertTrue(session.makeTurn(false, 38, 36).status);
        Assert.assertTrue(session.makeTurn(true, 37, 35).status);
        Assert.assertTrue(session.makeTurn(false, 2, 21).status);
        Assert.assertTrue(session.makeTurn(true, 6, 14).status);
        Assert.assertTrue(session.makeTurn(false, 21, 28).status);
        Assert.assertTrue(session.makeTurn(true, 13,37).status);
        Assert.assertTrue(session.makeTurn(false, 28, 38).status);
        Assert.assertTrue(session.makeTurn(true, 12, 13).status);
        Assert.assertTrue(session.makeTurn(false, 38, 40).status);
        Assert.assertTrue(session.makeTurn(true, 13, 6).status);
        Assert.assertTrue(session.makeTurn(false, 40, 42).status);
        Assert.assertTrue(session.makeTurn(true, 5, 12).status);
        Assert.assertTrue(session.makeTurn(false, 18, 2).status);

        Assert.assertEquals(session.state, GameControllerImpl.WaitingFor.SECOND_ELEMENT_CHOICE);

        Assert.assertFalse(session.makeTurn(true, 42, 11).status);
        Assert.assertTrue(session.isWaitingForPrompt(false));

        Assert.assertEquals(session.board.fields.get(2).getPiece().getElement(), Element.FIRE);
        Assert.assertTrue(session.answerPrompt(false, Element.WOOD).status);
        Assert.assertEquals(session.board.fields.get(2).getPiece().getElement(), Element.WOOD);

        Assert.assertEquals(session.state, GameControllerImpl.WaitingFor.FIRST_TURN);
        Assert.assertTrue(session.makeTurn(true, 1, 5).status);
        Assert.assertTrue(session.changeKingElement(false, Element.FIRE).status);
        Assert.assertTrue(session.makeTurn(false, 42, 12).status);
        Assert.assertFalse(session.board.fields.get(42).getPiece().hasElement(Element.FIRE));
        Assert.assertTrue(session.makeTurn(true, 11, 42).status);
    }
}
