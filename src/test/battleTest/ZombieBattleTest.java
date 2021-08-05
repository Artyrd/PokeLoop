package test.battleTest;

import org.junit.jupiter.api.Test;

import test.testHelpers.BasicHelper;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import unsw.loopmania.Ally;
import unsw.loopmania.Character;
import unsw.loopmania.enemies.Slug;
import unsw.loopmania.enemies.Zombie;
import unsw.loopmania.items.Staff;
import unsw.loopmania.status.CampfireBuff;

/**
 * Testing the battle behaviour interaction between a zombie, character and allies.
 * A zombie can basic attack, and can critical attack allies or character.
 * A zombie can only apply a Zombified status to allies in critical attacks.
 */
public class ZombieBattleTest {
    
    /**
     * Testing damage dealt and received by a zombie and character.
     */
    @Test
    void testZombieBattleAttack(){
        
        BasicHelper helper = new BasicHelper();
        // Set up character
        Character character = helper.testCharacter;
        assertTrue(character.getCurrHP() == 100);
        assertTrue(character.getCurrATK() == 2);
        assertTrue(character.getCurrDEF() == 1);
        
        // Set up zombie
        Zombie zombie = helper.testZombie;
        assertTrue(zombie.getCurrHP() == 40);
        assertTrue(zombie.getCurrATK() == 8);
        assertTrue(zombie.getCurrDEF() == 1);

        zombie.setCritChance(0);
        character.setCritChance(0);

        // Attack character
        zombie.attack(character, null, null);
        assertTrue(character.getCurrHP() == 93);    // Zombie has ATK of 8, character has DEF of 1
        assertTrue(character.getCurrATK() == 2);
        assertTrue(character.getCurrDEF() == 1);

        // Attack zombie
        character.attack(zombie, null, null);
        assertTrue(zombie.getCurrHP() == 39);      // Character has ATK of 2, zombie has DEF of 1
        assertTrue(zombie.getCurrATK() == 8);
        assertTrue(zombie.getCurrDEF() == 1);

        zombie.attack(character, null, null);
        assertTrue(character.getCurrHP() == 86);
        
    }

    /**
     * Test the effects of campfire on the zombie
     */
    @Test
    void testCampfireBuffZombie() {

        BasicHelper helper = new BasicHelper();
        // Set up character and zombie
        Character character = helper.testCharacter;
        character.receiveStatus(new CampfireBuff());
        Zombie zombie = helper.testZombie;

        zombie.setCritChance(0);
        character.setCritChance(0);

        // Character deals more damage to zombies 
        character.attack(zombie, null, null);
        System.err.println(zombie.getCurrHP());
        assertTrue(zombie.getCurrHP() == 37);

    }

    /**
     * Testing damage dealt and received when a zombie critically attacks
     */
    @Test
    void testZombieBattleCrit(){
        
        // Set up character, enemy and allies
        BasicHelper helper = new BasicHelper();
        Character character = helper.testCharacter;
        Zombie zombie = helper.testZombie;
        Ally ally = helper.testAlly;

        // Check ally stats
        assertTrue(ally.isFriendly());
        assertTrue(ally.getCurrHP() == 50);  
        
        // Critically attack ally and zombify them 
        zombie.critAttack(ally, null, null);
        assertTrue(!ally.isFriendly());
        assertTrue(ally.getStatusIDs().contains("Zombified"));
        assertTrue(ally.getCurrHP() == 39);     // Crit ATK of zombie is 12, ally has def of 1

        // Critically attack the ally, does not zombify
        zombie.critAttack(character, null, null);
        assertTrue(character.isFriendly());
        assertTrue(character.getCurrHP() == 89);

        // Critically attack the ally 
        zombie.critAttack(ally, null, null);
        assertTrue(!ally.isFriendly());
        assertTrue(ally.getCurrHP() == 28);

    }

    /**
     * Testing zombified debuffs cannot be applied to an enemy when zombies are tranced
     */
    @Test
    void testZombifyEnemy(){
        
        // Set up character and enemies
        BasicHelper helper = new BasicHelper();
        Character character = helper.testCharacter;
        Zombie zombie = helper.testZombie;
        Slug slug = helper.testSlug;
        Staff staff = helper.testStaff;

        // Critically attack and trance the zombie
        character.equip(staff);
        character.critAttack(zombie, null, null);
        assertTrue(zombie.getStatusIDs().contains("Tranced"));

        // If a tranced zombie crits an enemy, the enemy will not be zombified
        zombie.critAttack(slug, null, null);
        assertFalse(slug.getStatusIDs().contains("Zombified"));  

    }

    /**
     * Test zombifying effect
     */
    @Test
    void testAllyZombify(){
        
        // Set up zombies and allies
        BasicHelper helper = new BasicHelper();
        Zombie zombie = helper.testZombie;
        Ally ally1 = helper.testAlly;
        Ally ally2 = new Ally();

        // A critical attack to allies should zombify them and apply a zombified status
        zombie.critAttack(ally1, null, null);
        assertTrue(ally1.getStatusIDs().contains("Zombified"));
        
        ally1.critAttack(ally2, null, null);
        assertTrue(ally2.getStatusIDs().contains("Zombified"));

    }

    /**
     * Test zombie cannot be zombified
     */
    @Test
    void testImmuneZombify(){
        
        // Set up zombies and allies
        BasicHelper helper = new BasicHelper();
        Zombie zombie = helper.testZombie;
        Zombie zombie2 = new Zombie(helper.testPosition);

        // A critical attack to allies should zombify them and apply a zombified status
        zombie.critAttack(zombie2, null, null);
        assertTrue(!zombie.getStatusIDs().contains("Zombified"));

    }

}
