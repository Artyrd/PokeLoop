package test.cardTest;

import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;

import javafx.beans.property.SimpleIntegerProperty;
import test.testHelpers.BasicHelper;
import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.buildings.BarracksBuilding;
import unsw.loopmania.buildings.CampfireBuilding;
import unsw.loopmania.buildings.TowerBuilding;
import unsw.loopmania.buildings.TrapBuilding;
import unsw.loopmania.buildings.VampireCastleBuilding;
import unsw.loopmania.buildings.VillageBuilding;
import unsw.loopmania.buildings.ZombiePitBuilding;
import unsw.loopmania.cards.BarracksCard;
import unsw.loopmania.cards.CampfireCard;
import unsw.loopmania.cards.Card;
import unsw.loopmania.cards.DragonSpawnerCard;
import unsw.loopmania.cards.ElanSpawnerCard;
import unsw.loopmania.cards.TowerCard;
import unsw.loopmania.cards.TrapCard;
import unsw.loopmania.cards.VampireCastleCard;
import unsw.loopmania.cards.VillageCard;
import unsw.loopmania.cards.ZombiePitCard;

public class CardTest {
    
    /**
     * Test that cards are associated with their respective buildings
     */
    @Test
    void testBuiltBuildings() {
        ZombiePitCard zp = new ZombiePitCard(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        assertTrue(zp.getBuiltBuilding(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0)) instanceof ZombiePitBuilding);

        VampireCastleCard vc = new VampireCastleCard(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        assertTrue(vc.getBuiltBuilding(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0)) instanceof VampireCastleBuilding);

        BarracksCard b = new BarracksCard(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        assertTrue(b.getBuiltBuilding(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0)) instanceof BarracksBuilding);

        TowerCard tower = new TowerCard(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        assertTrue(tower.getBuiltBuilding(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0)) instanceof TowerBuilding);

        VillageCard v = new VillageCard(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        assertTrue(v.getBuiltBuilding(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0)) instanceof VillageBuilding);

        TrapCard trap = new TrapCard(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        assertTrue(trap.getBuiltBuilding(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0)) instanceof TrapBuilding);
    }

    /**
     * Test the zombie pit card qualities
     */
    @Test
    void testZombiePitCard() {

        BasicHelper h = new BasicHelper();
        LoopManiaWorld testWorld = new LoopManiaWorld(12, 12, h.orderedPath);
        testWorld.loadCard("ZombiePitCard");
        
        for (Card c : testWorld.getCards()) {
            assertTrue(c instanceof ZombiePitCard);
            assertTrue(c.getBuilding() instanceof ZombiePitBuilding);
            assertTrue(c.getName().equals("Gengar's Zombie Pit Card"));
            assertTrue(c.getValue() == 15);

            assertTrue(c.canPlaceBuilding(0, 0, testWorld));
            assertTrue(!c.canPlaceBuilding(1, 1, testWorld));
        }

    }

    /**
     * Test the vampire castle card qualities
     */
    @Test
    void testVampireCastleCard() {

        BasicHelper h = new BasicHelper();
        LoopManiaWorld testWorld = new LoopManiaWorld(12, 12, h.orderedPath);
        testWorld.loadCard("VampireCastleCard");
        
        for (Card c : testWorld.getCards()) {
            assertTrue(c instanceof VampireCastleCard);
            assertTrue(c.getBuilding() instanceof VampireCastleBuilding);
            assertTrue(c.getName().equals("Darkrai's Vampire Castle Card"));
            assertTrue(c.getValue() == 20);

            assertTrue(c.canPlaceBuilding(0, 0, testWorld));
            assertTrue(!c.canPlaceBuilding(1, 1, testWorld));
        }

    }

    /**
     * Test the barracks card qualities
     */
    @Test
    void testBarracksCard() {

        BasicHelper h = new BasicHelper();
        LoopManiaWorld testWorld = new LoopManiaWorld(12, 12, h.orderedPath);
        testWorld.loadCard("BarracksCard");
        
        for (Card c : testWorld.getCards()) {
            assertTrue(c instanceof BarracksCard);
            assertTrue(c.getBuilding() instanceof BarracksBuilding);
            assertTrue(c.getName().equals("Barracks Card"));
            assertTrue(c.getValue() == 10);
            
            assertTrue(c.canPlaceBuilding(1, 1, testWorld));
            assertTrue(!c.canPlaceBuilding(0, 0, testWorld));
        }

    }

