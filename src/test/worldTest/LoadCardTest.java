package test.worldTest;

import org.junit.Test;

import test.testHelpers.BetterHelper;

public class LoadCardTest {
    
    /**
     * Test what happens if we get too many cards
     */
    @Test
    public void testTooManyCards(){
        BetterHelper h = new BetterHelper();

        for(int numCards = 0; numCards < 15; numCards++){
            h.testWorld.loadCard("BarracksCard");
        }
        
        // World has only 12 cards avail to be held at a time
        assert(h.testWorld.getCards().size() == 12);

    }


}
