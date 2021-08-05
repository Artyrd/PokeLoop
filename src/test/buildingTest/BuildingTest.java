package test.buildingTest;

import org.junit.jupiter.api.Test;

import javafx.beans.property.SimpleIntegerProperty;
import test.testHelpers.BasicHelper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import unsw.loopmania.enemies.Slug;
import unsw.loopmania.enemies.Vampire;
import unsw.loopmania.enemies.Zombie;
import unsw.loopmania.movement.PathPosition;
import unsw.loopmania.Ally;
import unsw.loopmania.Entity;
import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.LoopManiaWorld.GAME_STATE;
import unsw.loopmania.battle.BattleSimulator;
import unsw.loopmania.buildings.BarracksBuilding;
import unsw.loopmania.buildings.CampfireBuilding;
import unsw.loopmania.buildings.HeroCastleBuilding;
import unsw.loopmania.buildings.TowerBattler;
import unsw.loopmania.buildings.TowerBuilding;
import unsw.loopmania.buildings.TrapBuilding;
import unsw.loopmania.buildings.VampireCastleBuilding;
import unsw.loopmania.buildings.VillageBuilding;
import unsw.loopmania.buildings.ZombiePitBuilding;

public class BuildingTest {

    /**
     * Tests if the Hero Building spawns when the character loads into world
     * and displays the show menu when the hero finishes a loop
     */
    @Test
    void testHeroCastle() {
        
        // load the world
        BasicHelper h = new BasicHelper();
        h.LoadBasicLoopWorld(); 
        LoopManiaWorld testWorld = new LoopManiaWorld(12, 12, h.orderedPath);
        testWorld.setCharacter(h.testCharacter);
        HeroCastleBuilding heroCastle = h.testHeroCastle;
        testWorld.addBuilding(heroCastle);

        assertEquals(testWorld.getLoop(), 1);
        // check the hero castle is in the position where the hero started
        assertTrue(heroCastle.getX() == 1);
        assertTrue(heroCastle.getY() == 1);

        // do a loop...
        for (int loop = 0; loop < h.totalLoopLength; loop++) {
            testWorld.runTickMoves();
            testWorld.updateBuildings();
        }
        // check when the hero finishes a loop the shop menu prompt shows up
        assertTrue(testWorld.gameState.equals(GAME_STATE.SHOP));

        // loop should have incremented
        assertEquals(testWorld.getLoop(), 2);

    }

    /**
     * Tests if the Vampire Castle spawns a vampire when the character finishes a loops 5 times
     */
    @Test
    void testVampireCastle() {
        
        // Load the world
        BasicHelper h = new BasicHelper();
        h.LoadBasicLoopWorld();

        LoopManiaWorld testWorld = new LoopManiaWorld(12, 12, h.orderedPath);
        testWorld.addBuilding(h.testHeroCastle);
        testWorld.setCharacter(h.testCharacter);

        // Add a vampire castle building
        VampireCastleBuilding vc = new VampireCastleBuilding(new SimpleIntegerProperty(11), new SimpleIntegerProperty(11));
        testWorld.addBuilding(vc);

        // check the vampire castle is in the correct position
        assertTrue(vc.getX() == 11);
        assertTrue(vc.getY() == 11);

        // check when the hero finishes 5 loops a vampire spawns
        for (int loop = 1; loop < 4; loop++) {
            for (int i = 0; i < h.totalLoopLength; i++) {
                testWorld.runTickMoves();
                testWorld.updateBuildings();
            }
            System.out.println(testWorld.getLoop());
            assertTrue(testWorld.getEnemies().isEmpty());
        }

        for (int i = 0; i < h.totalLoopLength; i++) {
            testWorld.runTickMoves();
            testWorld.updateBuildings();
        }
        assertTrue(!testWorld.getEnemies().isEmpty());
        
        for (Entity e : testWorld.getEnemies()) {
            assertTrue(e instanceof Vampire);
        }

    }

    /**
     * Tests if the Zombie Pit spawns a zombie when the character finishes a loops
     */
    @Test
    void testZombiePit() {
        
        // Load the world
        BasicHelper h = new BasicHelper();
        h.LoadBasicLoopWorld();

        LoopManiaWorld testWorld = new LoopManiaWorld(12, 12, h.orderedPath);
        testWorld.addBuilding(h.testHeroCastle);
        testWorld.setCharacter(h.testCharacter);
        // Add a Zombie Pit
        ZombiePitBuilding zp = new ZombiePitBuilding(new SimpleIntegerProperty(11), new SimpleIntegerProperty(11));

        // check the vampire castle is in the correct position
        assertTrue(zp.getX() == 11);
        assertTrue(zp.getY() == 11);

        // first loop without placing spawner
        for (int i = 0; i < h.totalLoopLength; i++) {
            testWorld.runTickMoves();
            testWorld.updateBuildings();
        }
        // check when the hero finishes a loops without zombie spawning
        assertTrue(testWorld.getEnemies().isEmpty());

        // place the zombie Building
        testWorld.addBuilding(zp);
        for (int i = 0; i < h.totalLoopLength - 1; i++) { // dont finish the loop
            testWorld.runTickMoves();
            testWorld.updateBuildings();
        }
        assertEquals(testWorld.getEnemies().size(), 1);
        
        for (int i = 0; i < h.totalLoopLength - 1; i++) {
            testWorld.runTickMoves();
            testWorld.updateBuildings();
        }

        // should be only 2 loops even though the char is on loop 3
        assertEquals(testWorld.getEnemies().size(), 2);

        for (Entity e : testWorld.getEnemies()) {
            assertTrue(e instanceof Zombie);
        }
    }

