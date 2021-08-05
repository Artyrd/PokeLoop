package test.battleTest;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import org.javatuples.Pair;

import org.junit.Test;

import test.testHelpers.BasicHelper;
import unsw.loopmania.buildings.TowerBuilding;
import unsw.loopmania.enemies.*;
import unsw.loopmania.Ally;
import unsw.loopmania.Character;
import unsw.loopmania.Inventory;
import unsw.loopmania.movement.PathPosition;
import unsw.loopmania.LoopManiaWorld;
import javafx.beans.property.SimpleIntegerProperty;


public class BattleControllerTest {
    
    /**
     * Test the basic battle controller against one enemy
     */
    @Test
    public void testBasicController(){
        BasicHelper helper = new BasicHelper();
        LoopManiaWorld world = new LoopManiaWorld(10, 10, helper.orderedPath);

        // Slug at index 1 on 100 tile long path
        world.addEnemy(helper.testSlug);
        world.setCharacter(helper.testCharacter);
        world.runBattle(world.findBattle());
        world.updateMovingEntities();
        // Battle against the slug and defeat it
        assert(world.getEnemies().size() == 0);
        
    }

    /**
     * Test the battle controller against many slugs
     */
    @Test
    public void testControllerSlugs(){
        BasicHelper helper = new BasicHelper();
        LoopManiaWorld world = new LoopManiaWorld(10, 10, helper.orderedPath);

        List<Pair<Integer, Integer>> orderedPath = new ArrayList<Pair<Integer, Integer>>();
        for(int posX = 1; posX < 6; posX++){
            Pair<Integer, Integer> pos = new Pair<Integer, Integer>(posX, 1);
            orderedPath.add(pos);
        }
        // Add slugs to the path
        Slug slug0 = new Slug(new PathPosition(1, orderedPath));
        world.addEnemy(slug0);
        Slug slug1 = new Slug(new PathPosition(2, orderedPath));
        world.addEnemy(slug1);
        Slug slug2 = new Slug(new PathPosition(3, orderedPath));
        world.addEnemy(slug2);
        Slug slug3 = new Slug(new PathPosition(4, orderedPath));
        world.addEnemy(slug3);
        world.updateMovingEntities();

        Character character = new Character(new PathPosition(0, orderedPath), new Inventory(4,4));

        world.setCharacter(character);

        world.runBattle(world.findBattle());
        world.updateMovingEntities();
        // Two slugs should be defeated, leaving two alive
        assert(world.getEnemies().size() == 2); 
    }

    /**
     * Test the battle controller eith enemies out of range
     */
    @Test
    public void testControllerSlugOutOfRange(){
        BasicHelper helper = new BasicHelper();
        LoopManiaWorld world = new LoopManiaWorld(10, 10, helper.orderedPath);
        // Set up a path
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<Pair<Integer, Integer>>();
        for(int posX = 1; posX < 6; posX++){
            Pair<Integer, Integer> pos = new Pair<Integer, Integer>(posX, 1);
            orderedPath.add(pos);
        }
        // Add slugs to the path
        Slug slug0 = new Slug(new PathPosition(2, orderedPath));
        world.addEnemy(slug0);        

        Character character = new Character(new PathPosition(0, orderedPath), new Inventory(4,4));

        world.setCharacter(character);

        world.runBattle(world.findBattle());
        world.updateMovingEntities();
        // The slug should not be in battle range, and should survive
        assert(world.getEnemies().size() == 1); 
    }

    /**
     * Test the battle controller with multiple enemy variants and battle radii
     */
    @Test
    public void testControllerMixedEnemies(){
        BasicHelper helper = new BasicHelper();
        LoopManiaWorld world = new LoopManiaWorld(10, 10, helper.orderedPath);

        List<Pair<Integer, Integer>> orderedPath = new ArrayList<Pair<Integer, Integer>>();
        for(int posX = 1; posX < 10; posX++){
            Pair<Integer, Integer> pos = new Pair<Integer, Integer>(posX, 1);
            orderedPath.add(pos);
        }

        // Add enemies of each type to the path
        Slug slug = new Slug(new PathPosition(1, orderedPath));
        world.addEnemy(slug);
        Zombie zombie = new Zombie(new PathPosition(2, orderedPath));
        world.addEnemy(zombie);
        Vampire vampire = new Vampire(new PathPosition(3, orderedPath));
        world.addEnemy(vampire);
        
        // Increase character level, equip sword and armour
        Character character = new Character(new PathPosition(0, orderedPath), new Inventory(4,4));
        character.gainXP(2700); 
        assertEquals(character.getLevel(), 10);
        character.equip(helper.testSword);
        assertEquals(character.getCurrATK(), 11.5, 0.001); // 6.5atk base + 5atk from sword
        character.equip(helper.testArmour);

        world.setCharacter(character);

        List<Enemy> defeatedEnemies = world.runBattle(world.findBattle());
        world.cleanupBattle(defeatedEnemies);

        // Only the slug and zombie should be defeated, leaving the vampire
        assert(world.getEnemies().size() == 1); 
    }

