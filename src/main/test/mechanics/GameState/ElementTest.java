package mechanics.GameState;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ElementTest {
    @Before
    public void setUp() {

    }

    @After
    public void tearDown() {

    }

    @Test
    public void testCombat() {
        Assert.assertEquals(Element.battle(Element.WATER, Element.FIRE), Outcome.WIN);
        Assert.assertEquals(Element.battle(Element.WATER, Element.WATER), Outcome.DESTRUCTION);
        Assert.assertEquals(Element.battle(Element.FIRE, Element.WOOD), Outcome.WIN);
        Assert.assertEquals(Element.battle(Element.FIRE, Element.WATER), Outcome.LOSS);
        Assert.assertEquals(Element.battle(Element.EARTH, Element.METAL), Outcome.DRAW);
        Assert.assertEquals(Element.battle(Element.FIRE, Element.EARTH), Outcome.DRAW);
        Assert.assertEquals(Element.battle(Element.WOOD, Element.METAL), Outcome.LOSS);
        Assert.assertEquals(Element.battle(Element.WOOD, Element.EARTH), Outcome.WIN);
        Assert.assertEquals(Element.battle(Element.WOOD, Element.WOOD), Outcome.DESTRUCTION);
    }
}
