package test.worldTest;

import org.junit.jupiter.api.Test;

import test.testHelpers.BetterHelper;
import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.buildings.Building;
import unsw.loopmania.cards.*;

public class ConvertCardToBuildingTest {
    
    // zombie pit can be placed on non path tile (2, 2)
    @Test
    public void testReplace(){
        BetterHelper h = new BetterHelper();
        LoopManiaWorld world = h.testWorld;

        world.loadCard("ZombiePitCard");

        Building building = world.convertCardToBuildingByCoordinates(0, 0, 2, 2);
        assert(building.getID().equals("ZombiePitBuilding"));

    }

    // There are no cards in the world to convert to building
    @Test
    public void testCardNull(){
        BetterHelper h = new BetterHelper();
        LoopManiaWorld world = h.testWorld;

        Building building = world.convertCardToBuildingByCoordinates(0, 0, 2, 2);
        assert(building == null);

    }

    // zombie pit cannot be placed on a path tile (2, 1)
    @Test void testInvalidLocation(){
        BetterHelper h = new BetterHelper();
        LoopManiaWorld world = h.testWorld;

        world.loadCard("ZombiePitCard");

        Building building = world.convertCardToBuildingByCoordinates(0, 0, 2, 1);
        assert(building == null);
    }


}