    /**
     * Test the sorted targeting order of battle
     */
    @Test
    public void testSortingOrder(){
        BasicHelper helper = new BasicHelper();
        LoopManiaWorld world = new LoopManiaWorld(10, 10, helper.orderedPath);
        // Set up a path
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<Pair<Integer, Integer>>();
        for(int posX = 1; posX < 10; posX++){
            Pair<Integer, Integer> pos = new Pair<Integer, Integer>(posX, 1);
            orderedPath.add(pos);
        }

        // Add enemies of each type to the path
        Zombie zombie = new Zombie(new PathPosition(1, orderedPath));       // Will Battle
        world.addEnemy(zombie);
        Zombie zombie2 = new Zombie(new PathPosition(5, orderedPath));      // Will not battle
        world.addEnemy(zombie2);
        Vampire vampire = new Vampire(new PathPosition(3, orderedPath));    // Will Battle
        world.addEnemy(vampire);
        Slug slug = new Slug(new PathPosition(2, orderedPath));             // Will Battle
        world.addEnemy(slug);
        
        // Order of fighters is Zombie, Slug, Vampire
        
        // Level up the character, equip sword, equip armour, add 25 potions
        Character character = new Character(new PathPosition(0, orderedPath), new Inventory(4,4));
        character.gainXP(2700); 
        assertEquals(character.getLevel(), 10);
        character.equip(helper.testSword);
        assertEquals(character.getCurrATK(), 11.5, 0.001); // 6.5atk base + 5atk from sword
        character.equip(helper.testArmour);
        for(int itemNum = 0; itemNum < 25; itemNum++){
            character.addItem("Potion");
        }
        assert(character.getPotions() == 25);

        world.setCharacter(character);

        world.runBattle(world.findBattle());

        // Character should win battle, leaving one enemy which was not battled
        assert(world.getEnemies().size() == 1); 

    }

    /**
     * Test another variant with support radii
     */
    @Test
    public void testSortingOrder2(){
        BasicHelper helper = new BasicHelper();
        LoopManiaWorld world = new LoopManiaWorld(10, 10, helper.orderedPath);
        // Set up a path
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<Pair<Integer, Integer>>();
        for(int posX = 1; posX < 10; posX++){
            Pair<Integer, Integer> pos = new Pair<Integer, Integer>(posX, 1);
            orderedPath.add(pos);
        }

        // Add enemies of each type to the path
        Zombie zombie = new Zombie(new PathPosition(1, orderedPath));       // Will Battle
        world.addEnemy(zombie);
        Zombie zombie2 = new Zombie(new PathPosition(3, orderedPath));      // Will not battle
        world.addEnemy(zombie2);
        Vampire vampire = new Vampire(new PathPosition(4, orderedPath));    // Will Battle
        world.addEnemy(vampire);
        Slug slug = new Slug(new PathPosition(2, orderedPath));             // Will Battle
        world.addEnemy(slug);
        
        // Order of fighters is Zombie, Slug, Zombie, Vampire
        
        // Level up the character, equip sword, equip armour, add 25 potions
        Character character = new Character(new PathPosition(0, orderedPath), new Inventory(4,4));
        character.gainXP(5000); 
        character.equip(helper.testSword);
        character.equip(helper.testArmour);
        for(int itemNum = 0; itemNum < 25; itemNum++){
            character.addItem("Potion");
        }
        assert(character.getPotions() == 25);

        world.setCharacter(character);

        world.runBattle(world.findBattle());

        // All enemies should be defeated including the zombie out of range, as it is in support range
        assert(world.getEnemies().size() == 0); 

    }

