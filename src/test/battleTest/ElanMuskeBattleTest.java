package test.battleTest;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.ArrayList;

import org.junit.Test;

import test.testHelpers.BasicHelper;
import unsw.loopmania.enemies.ElanMuske;
import unsw.loopmania.enemies.Slug;
import unsw.loopmania.enemies.Zombie;
import unsw.loopmania.Character;
import unsw.loopmania.MovingEntity;


public class ElanMuskeBattleTest {
    
    /**
     * Test Elan's healing of enemy allies
     */
    @Test
    public void testHealNPC(){
        BasicHelper helper = new BasicHelper();
        // Set up character, enemies and elan
        Character character = helper.testCharacter;
        Zombie zombie = helper.testZombie;
        ElanMuske elanMuske = helper.testElanMuske;
        character.setCritChance(0);
        zombie.setCritChance(0);

        // Set up enemy
        ArrayList<MovingEntity> enemies = new ArrayList<MovingEntity>();
        enemies.add(elanMuske);
        enemies.add(zombie);

        // Set up ally
        ArrayList<MovingEntity> allies = new ArrayList<MovingEntity>();
        allies.add(character);

        // Attack enemies
        character.attack(zombie, allies, enemies);
        character.attack(zombie, allies, enemies);
        character.attack(zombie, allies, enemies);
        assertTrue(zombie.getCurrHP() == 37);

        // Elan critical attack heals allies
        elanMuske.critAttack(character, enemies, allies);
        assertTrue(zombie.getCurrHP() == 40);
    }
    
    /**
     * Test elan healing a dead enemy ally
     */
    @Test
    public void testHealDeadNPC(){
        BasicHelper helper = new BasicHelper();
        // Set up the character, enemies and elan
        Character character = helper.testCharacter;
        Zombie zombie = helper.testZombie;
        ElanMuske elanMuske = helper.testElanMuske;
        Slug slug = helper.testSlug;
        character.setCritChance(0);
        zombie.setCritChance(0);

        // Set up enemies
        ArrayList<MovingEntity> enemies = new ArrayList<MovingEntity>();
        enemies.add(elanMuske);
        enemies.add(zombie);
        enemies.add(slug);

        // Set up allies
        ArrayList<MovingEntity> allies = new ArrayList<MovingEntity>();
        allies.add(character);

        // Attack enemies
        for(int atk = 0; atk < 40; atk++){
            character.attack(zombie, allies, enemies);
        }
        assertTrue(zombie.getCurrHP() == 0);
        character.attack(slug, allies, enemies);
        assertTrue(slug.getCurrHP() == 10);

        // Check that elan does not heal dead enemies, only live ones
        elanMuske.critAttack(character, enemies, allies);
        assertTrue(zombie.getCurrHP() == 0);
        assertTrue(slug.getCurrHP() == 12);
    }

    /**
     * Test elans healing low health
     */
    @Test
    public void testHealLowHealth(){
        BasicHelper helper = new BasicHelper();
        // Set up character, enemies and elan
        Character character = helper.testCharacter;
        ElanMuske elanMuske = helper.testElanMuske;
        Slug slug = helper.testSlug;
        character.setCritChance(0);

        // Set up enemies
        ArrayList<MovingEntity> enemies = new ArrayList<MovingEntity>();
        enemies.add(elanMuske);
        enemies.add(slug);

        // Set up allies 
        ArrayList<MovingEntity> allies = new ArrayList<MovingEntity>();
        allies.add(character);

        // Attack enemies
        for(int atk = 0; atk < 4; atk++){
            character.attack(slug, allies, enemies);
        }
        assertTrue(slug.getCurrHP() == 4);

        // Test elans healing
        elanMuske.critAttack(character, enemies, allies);
        assertTrue(slug.getCurrHP() == 7.6);
    }

    /**
     * Test elan's immunity to status effects
     */
    @Test
    public void testImmunity(){
        BasicHelper helper = new BasicHelper();
        // Set up character, enemies and elan
        Character character = helper.testCharacter;
        character.equip(helper.testStaff);
        ElanMuske elanMuske = helper.testElanMuske;
        Zombie zombie = helper.testZombie;

        // Critically attack elan, check he rejects trancing
        character.critAttack(elanMuske, null, null);
        assertFalse(elanMuske.getStatusIDs().contains("Tranced"));

        // Critically attack elan, check he rejects zombification
        zombie.critAttack(elanMuske, null, null);
        assertFalse(elanMuske.getStatusIDs().contains("Zombified"));

    }

}
