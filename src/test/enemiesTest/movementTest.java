package test.enemiesTest;

import org.junit.jupiter.api.Test;

import javafx.beans.property.SimpleIntegerProperty;
import test.testHelpers.BasicHelper;
import test.testHelpers.BetterHelper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.javatuples.Pair;

import unsw.loopmania.Dragon;
import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.buildings.CampfireBuilding;
import unsw.loopmania.enemies.Enemy;
import unsw.loopmania.enemies.Slug;
import unsw.loopmania.enemies.StageBoss;
import unsw.loopmania.enemies.StageBossPet;
import unsw.loopmania.enemies.Vampire;
import unsw.loopmania.movement.PathPosition;
import unsw.loopmania.movement.RandomMovementBehaviour;
import unsw.loopmania.movement.ZombieMovementBehaviour;

public class movementTest {

    /**
     * Test the movement behaviour of the slug
     */
    @Test 
    void TestSlugMovementBehaviour(){
        
        // Create a SlugMovementBehaviour class 
        BasicHelper basicHelper = new BasicHelper();
        PathPosition testPos = basicHelper.testPosition;
        Slug slug = new Slug(testPos);
        
        // Move clockwise and check slug is in bounds
        RandomMovementBehaviour mvBehaviour = new RandomMovementBehaviour(5);
        //mvBehaviour.setDirectionChoice(0);
        slug.setMovementBehaviour(mvBehaviour);
        for(int loop = 0; loop < 1000; loop++){
            slug.move();
            assertTrue(slug.getPosition().getX().get() < 100);
            assertTrue(slug.getPosition().getY().get() < 100);
        }

        // Move counter clockwise and check slug is in bounds
        //mvBehaviour.setDirectionChoice(1);
        slug.setMovementBehaviour(mvBehaviour);
        for(int loop = 0; loop < 1000; loop++){
            slug.move();
            assertTrue(slug.getPosition().getX().get() < 100);
            assertTrue(slug.getPosition().getY().get() < 100);
        }

    }

    /**
     * Test the random movement behaviour of the slug
     */
    @Test
    void TestSeededRandomMovementBehaviour(){

        // Create a slug with RandomMovementBehaviour
        BasicHelper basicHelper = new BasicHelper();
        PathPosition testPos = basicHelper.testPosition;
        Slug slug = new Slug(testPos);

        bufferWait(slug, 5);
        assert(slug.getPosition().getX().get() == 2);

        bufferWait(slug, 5);
        assert(slug.getPosition().getX().get() == 3);

        bufferWait(slug, 5);
        assert(slug.getPosition().getX().get() == 4);

        bufferWait(slug, 5);
        assert(slug.getPosition().getX().get() == 3);

        bufferWait(slug, 5);
        assert(slug.getPosition().getX().get() == 2);

        bufferWait(slug, 5);
        assert(slug.getPosition().getX().get() == 1);

        bufferWait(slug, 5);
        assert(slug.getPosition().getX().get() == 2);

        bufferWait(slug, 5);
        assert(slug.getPosition().getX().get() == 3);

        bufferWait(slug, 5);
        assert(slug.getPosition().getX().get() == 2);

        bufferWait(slug, 5);
        assert(slug.getPosition().getX().get() == 3);

    }

    /**
     * Helper function to tick movement
     * @param enemy
     * @param wait 
     */
    private void bufferWait(Enemy enemy, int wait){
        for(int i = 0; i < wait + 1; i++){
            enemy.move();
        }
    }


    /**
     * Test the zombie is within bounds when it is walking
     */
    @Test 
    void TestZombieMovementBehaviour(){
        
        // Create a ZombieMovementBehaviour class 
        ZombieMovementBehaviour zMovement = new ZombieMovementBehaviour();
        BasicHelper basicHelper = new BasicHelper();
        int maxDist = zMovement.getMaxDistance() * 2 + 1; // Zombie starts in the middle tile, it has maxDist clockwise and anticlockwise
        PathPosition testPos = basicHelper.testPosition;
        
        // Going clockwise
        zMovement.setDirectionChoice(0);
        for(int loop = 0; loop < 1000; loop++){
            zMovement.move(testPos, null);
            assertTrue(zMovement.getCurrPosition() < maxDist);
        }

        // Going counter clockwise
        zMovement.setDirectionChoice(1);
        for(int loop = 0; loop < 1000; loop++){
            zMovement.move(testPos, null);
            assertTrue(zMovement.getCurrPosition() < maxDist);
        }

    }

