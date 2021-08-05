package test.stageBossTest;

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
import unsw.loopmania.StageBossManager;
import unsw.loopmania.battle.BattleSimulator;
import unsw.loopmania.buildings.StageBossBuilding;
import unsw.loopmania.cards.StageBossCard;
import unsw.loopmania.enemies.Doggie;
import unsw.loopmania.enemies.ElanMuske;
import unsw.loopmania.enemies.Enemy;
import unsw.loopmania.enemies.StageBoss;
import unsw.loopmania.enemies.StageBossPet;

public class StageBossManagerTest {
    
    @Test
    public void StageBossJSONTest() throws FileNotFoundException{
        BetterHelper helper = new BetterHelper();
        LoopManiaWorld world = helper.testWorld;
        Character character = helper.testCharacter;

        // Create a simple experience goal
       JSONObject jsonBossStats = new JSONObject()
                                .put("hp", 10)
                                .put("atk", 1)
                                .put("def", 1)
                                .put("gold", 222)
                                .put("xp", 222);
        JSONObject jsonPetStats = new JSONObject()
                                .put("hp", 10)
                                .put("atk", 2)
                                .put("def", 5);
        JSONObject jsonChallengeGoal = new JSONObject()
                                .put("goal", "cycles")
                                .put("quantity", 1);
        JSONObject jsonStageBoss = new JSONObject()
                                .put("boss_name", "Brock")
                                .put("battle_status", "")
                                .put("boss_stats", jsonBossStats)
                                .put("pet_name", "Onix")
                                .put("pet_stats", jsonPetStats)
                                .put("challenge-condition", jsonChallengeGoal);

        // Load the goal into the world
        StageBossManager stageBossManager = new StageBossManager(jsonStageBoss);

        // we are on loop 1
        assertTrue(stageBossManager.canChallenge(character, world));

        StageBossBuilding gym = (StageBossBuilding) stageBossManager.giveCard().getBuilding();
        gym.changeIntegerProperty(new SimpleIntegerProperty(1), new SimpleIntegerProperty(1));
        StageBoss boss = gym.createStageBoss(world);
        StageBossPet pet = gym.createStageBossPet(world);
        world.addBoss(boss);
        world.addBoss(pet);
        assertEquals(boss.getCurrHP(), 10, 0.1);

        // show ChallengeGoal
        String challengeGoal = "X  LOOP: 1/1";
        assertEquals(challengeGoal, stageBossManager.showChallengeGoal(character, world));
        
        // show stageboss goal
        String stageBossGoal = "O  GYM: Defeat Brock";
        assertEquals(stageBossGoal, stageBossManager.showStageBossGoal(character, world));

        // defeatedStageBoss is false
        assertFalse(stageBossManager.defeatedStageBossGoal(world));

        BattleSimulator battle = world.findBattle();
        world.runBattle(battle);
        assertTrue(stageBossManager.defeatedStageBossGoal(world));

    }
}