package test.itemsTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import test.testHelpers.*;
import unsw.loopmania.items.*;

public class RareItemRNGTest {

    /**
     * Test that indeed, there is only one of "The One Ring"'s
     */
    @Test
    public void OnlyOneOfTheOneRingTest() {

        BetterHelper h = new BetterHelper();
        h.testWorld.addPossibleRareItem(new TheOneRing());
        // spawn out the one ring
        assertTrue(h.testWorld.generateRareItem(1) instanceof TheOneRing);
        // no more one rings
        assertTrue(h.testWorld.generateRareItem(1)==null);
   }

   /**
    * Test a uniform distribution of given rare items.
    * So that, in future, if more than one rare item exist we can ascertain there is an equal chance of receiving all of them
    */
    @Test
    public void EqualChanceOfRareItemsTest() {
        int item1Count = 0;
        int item2Count = 0;
        int item3Count = 0;
        int item4Count = 0;
        for (int attempts = 0; attempts < 1000; attempts++) {
            BetterHelper h = new BetterHelper();
            h.testWorld.addPossibleRareItem(new Sword(null, null));
            h.testWorld.addPossibleRareItem(new Staff(null, null));
            h.testWorld.addPossibleRareItem(new Stake(null, null));
            h.testWorld.addPossibleRareItem(new TheOneRing(null, null));
            // spawn a "rare item"
            Item item = h.testWorld.generateRareItem(1);
            switch(item.getID()) {
            case ("Sword"):
                item1Count++;
                break;
            case ("Staff"):
                item2Count++;
                break;
            case ("Stake"):
                item3Count++;
                break;
            case ("TheOneRing"):
                item4Count++;
                break;
            }
        }
        // assert ~250 counts each
        assertEquals(item1Count, 250, 50);
        assertEquals(item2Count, 250, 50);
        assertEquals(item3Count, 250, 50);
        assertEquals(item4Count, 250, 50);
   }

   @Test
   public void testNullRareItem(){
       BetterHelper h = new BetterHelper();
       Item item = h.testWorld.generateRareItem(100000);
       assert(item == null);
   }

}