    /**
     * Test the battle controller with the help of a tower
     */
    @Test
    public void testTowerBattle(){
        BasicHelper helper = new BasicHelper();
        LoopManiaWorld world = new LoopManiaWorld(10, 10, helper.orderedPath);
        // Set up a path
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<Pair<Integer, Integer>>();
        for(int posX = 1; posX < 10; posX++){
            Pair<Integer, Integer> pos = new Pair<Integer, Integer>(posX, 1);
            orderedPath.add(pos);
        }

        // Level up the character, equip armour
        Character character = new Character(new PathPosition(0, orderedPath), new Inventory(4,4));
        character.gainXP(5000); 
        character.equip(helper.testArmour);
        character.addItem("Staff");
        
        // Add tower to the world
        TowerBuilding tower = new TowerBuilding(new SimpleIntegerProperty(2), new SimpleIntegerProperty(1));
        tower.update(world);
        assert(world.getActiveStructures().size() == 1);

        // Add zombies to the path and world
        Zombie zombie = new Zombie(new PathPosition(1, orderedPath));
        Zombie zombie2 = new Zombie(new PathPosition(1, orderedPath));
        Zombie zombie3 = new Zombie(new PathPosition(1, orderedPath));

        world.addEnemy(zombie);
        world.addEnemy(zombie2);
        world.addEnemy(zombie3);

        world.setCharacter(character);

        world.runBattle(world.findBattle());
        // Character should win the battle, defeating all enemies
        assert(world.getEnemies().size() == 0);

    }

    /**
     * Test the order of battle with multiple towers and enemies
     */
    @Test
    public void testOrderOfEntities(){
        BasicHelper helper = new BasicHelper();
        LoopManiaWorld world = new LoopManiaWorld(10, 10, helper.orderedPath);
        // set up the path
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<Pair<Integer, Integer>>();
        for(int posX = 1; posX < 10; posX++){
            Pair<Integer, Integer> pos = new Pair<Integer, Integer>(posX, 1);
            orderedPath.add(pos);
        }

        // Spawn 25 potions
        Character character = new Character(new PathPosition(0, orderedPath), new Inventory(4,4));
        for(int posX = 0; posX < 25; posX++){
            character.addItem("Potion");
        }
        character.addItem("Stake");

        // Add 2 towers to the world
        TowerBuilding tower = new TowerBuilding(new SimpleIntegerProperty(0), new SimpleIntegerProperty(1));
        TowerBuilding tower2 = new TowerBuilding(new SimpleIntegerProperty(9), new SimpleIntegerProperty(1));
        tower.update(world);
        tower2.update(world);

        // Add enemies of each type to the path/world
        Slug slug = new Slug(new PathPosition(1, orderedPath));
        Zombie zombie = new Zombie(new PathPosition(2, orderedPath));
        Vampire vampire = new Vampire(new PathPosition(4, orderedPath));
        Slug slug2 = new Slug(new PathPosition(8, orderedPath));

        world.addEnemy(zombie);
        world.addEnemy(slug);
        world.addEnemy(slug2);
        world.addEnemy(vampire);
        world.setCharacter(character);

        List<Enemy> defeatedEnemies = world.runBattle(world.findBattle());
        world.cleanupBattle(defeatedEnemies);
        // Character should win battle against those within its range, leaving the others alive
        assert(world.getActiveStructures().size() == 2);
        assert(world.getEnemies().size() == 2);

    }

    /**
     * Test the battle controller against a doggie boss
     */
    @Test
    public void testDoggieDefeated(){
        BasicHelper helper = new BasicHelper();
        LoopManiaWorld world = new LoopManiaWorld(10, 10, helper.orderedPath);
        // Set up a path
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<Pair<Integer, Integer>>();
        for(int posX = 1; posX < 10; posX++){
            Pair<Integer, Integer> pos = new Pair<Integer, Integer>(posX, 1);
            orderedPath.add(pos);
        }

        // Spawn 50 potions
        Character character = new Character(new PathPosition(0, orderedPath), new Inventory(4,4));
        for(int itemNum = 0; itemNum < 50; itemNum++){
            character.addItem("Potion");
            world.addAlly(new Ally());
        }
        character.addItem("Sword");
        character.addItem("Helmet");
        character.addItem("Shield");
        character.addItem("Armour");
        character.addItem("TheOneRing");
        character.addItem("Gold");
        character.addItem("");
        // Level up the character and equip items
        character.gainXP(20000);
        character.equip(helper.testSword);
        character.equip(helper.testShield);
        world.setCharacter(character);

        assert(world.getAllies().size() == 50);

        // Set up doggie in the world
        Doggie doggie = new Doggie(new PathPosition(1, orderedPath));
        world.addBoss(doggie);
        assert(world.getAliveBosses().size() == 1);
        assert(world.getAliveBosses().get(0) instanceof Doggie);
        assert(world.getEnemies().size() == 1);

        // Run the battle and defeat doggie
        List<Enemy> defeatedEnemies = world.runBattle(world.findBattle());
        world.cleanupBattle(defeatedEnemies);
        assert(world.getCharacter().getCurrHP() > 0);

        assert(world.getAliveBosses().size() == 0);
        assert(world.getEnemies().size() == 0);

    }


}