    /**
     * Test the vampire's normal movement: anticlockwise
     */
    @Test
    void TestVampireMovementBehaviour(){
        
        BetterHelper h = new BetterHelper(); 
        Vampire vampire = h.testVampire;

        assertEquals(vampire.getX(), 2);
        assertEquals(vampire.getY(), 1);

        // vampire is slower and moves once every 3 ticks
        vampire.move();
        vampire.move();
        vampire.move();
        assertEquals(vampire.getX(), 1);
        assertEquals(vampire.getY(), 1);

        // again..
        vampire.move();
        vampire.move();
        vampire.move();
        assertEquals(vampire.getX(), 1);
        assertEquals(vampire.getY(), 2);

    }

    /**
     * Test the vampire runs away from campfires
     */
    @Test
    void TestCampfireVampireCW(){
        
        // Create an orderedPath 
        ArrayList<Pair<Integer, Integer>> orderedPath = new ArrayList<Pair<Integer, Integer>>();
        for(int loop = 0; loop < 10; loop++){
            Pair<Integer, Integer> pos = new Pair<Integer, Integer>(loop, 1);
            orderedPath.add(pos);
        }
        LoopManiaWorld world = new LoopManiaWorld(10, 10, orderedPath);

        PathPosition vampPos = new PathPosition(8, orderedPath);
        Vampire vampire = new Vampire(vampPos);

        CampfireBuilding campfire = new CampfireBuilding(new SimpleIntegerProperty(9), new SimpleIntegerProperty(1));
        world.addEnemy(vampire);
        world.addBuilding(campfire);

        assert(vampire.getX() == 8);
        assert(vampire.getY() == 1);

        // vampire is a slowpoke that moves once every 3 ticks
        vampire.move();
        vampire.move();
        vampire.move();
        //make sure vampire moved away from the campfire!
        assert(vampire.getX() == 7);
        assert(vampire.getY() == 1);

        // again..
        vampire.move();
        vampire.move();
        vampire.move();
        //make sure vampire moved away from the campfire!
        assert(vampire.getX() == 6);
        assert(vampire.getY() == 1);

    }

    /**
     * Test the movement of vampires around a campfire
     */
    @Test
    void TestCampfireVampireCCW(){
        
        // Create an orderedPath 
        ArrayList<Pair<Integer, Integer>> orderedPath = new ArrayList<Pair<Integer, Integer>>();
        for(int loop = 0; loop < 10; loop++){
            Pair<Integer, Integer> pos = new Pair<Integer, Integer>(loop, 1);
            orderedPath.add(pos);
        }
        // Set up the vampire and campfire
        LoopManiaWorld world = new LoopManiaWorld(10, 10, orderedPath);
        PathPosition vampPos = new PathPosition(4, orderedPath);
        Vampire vampire = new Vampire(vampPos);
        CampfireBuilding campfire = new CampfireBuilding(new SimpleIntegerProperty(0), new SimpleIntegerProperty(1));
        world.addEnemy(vampire);
        world.addBuilding(campfire);

        assert(vampire.getX() == 4);
        assert(vampire.getY() == 1);

        // vampire is a slowpoke that moves once every 3 ticks
        vampire.move();
        vampire.move();
        vampire.move();
        //make sure vampire moved towards from the campfire!
        System.err.println(vampire.getX());
        assert(vampire.getX() == 3);
        assert(vampire.getY() == 1);

        // again..
        vampire.move();
        vampire.move();
        vampire.move();
        //make sure vampire moved away from the campfire!
        assert(vampire.getX() == 4);
        assert(vampire.getY() == 1);

        // again..
        vampire.move();
        vampire.move();
        vampire.move();
        //make sure vampire moved away from the campfire!
        assert(vampire.getX() == 5);
        assert(vampire.getY() == 1);

    }