    /**
     * Test that the hero gains allies when he passes through a barrack
     */
    @Test
    void testBarrack() {
        
        // Load the world
        BasicHelper h = new BasicHelper();
        h.LoadBasicLoopWorld();

        LoopManiaWorld testWorld = new LoopManiaWorld(12, 12, h.orderedPath);
        testWorld.setCharacter(h.testCharacter);
        // Add a barracks building to the world
        BarracksBuilding barrack = new BarracksBuilding(new SimpleIntegerProperty(10), new SimpleIntegerProperty(10));
        testWorld.addBuilding(barrack);
        // check the ally list is empty
        assertEquals(testWorld.getAllies().size(), 0);

        for (int loop = 0; loop < h.totalLoopLength; loop++) {
            testWorld.runTickMoves();
            testWorld.updateBuildings();
        }
        // check the ally list is not empty
        assertEquals(testWorld.getAllies().size(), 1);
        // check the objects of the list are of instace Ally
        assertTrue(testWorld.getAllies().get(0) instanceof Ally);

        // loop again...
        for (int i = 0; i < h.totalLoopLength; i++) {
            testWorld.runTickMoves();
            testWorld.updateBuildings();
        }
        // check the ally list is not empty
        assertEquals(testWorld.getAllies().size(), 2);
    }

    /**
     * Tests if towers help kill enemies
     * Also tests property of tower's attack
     */
    @Test
    void testTower() {

        // Load the world
        BasicHelper h = new BasicHelper();
        h.LoadBasicLoopWorld();
        LoopManiaWorld testWorld = new LoopManiaWorld(12, 12, h.orderedPath);
        testWorld.setCharacter(h.testCharacter);
        // Add a tower and spawn a zombie
        TowerBuilding tower = new TowerBuilding(new SimpleIntegerProperty(11), new SimpleIntegerProperty(11));
        Zombie zombie = new Zombie(new PathPosition(20, h.orderedPath));
        testWorld.addBuilding(tower);
        testWorld.addEnemy(zombie);

        // Naked Level 1 character cannot kill a zombie by itself
        // but char + tower can 2v1 a zombie
        for (int loop = 0; loop < h.totalLoopLength; loop++) {
            testWorld.runTickMoves();
            testWorld.updateBuildings();
            BattleSimulator battle = testWorld.findBattle();
            if (battle != null) {
                testWorld.runBattle(battle);
            }
        }
        // make sure battle was won
        assertTrue(h.testCharacter.getCurrHP() > 0);
        assertEquals(zombie.getCurrHP(), 0);


        // check towerBattler damage
        TowerBattler twrBattler = new TowerBattler(tower);
        Slug slug = h.testSlug;
        assertEquals(slug.getCurrHP(), 12);

        twrBattler.setCritChance(0);
        twrBattler.attack(slug, null, null);
        // towers should do 5 damage
        assertEquals(slug.getCurrHP(), 7);


    }
    /**
     * Spawns a trap and tests if it does damage
     * Since zombies move randomly, can only assert it is damaged and not exact numbers
     */
    @Test
    void testTrap() {

        // Load the world
        BasicHelper h = new BasicHelper();
        h.LoadBasicLoopWorld();
        LoopManiaWorld testWorld = new LoopManiaWorld(12, 12, h.orderedPath);
        testWorld.setCharacter(h.testCharacter);
        // Add a trap and a zombie
        TrapBuilding trap = new TrapBuilding(new SimpleIntegerProperty(10), new SimpleIntegerProperty(10));
        Zombie zombie = new Zombie(new PathPosition(20, h.orderedPath));
        testWorld.addBuilding(trap);
        testWorld.addEnemy(zombie);

        assertTrue(testWorld.getBuildings().contains(trap));
        
        // Zombie should still have full hp
        assertEquals(zombie.getCurrHP(), zombie.getMaxHP());

        // loop one time
        for (int loop = 0; loop < h.totalLoopLength; loop++) {
            testWorld.runTickMoves();
            testWorld.updateBuildings();
        }

        // check the health of the zombie
        assertTrue(zombie.getCurrHP() < zombie.getMaxHP());
        // check that the trap is destroyed
        assertFalse(testWorld.getBuildings().contains(trap));
    }