    /**
     * Test the tower card qualities
     */
    @Test
    void testTowerCard() {

        BasicHelper h = new BasicHelper();
        LoopManiaWorld testWorld = new LoopManiaWorld(12, 12, h.orderedPath);
        testWorld.loadCard("TowerCard");
        
        for (Card c : testWorld.getCards()) {
            assertTrue(c instanceof TowerCard);
            assertTrue(c.getBuilding() instanceof TowerBuilding);
            assertTrue(c.getName().equals("Regirock's Tower Card"));
            assertTrue(c.getValue() == 10);

            assertTrue(c.canPlaceBuilding(0, 0, testWorld));
            assertTrue(!c.canPlaceBuilding(1, 1, testWorld));
        }

    }

    /**
     * Test the test card qualities
     */
    @Test
    void testTrapCard() {

        BasicHelper h = new BasicHelper();
        LoopManiaWorld testWorld = new LoopManiaWorld(12, 12, h.orderedPath);
        testWorld.loadCard("TrapCard");
        
        for (Card c : testWorld.getCards()) {
            assertTrue(c instanceof TrapCard);
            assertTrue(c.getBuilding() instanceof TrapBuilding);
            assertTrue(c.getName().equals("Electric Trap Card"));
            assertTrue(c.getValue() == 5);

            assertTrue(c.canPlaceBuilding(1, 1, testWorld));
            assertTrue(!c.canPlaceBuilding(0, 0, testWorld));
        }

    }

    /**
     * Test the village card qualities
     */
    @Test
    void testVillageCard() {

        BasicHelper h = new BasicHelper();
        LoopManiaWorld testWorld = new LoopManiaWorld(12, 12, h.orderedPath);
        testWorld.loadCard("VillageCard");
        
        for (Card c : testWorld.getCards()) {
            assertTrue(c instanceof VillageCard);
            assertTrue(c.getBuilding() instanceof VillageBuilding);
            assertTrue(c.getName().equals("Twinleaf Town Card"));
            assertTrue(c.getValue() == 10);

            assertTrue(c.canPlaceBuilding(1, 1, testWorld));
            assertTrue(!c.canPlaceBuilding(0, 0, testWorld));
        }

    }

    /**
     * Test the campfire card qualities
     */
    @Test
    void testCampfireCard() {

        BasicHelper h = new BasicHelper();
        LoopManiaWorld testWorld = new LoopManiaWorld(12, 12, h.orderedPath);
        testWorld.loadCard("CampfireCard");
        
        for (Card c : testWorld.getCards()) {
            assertTrue(c instanceof CampfireCard);
            assertTrue(c.getBuilding() instanceof CampfireBuilding);
            assertTrue(c.getName().equals("Solrock's Campfire Card"));
            assertTrue(c.getValue() == 5);

            assertTrue(c.canPlaceBuilding(0, 0, testWorld));
            assertTrue(!c.canPlaceBuilding(1, 1, testWorld));
        }

    }


    @Test
    void testElanSpawnerCard() {

        BasicHelper h = new BasicHelper();
        LoopManiaWorld testWorld = new LoopManiaWorld(12, 12, h.orderedPath);
        testWorld.loadCard("ElanSpawnerCard");
        
        for (Card c : testWorld.getCards()) {
            assertTrue(c instanceof ElanSpawnerCard);

            assertTrue(c.canPlaceBuilding(1, 1, testWorld));
        }

    }

    @Test
    void testDragonSpawnerCard() {

        BasicHelper h = new BasicHelper();
        LoopManiaWorld testWorld = new LoopManiaWorld(12, 12, h.orderedPath);
        testWorld.loadCard("DragonSpawnerCard");
        
        for (Card c : testWorld.getCards()) {
            assertTrue(c instanceof DragonSpawnerCard);

            assertTrue(c.canPlaceBuilding(1, 1, testWorld));
        }

    }

    @Test
    void testNullCard() {

        BasicHelper h = new BasicHelper();
        LoopManiaWorld testWorld = new LoopManiaWorld(12, 12, h.orderedPath);
        testWorld.loadCard("");
        
        for (Card c : testWorld.getCards()) {
            assertTrue(c == null);
        }

    }



}