    /**
     * Test the movement of a vampire not in range of the vampfire
     */
    @Test
    void TestCampfireVampireNotClose(){
        
        // Create an orderedPath 
        ArrayList<Pair<Integer, Integer>> orderedPath = new ArrayList<Pair<Integer, Integer>>();
        for(int loop = 0; loop < 12; loop++){
            Pair<Integer, Integer> pos = new Pair<Integer, Integer>(loop, 1);
            orderedPath.add(pos);
        }
        LoopManiaWorld world = new LoopManiaWorld(12, 12, orderedPath);
        // Set up the vampire and campfire
        PathPosition vampPos = new PathPosition(11, orderedPath);
        Vampire vampire = new Vampire(vampPos);
        CampfireBuilding campfire = new CampfireBuilding(new SimpleIntegerProperty(2), new SimpleIntegerProperty(1));
        world.addEnemy(vampire);
        world.addBuilding(campfire);

        assert(vampire.getX() == 11);
        assert(vampire.getY() == 1);

        // vampire is a slowpoke that moves once every 3 ticks
        vampire.move();
        vampire.move();
        vampire.move();
        //make sure vampire moved towards from the campfire!
        assert(vampire.getX() == 10);
        assert(vampire.getY() == 1);
        
        // again..
        vampire.move();
        vampire.move();
        vampire.move();
        //make sure vampire moved towards from the campfire!
        assert(vampire.getX() == 9);
        assert(vampire.getY() == 1);

    }


    /**
     * Test the dragon's normal movement: anticlockwise
     */
    @Test
    void TestDragonMovementBehaviour(){
        
        BetterHelper h = new BetterHelper(); 
        Dragon dragon = h.testDragon;

        assertEquals(dragon.getX(), 2);
        assertEquals(dragon.getY(), 1);

        // dragon moves right until it hits the edge, every 2 ticks
        // the edge of the test world is X = 10
        for (int i = 3; i < 11; i++) {
            dragon.move();
            dragon.move();
            assertEquals(dragon.getX(), i);
            assertEquals(dragon.getY(), 1);
        }

        // dragon should now loop anticlockwise
        // to the left
        for (int i = 9; i >0 ; i--) {
            dragon.move();
            dragon.move();
            assertEquals(dragon.getX(), i);
            assertEquals(dragon.getY(), 1);
        }
        // to the bottom
        for (int i = 2; i <11 ; i++) {
            dragon.move();
            dragon.move();
            assertEquals(dragon.getX(), 1);
            assertEquals(dragon.getY(), i);
        }
 
        // to the right
        for (int i = 2; i <11 ; i++) {
            dragon.move();
            dragon.move();
            assertEquals(dragon.getX(), i);
            assertEquals(dragon.getY(), 10);
        }
 
        // to the top
        for (int i = 9; i >0 ; i--) {
            dragon.move();
            dragon.move();
            assertEquals(dragon.getX(), 10);
            assertEquals(dragon.getY(), i);
        }
 
    }

    @Test
    void TestStageBossMovementBehaviour(){
        BetterHelper h = new BetterHelper();
        StageBoss stageBoss = h.testStageBoss;

        for(int move = 0; move < 20; move++){
            int prevX = stageBoss.getX();
            int prevY = stageBoss.getY();
            stageBoss.move();
            int newX = stageBoss.getX();
            int newY = stageBoss.getY();
            assertEquals(prevX, newX);
            assertEquals(prevY, newY);
        }

    }

    @Test
    void TestStageBossPetMovementBehavior(){
        BetterHelper h = new BetterHelper();
        StageBossPet stageBossPet = h.testStageBossPet;

        bufferWait(stageBossPet, 3);

        for(int move = 0; move < 20; move++){
            bufferWait(stageBossPet, 3);
            int nextX = stageBossPet.getX();
            assert(nextX <= 3);
            assert(nextX >= 1);
        }

    }




}
