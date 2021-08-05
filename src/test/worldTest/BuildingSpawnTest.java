package test.worldTest;

import org.junit.jupiter.api.Test;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import test.testHelpers.BetterHelper;
import unsw.loopmania.LoopManiaWorld;

public class BuildingSpawnTest {
    
    // Test that you cant place a zombie building ontop of itself
    @Test
    public void testBuildingPlaceOverlap(){
        BetterHelper h = new BetterHelper();
        LoopManiaWorld world = h.testWorld;
        
        world.loadCard("ZombiePitCard");
        assertTrue(world.canPlaceBuilding("ZombiePitCard", 0, 0));
        assertFalse(world.canPlaceBuilding("ZombiePitCard", 1, 1));
        assertTrue(world.canPlaceBuilding("ZombiePitCard", 0, 1));
        assertTrue(world.canPlaceBuilding("ZombiePitCard", 1, 0));
        assertFalse(world.canPlaceBuilding("ZombiePitCard", 5, 5));
    }

    @Test
    public void testBuildingPlaceExisting(){
        BetterHelper h = new BetterHelper();
        LoopManiaWorld world = h.testWorld;
        world.addBuilding(h.testHeroCastle);
        
        world.loadCard("ZombiePitCard");
        assertFalse(world.canPlaceBuilding("ZombiePitCard", 1, 1));
        assertTrue(world.canPlaceBuilding("ZombiePitCard", 0, 1));
        assertTrue(world.canPlaceBuilding("ZombiePitCard", 1, 0));
        assertFalse(world.canPlaceBuilding("ZombiePitCard", 5, 5));

        
    }

}
