package test.worldTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.javatuples.Pair;
import org.junit.Test;

import test.testHelpers.BetterHelper;
import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.enemies.*;
import unsw.loopmania.movement.PathPosition;

public class EnemySpawnTest {
    
    /**
     * Test random spawning of slugs
     */
    @Test
    public void testSpawnEnemiesSlug(){
        BetterHelper h = new BetterHelper();
        List<Enemy> spawnedEnemies = new ArrayList<Enemy>();
        // Test when loopNum != 10 && == 10, spawnedDoggies < 1
        for(int attempt = 0; attempt < 100; attempt++){
            spawnedEnemies.addAll(h.testWorld.possiblySpawnEnemies());
        }
        assertEquals(h.testWorld.getEnemies().size(), 2); // There can only be 2 slugs in the world at a time
    }

    /**
     * Test random spawning of doggie after 25 loops
     */
    @Test
    public void testSpawnDoggie(){
        BetterHelper h = new BetterHelper();
        List<Enemy> spawnedEnemies = new ArrayList<Enemy>();
        for(int attempt = 0; attempt < 25; attempt++){
            h.testWorld.addLoop();
            spawnedEnemies.addAll(h.testWorld.possiblySpawnEnemies());
        }

        // 2 slugs spawned and doggie
        int numEnemies = h.testWorld.getEnemies().size();  
        assert(numEnemies <= 3);

    }

    @Test
    public void testUpdateMovingEntities(){
        BetterHelper helper = new BetterHelper();
        LoopManiaWorld world = new LoopManiaWorld(10, 10, helper.orderedPath);
        // Set up a path
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<Pair<Integer, Integer>>();
        for(int posX = 1; posX < 10; posX++){
            Pair<Integer, Integer> pos = new Pair<Integer, Integer>(posX, 1);
            orderedPath.add(pos);
        }

        Slug slug = new Slug(new PathPosition(1, orderedPath));
        slug.setCurrHP(0);
        world.addEnemy(slug);
        world.updateMovingEntities();
        assert(world.getEnemies().size() == 0);
    }

}