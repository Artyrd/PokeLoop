package test.worldTest;

import org.junit.jupiter.api.Test;

import test.testHelpers.BetterHelper;
import unsw.loopmania.LoopManiaWorld;

public class MovingEntitiesTest {
    
    @Test
    public void testMovingEntities(){
        BetterHelper h = new BetterHelper();
        LoopManiaWorld world = h.testWorld;

        world.addBoss(h.testElanMuske);
        world.addBoss(h.testDoggie);
        world.setCharacter(h.testCharacter);
        
        assert(world.getMovingEntities().size() == 3);

    }

    @Test
    public void testEntities(){
        BetterHelper h = new BetterHelper();
        LoopManiaWorld world = h.testWorld;

        world.addBoss(h.testElanMuske);
        world.addBoss(h.testDoggie);
        world.setCharacter(h.testCharacter);
        world.addUnequippedItem(h.testPotion);
        
        assert(world.getEntities().size() == 3);

    }



}