    /**
     * Tests if a village successfully restores 25hp to the character
     */
    @Test
    void testVillage() {
        
        // Load the world
        BasicHelper h = new BasicHelper();
        h.LoadBasicLoopWorld();
        LoopManiaWorld testWorld = new LoopManiaWorld(12, 12, h.orderedPath);
        testWorld.setCharacter(h.testCharacter);
        // Add a village to the world
        VillageBuilding village = new VillageBuilding(new SimpleIntegerProperty(10), new SimpleIntegerProperty(10));
        testWorld.addBuilding(village);

        // deal damage to the character
        h.testCharacter.reduceHP(25);

        // check health of the character
        assertEquals(h.testCharacter.getCurrHP(), 75);
        for (int loop = 0; loop < 20; loop++) {
            testWorld.runTickMoves();
            testWorld.updateBuildings();
        }
        // check the health of the character
        // vilalge heals 25 hp
        assertEquals(h.testCharacter.getCurrHP(), 100);

    }

    @Test
    void testCampfire() {
        
        // Load the world 
        BasicHelper h = new BasicHelper();
        h.LoadBasicLoopWorld();
        LoopManiaWorld testWorld = new LoopManiaWorld(12, 12, h.orderedPath);
        testWorld.setCharacter(h.testCharacter);
        // Add a campfire to the world
        CampfireBuilding cf = new CampfireBuilding(new SimpleIntegerProperty(11), new SimpleIntegerProperty(11));
        testWorld.addBuilding(cf);

        // make the character walk into the battle radius of the campfire
        for (int loop = 0; loop < 20; loop++) {
            testWorld.runTickMoves();
            testWorld.updateBuildings();
        }
        // check the character has the campfire buff
        assertTrue(h.testCharacter.getStatusIDs().contains("CampfireBuff"));

        // move character away
        for (int loop = 0; loop < 10; loop++) {
            testWorld.runTickMoves();
            testWorld.runStatusWorldTicks();
            testWorld.updateBuildings();
        }
        assertFalse(h.testCharacter.getStatusIDs().contains("CampfireBuff"));

    }

    @Test
    void testZombiePitInvalidSpawn() {
        
        BasicHelper h = new BasicHelper();
        h.LoadBasicLoopWorld();

        LoopManiaWorld testWorld = new LoopManiaWorld(12, 12, h.orderedPath);
        testWorld.setCharacter(h.testCharacter);
        
        // place the zombieBuilding
        ZombiePitBuilding zp = new ZombiePitBuilding(new SimpleIntegerProperty(100), new SimpleIntegerProperty(100));
        testWorld.addBuilding(zp);
        for (int loop = 1; loop < 100; loop++) {
            for (int nestLoop = 0; nestLoop < h.totalLoopLength; nestLoop++) {
                testWorld.runTickMoves();
                testWorld.updateBuildings();
                assertTrue(testWorld.getEnemies().isEmpty());
            }
        }
        
    }

    /**
     * Test the vampire castle
     */
    @Test
    void testVampireCastleInvalidSpawn() {
        
        BasicHelper h = new BasicHelper();
        h.LoadBasicLoopWorld();

        LoopManiaWorld testWorld = new LoopManiaWorld(12, 12, h.orderedPath);
        testWorld.setCharacter(h.testCharacter);
        
        // place the vampire castle off the path and check that it cannot spawn any vampires
        VampireCastleBuilding vc = new VampireCastleBuilding(new SimpleIntegerProperty(100), new SimpleIntegerProperty(100));
        testWorld.addBuilding(vc);
        for (int loop = 1; loop < 100; loop++) {
            for (int nestLoop = 0; nestLoop < h.totalLoopLength; nestLoop++) {
                testWorld.runTickMoves();
                testWorld.updateBuildings();
                assertTrue(testWorld.getEnemies().isEmpty());
            }
        }
        
    }

    /**
     * Test the campfire with a zombie and ally in the world
     */
    @Test
    void testCampfireZombie() {
        
        // Load the world 
        BasicHelper h = new BasicHelper();
        h.LoadBasicLoopWorld();
        LoopManiaWorld testWorld = new LoopManiaWorld(12, 12, h.orderedPath);
        testWorld.setCharacter(h.testCharacter);
        testWorld.addEnemy(h.testZombie);
        testWorld.addAlly(h.testAlly);
        testWorld.addFriendlyEntity(h.testAlly);
        // Add a campfire to the world
        CampfireBuilding cf = new CampfireBuilding(new SimpleIntegerProperty(11), new SimpleIntegerProperty(11));
        testWorld.addBuilding(cf);

        // make the character walk into the battle radius of the campfire
        for (int loop = 0; loop < 20; loop++) {
            testWorld.runTickMoves();
            testWorld.updateBuildings();
        }
        // check the character has the campfire buff
        assertTrue(h.testCharacter.getStatusIDs().contains("CampfireBuff"));

        // move character away
        for (int loop = 0; loop < 10; loop++) {
            testWorld.runTickMoves();
            testWorld.runStatusWorldTicks();
            testWorld.updateBuildings();
        }
        assertFalse(h.testCharacter.getStatusIDs().contains("CampfireBuff"));

    }

}
