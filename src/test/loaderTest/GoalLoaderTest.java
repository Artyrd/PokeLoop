package test.loaderTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.util.List;

import org.junit.Test;

import javafx.beans.property.SimpleIntegerProperty;
import test.testHelpers.BasicHelper;
import test.testHelpers.BetterHelper;
import unsw.loopmania.items.Gold;
import unsw.loopmania.movement.PathPosition;
import unsw.loopmania.Ally;
import unsw.loopmania.Character;
import unsw.loopmania.GoalLoader;
import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.enemies.Doggie;
import unsw.loopmania.enemies.ElanMuske;
import unsw.loopmania.enemies.Enemy;

public class GoalLoaderTest {
    
    /**
     * Test loading a world with simple goals
     * @throws FileNotFoundException
     */
    @Test
    public void GoalLoaderTestSimple() throws FileNotFoundException{
        BetterHelper h = new BetterHelper();
        LoopManiaWorld world = h.testWorld;

        // Create a simple experience goal
        JSONObject jsonGoal = new JSONObject()
                                .put("goal", "experience")
                                .put("quantity", 123456);

        // Load the goal into the world
        GoalLoader goalLoader = new GoalLoader();
        world.setWorldGoal(goalLoader.loadGoals(jsonGoal));
        assertFalse(world.reachedGoal());
        String goalState = "O  EXP: 0/123456";
        assertEquals(goalState, world.goalsPrettyPrint());

        // Check that the player meet the goal 
        Character character = world.getCharacter();
        character.gainXP(123456);
        assertTrue(world.reachedGoal());
        
        goalState = "X  EXP: 123456/123456";
        assertEquals(goalState, world.goalsPrettyPrint());

    }

    /**
     * Test loading a world with a gold goal
     * @throws FileNotFoundException
     */
    @Test
    public void GoalLoaderGoldTest() throws FileNotFoundException{
        BetterHelper h = new BetterHelper();
        LoopManiaWorld world = h.testWorld;

        // Create a simple gold goal
        JSONObject jsonGoal = new JSONObject()
                                .put("goal", "gold")
                                .put("quantity", 1000);

        // Load goal into the world
        GoalLoader goalLoader = new GoalLoader();
        world.setWorldGoal(goalLoader.loadGoals(jsonGoal));
        assertFalse(world.reachedGoal());

        Character character = world.getCharacter();
        // Give the character gold
        int totalGold = 0;
        for(int i = 0; i < 1000; i++){
            Gold gold = new Gold(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
            character.addItem(gold);
            totalGold = totalGold + gold.getValue();
        }

        // Check that the player has met the goal
        assert(character.getGold() > 1000);
        assertTrue(world.reachedGoal());
        String goalState = "X  GOLD: " + String.valueOf(totalGold) + "/1000";
        assertEquals(goalState, world.goalsPrettyPrint());
    }

    /**
     * Test loading a world with cycles goal
     * @throws FileNotFoundException
     */
    @Test
    public void GoalLoaderCyclesTest() throws FileNotFoundException{
        BetterHelper h = new BetterHelper();
        LoopManiaWorld world = h.testWorld;

        // Create a simple cycle goal
        JSONObject jsonGoal = new JSONObject()
                                .put("goal", "cycles")
                                .put("quantity", 1234);
        // Load goal into the world
        GoalLoader goalLoader = new GoalLoader();
        world.setWorldGoal(goalLoader.loadGoals(jsonGoal));
        assertFalse(world.reachedGoal());
        String goalState = "O  LOOP: 1/1234";
        assertEquals(goalState, world.goalsPrettyPrint());

        // Loop around the world
        for(int i = 0; i < 1233; i++){
            world.addLoop();
        }
        assert(world.getLoop() == 1234);

        // Check that the player has met the goal
        assertTrue(world.reachedGoal());
        goalState = "X  LOOP: 1234/1234";
        assertEquals(goalState, world.goalsPrettyPrint());

    }

    /**
     * Test loading a world with gold and cycles goal
     * @throws FileNotFoundException
     */
    @Test
    public void GoalLoaderComplexGoldCyclesTest() throws FileNotFoundException{
        BetterHelper h = new BetterHelper();
        LoopManiaWorld world = h.testWorld;
        
        // Create a goal with multiple conditions
        JSONObject jsonSubGoal1 = new JSONObject()
                                .put("goal", "cycles")
                                .put("quantity", 100);
        JSONObject jsonSubGoal2 = new JSONObject()
                                .put("goal", "gold")
                                .put("quantity", 5000);
        JSONArray jsonSubGoals = new JSONArray()
                                .put(jsonSubGoal1)
                                .put(jsonSubGoal2);
        JSONObject jsonGoal = new JSONObject()
                                .put("goal", "AND")
                                .put("subgoals", jsonSubGoals);
        // Load the goals into the world
        GoalLoader goalLoader = new GoalLoader();
        world.setWorldGoal(goalLoader.loadGoals(jsonGoal));
        assertFalse(world.reachedGoal());

        // Loop around the world
        Character character = world.getCharacter();
        for(int i = 0; i < 100; i++){
            world.addLoop();
        }
        // Check that the player has not met the combined goals
        assert(world.getLoop() == 101);
        assertFalse(world.reachedGoal());

        // Add gold to the character
        for(int i = 0; i < 5000; i++){
            Gold gold = new Gold(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
            character.addItem(gold);
        }
        assert(character.getGold() > 5000);

        // Check that the player has met the combined goals
        assertTrue(world.reachedGoal());

    }

    /**
     * Test a world with multiple gold and cycles goals
     * @throws FileNotFoundException
     */
    @Test
    public void GoalLoaderComplexGoldCyclesTestFail() throws FileNotFoundException{
        BetterHelper h = new BetterHelper();
        LoopManiaWorld world = h.testWorld;

        // Create a goal with multiple conditions
        JSONObject jsonSubGoal1 = new JSONObject()
                                .put("goal", "cycles")
                                .put("quantity", 100);
        JSONObject jsonSubGoal2 = new JSONObject()
                                .put("goal", "gold")
                                .put("quantity", 5000);
        JSONArray jsonSubGoals = new JSONArray()
                                .put(jsonSubGoal1)
                                .put(jsonSubGoal2);
        JSONObject jsonGoal = new JSONObject()
                                .put("goal", "AND")
                                .put("subgoals", jsonSubGoals);
        // Load goals into the world
        GoalLoader goalLoader = new GoalLoader();
        world.setWorldGoal(goalLoader.loadGoals(jsonGoal));
        assertFalse(world.reachedGoal());

        Character character = world.getCharacter();
        // Loop around the world
        for(int i = 0; i < 50; i++){
            world.addLoop();
        }
        assert(world.getLoop() == 51);

        // Check that the player has not met the combined goals
        assertFalse(world.reachedGoal());

        // Add gold to the character
        for(int i = 0; i < 5000; i++){
            Gold gold = new Gold(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
            character.addItem(gold);
        }
        assert(character.getGold() > 5000);
        // Check that the player has not met the combined goals
        assertFalse(world.reachedGoal());

    }

    /**
     * Test loading a world with multiple gold and cycles goals
     * @throws FileNotFoundException
     */
    @Test
    public void GoalLoaderComplexGoldCyclesTest2() throws FileNotFoundException{
        BetterHelper h = new BetterHelper();
        LoopManiaWorld world = h.testWorld;
        // Create goals with multiple conditions
        JSONObject jsonSubGoal1 = new JSONObject()
                                .put("goal", "cycles")
                                .put("quantity", 100);
        JSONObject jsonSubGoal2 = new JSONObject()
                                .put("goal", "gold")
                                .put("quantity", 5000);
        JSONArray jsonSubGoals = new JSONArray()
                                .put(jsonSubGoal1)
                                .put(jsonSubGoal2);
        JSONObject jsonGoal = new JSONObject()
                                .put("goal", "OR")
                                .put("subgoals", jsonSubGoals);
        // Load goals into the world
        GoalLoader goalLoader = new GoalLoader();
        world.setWorldGoal(goalLoader.loadGoals(jsonGoal));
        assertFalse(world.reachedGoal());

        Character character = world.getCharacter();
        // Loop around the world
        for(int i = 0; i < 100; i++){
            world.addLoop();
        }

        // Check that the player has met the goal conditions
        assert(world.getLoop() == 101);
        assertTrue(world.reachedGoal());

        // Add gold to the character
        int totalGold = 0;
        for(int i = 0; i < 5000; i++){
            Gold gold = new Gold(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
            character.addItem(gold);
            totalGold = totalGold + gold.getValue();
        }
        assert(character.getGold() > 5000);
        // Check that the player has met the goal conditions
        assertTrue(world.reachedGoal());
        String goalState = "X  LOOP: 101/100\n     OR\nX  GOLD: " + totalGold + "/5000";
        assertEquals(goalState, world.goalsPrettyPrint());

    }

    /**
     * Test loading a world with complex goals with all goal types
     * @throws FileNotFoundException
     */
    @Test
    public void GoalLoaderTestComplex() throws FileNotFoundException{
        BetterHelper h = new BetterHelper();
        LoopManiaWorld world = h.testWorld;
        // Create goals with multiple conditions in multiple subgoals
        JSONObject jsonSubSubGoal1 = new JSONObject()
                                .put("goal", "experience")
                                .put("quantity", 123456);
        JSONObject jsonSubSubGoal2 = new JSONObject()
                                .put("goal", "gold")
                                .put("quantity", 900000);
        JSONArray jsonSubSubGoals = new JSONArray()
                                .put(jsonSubSubGoal1)
                                .put(jsonSubSubGoal2);

        JSONObject jsonSubGoal1 = new JSONObject()
                                .put("goal", "cycles")
                                .put("quantity", 100);
        JSONObject jsonSubGoal2 = new JSONObject()
                                .put("goal", "OR")
                                .put("subgoals", jsonSubSubGoals);

        JSONArray jsonSubGoals = new JSONArray()
                                .put(jsonSubGoal1)
                                .put(jsonSubGoal2);
        JSONObject jsonGoal = new JSONObject()
                                .put("goal", "AND")
                                .put("subgoals", jsonSubGoals);
        //System.out.println(jsonGoal.toString());
        // Load goals into the world
        GoalLoader goalLoader = new GoalLoader();
        world.setWorldGoal(goalLoader.loadGoals(jsonGoal));
        assertFalse(world.reachedGoal());

        // Give the character exp and check that the goal has not been met yet
        Character character = world.getCharacter();
        character.gainXP(123456);
        assertFalse(world.reachedGoal());

        // Loop around the world
        for(int i = 0; i < 100; i++){
            world.addLoop();
        }
        // Check that the player has met the goals
        assertTrue(world.reachedGoal());

        String goalState = "X  LOOP: 101/100\n\n     AND\n\nX  EXP: 123456/123456\n     OR\nO  GOLD: 0/900000";
        assertEquals(goalState, world.goalsPrettyPrint());

    }

    /**
     * Test loading a world with complex goals using OR
     * @throws FileNotFoundException
     */
    @Test
    public void GoalLoaderTestORComplex() throws FileNotFoundException{
        BetterHelper h = new BetterHelper();
        LoopManiaWorld world = h.testWorld;
        // Create goals with multiple conditions in multiple subgoals
        JSONObject jsonSubSubGoal1 = new JSONObject()
                                .put("goal", "experience")
                                .put("quantity", 123456);
        JSONObject jsonSubSubGoal2 = new JSONObject()
                                .put("goal", "gold")
                                .put("quantity", 900000);
        JSONArray jsonSubSubGoals = new JSONArray()
                                .put(jsonSubSubGoal1)
                                .put(jsonSubSubGoal2);

        JSONObject jsonSubGoal1 = new JSONObject()
                                .put("goal", "cycles")
                                .put("quantity", 100);
        JSONObject jsonSubGoal2 = new JSONObject()
                                .put("goal", "OR")
                                .put("subgoals", jsonSubSubGoals);

        JSONArray jsonSubGoals = new JSONArray()
                                .put(jsonSubGoal1)
                                .put(jsonSubGoal2);
        JSONObject jsonGoal = new JSONObject()
                                .put("goal", "OR")
                                .put("subgoals", jsonSubGoals);
        //System.out.println(jsonGoal.toString());
        // Load goals into the world
        GoalLoader goalLoader = new GoalLoader();
        world.setWorldGoal(goalLoader.loadGoals(jsonGoal));
        assertFalse(world.reachedGoal());

        // Give the character exp and check that the goal has not been met yet
        Character character = world.getCharacter();
        character.gainXP(123456);
        assertTrue(world.reachedGoal());

        // Loop around the world
        for(int i = 0; i < 100; i++){
            world.addLoop();
        }
        // Check that the player has met the goals
        assertTrue(world.reachedGoal());

        String goalState = "X  LOOP: 101/100\n\n     OR\n\nX  EXP: 123456/123456\n     OR\nO  GOLD: 0/900000";
        assertEquals(goalState, world.goalsPrettyPrint());

    }


    /**
     * Test loading a world with goals to defeat bosses
     * @throws FileNotFoundException
     */
    @Test
    public void BossGoalLoaderTest() throws FileNotFoundException{
        BetterHelper h = new BetterHelper();
        LoopManiaWorld world = h.testWorld;

        // Create goals and set them in the world
        JSONObject jsonGoal = new JSONObject().put("goal", "bosses");
        GoalLoader goalLoader = new GoalLoader();
        world.setWorldGoal(goalLoader.loadGoals(jsonGoal));
        assertFalse(world.reachedGoal());
        String goalState = "O  BOSS: Kill all bosses";
        assertEquals(goalState, world.goalsPrettyPrint());

        // Set up the character
        Character character = world.getCharacter();
        for(int i = 0; i < 50; i++){
            character.addItem("Potion");
            world.addAlly(new Ally());
        }
        character.gainXP(300000);
        character.equip(h.testSword);
        character.equip(h.testShield);

        // Set up doggie boss
        Doggie doggie = new Doggie(new PathPosition(1, h.orderedPath));
        world.addBoss(doggie);
        assert(world.getAliveBosses().size() == 1);

        // Defeat doggie, didnt meet goal yet
        world.runBattle(world.findBattle());
        //world.cleanupBattle(defeatedEnemies);
        assertFalse(world.reachedGoal());

        // Set up elan boss
        ElanMuske elan = new ElanMuske(new PathPosition(1, h.orderedPath));
        elan.setCurrHP(50);
        world.addBoss(elan);

        // Defeat elan, met goal
        world.runBattle(world.findBattle());
        assertTrue(world.reachedGoal());

        goalState = "X  BOSS: Kill all bosses";
        assertEquals(goalState, world.goalsPrettyPrint());

    }

    /**
     * Load a world with complex goals and the boss goal 
     * @throws FileNotFoundException
     */
    @Test
    public void ComplexBossLoaderTest() throws FileNotFoundException{
        BetterHelper h = new BetterHelper();
        LoopManiaWorld world = h.testWorld;

        // Load complex goals 
        JSONObject jsonSubGoal = new JSONObject().put("goal", "experience").put("quantity", 1000);
        JSONObject jsonSubGoal2 = new JSONObject().put("goal", "bosses");
        JSONArray jsonSubgoals = new JSONArray().put(jsonSubGoal).put(jsonSubGoal2);
        JSONObject jsonGoal = new JSONObject().put("goal", "OR").put("subgoals",jsonSubgoals);
        GoalLoader goalLoader = new GoalLoader();
        world.setWorldGoal(goalLoader.loadGoals(jsonGoal));

        // Check the goal states
        String goalState = "O  EXP: 0/1000\n     OR\nO  BOSS: Kill all bosses";
        assertFalse(world.reachedGoal());
        assertEquals(goalState, world.goalsPrettyPrint());

        // Set up character and reach goal
        Character character = world.getCharacter();
        character.gainXP(1000);
        assertTrue(world.reachedGoal());
        goalState = "X  EXP: 1000/1000\n     OR\nO  BOSS: Kill all bosses";
        assertEquals(goalState, world.goalsPrettyPrint());
   
    }

    /**
     * Test a world with no goals
     * @throws FileNotFoundException
     */
    @Test 
    public void NullGoalTest() throws FileNotFoundException{
        BetterHelper h = new BetterHelper();
        LoopManiaWorld world = h.testWorld;

        assertFalse(world.reachedGoal());
        String goalState = "no goals yet!";
        assertEquals(goalState, world.goalsPrettyPrint());

    }



}
