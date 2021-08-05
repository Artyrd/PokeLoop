package test.battleTest;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;


import org.junit.Test;

import test.testHelpers.BetterHelper;
import unsw.loopmania.enemies.Slug;
import unsw.loopmania.enemies.Vampire;
import unsw.loopmania.enemies.Zombie;
import unsw.loopmania.Dragon;
import unsw.loopmania.MovingEntity;

/**
 * Testing the battle behaviour interaction between a dragon, character and allies.
 * A dragon can basic attack, and can critical attack allies or character to
 * apply a Burned. 
 */
public class DragonBattleTest {
    
    /**
     * Testing damage dealt and received by a dragon
     */
    @Test
    public void testDragonBattleAttack(){
        BetterHelper helper = new BetterHelper();
        Dragon dragon = helper.testDragon;
        Vampire vampire = helper.testVampire;
        
        // Check stats
        assertTrue(vampire.getCurrHP() == 120);
        assertTrue(vampire.getCurrATK() == 16);
        assertTrue(vampire.getCurrDEF() == 3);

        assertTrue(dragon.getCurrHP() == 200);
        assertTrue(dragon.getCurrATK() == 15);
        assertTrue(dragon.getCurrDEF() == 5);

        vampire.setCritChance(0);
        dragon.setCritChance(0);

        dragon.attack(vampire, null, null);
        assertTrue(vampire.getCurrHP() == (120 - 15 + 3));    // Dragon has ATK of 15, vampire has DEF of 3

        vampire.attack(dragon, null, null);
        assertTrue(dragon.getCurrHP() == (200 - 16 + 5));    // Vampire has ATK of 16, but dragon has DEF of 5

    }

    /**
     * Testing a dragon's burn when crit attacking
     */
    @Test
    public void testDragonCritAttack(){
        BetterHelper helper = new BetterHelper();
        Vampire vampire = helper.testVampire;
        Dragon dragon = helper.testDragon;
        
        assertEquals(vampire.getCurrHP(), 120, 0.001); // 

        // Critically attack the vampire and apply burn
        dragon.critAttack(vampire, null, null);
        assertTrue(vampire.getStatusIDs().contains("Burned"));
        // shouldve been damaged more than the cirt attack
        assertTrue(vampire.getCurrHP() < (120 - (15*1.5-3)));

        // heal vampire back up
        vampire.setCurrHP(120);

        double damage = dragon.attack(vampire, null, null);
        assertTrue(vampire.getStatusIDs().contains("Burned"));
        assertEquals(damage, 15-3, 0.1);
        assertTrue(vampire.getCurrHP() <= (120 - (15 -3))); // burn doesnt burn on attack
        vampire.getStatusList().get(0).battleTick(vampire);
        assertTrue(vampire.getCurrHP() < (120 - (15 -3))); // burn damages on battle tick 

    }
    /**
     * Testing a dragon's eating,
     * Restoring hp + gaining states
     */
    @Test
    public void testDragonEating(){
        BetterHelper helper = new BetterHelper();
        // Set up enemies and dragon
        Vampire vampire = helper.testVampire;
        Slug slug = helper.testSlug;
        Dragon dragon = helper.testDragon;
        dragon.setCritChance(0);

        // Check dragon stats
        assertTrue(dragon.getMaxHP() == 200);
        assertTrue(dragon.getCurrATK() == 15);
        assertTrue(dragon.getCurrDEF() == 5);
        
        dragon.setCurrHP(10);

        vampire.setCurrHP(1);
        dragon.critAttack(vampire, null, null);
        // dragon heals equal to half of its target's max hp
        assertEquals(dragon.getCurrHP(), 10 + vampire.getMaxHP()/2, 0.1);
        // and gain 10% of the target's stats
        assertEquals(dragon.getMaxHP(), 200 + vampire.getMaxHP()/10, 0.1);
        assertEquals(dragon.getCurrATK(), 15 + vampire.getCurrATK()/10, 0.1);
        assertEquals(dragon.getCurrDEF(), 5 + vampire.getCurrDEF()/10, 0.1);

        // should eat on crit attack too
        dragon.setCurrHP(10);
        slug.setCurrHP(1);
        dragon.critAttack(slug, null, null);
        assertEquals(dragon.getCurrHP(), 10 + slug.getMaxHP()/2, 0.1);
    }

    /**
     * Testing a dragon's attack preference,
     * Smallest curr HP, then Samllest max HP
     */
    @Test
    public void testDragonPreference(){
        BetterHelper helper = new BetterHelper();
        // Set up enemies and dragon
        Vampire vampire = helper.testVampire;
        Slug slug = helper.testSlug;
        Zombie zombie = helper.testZombie;
        Dragon dragon = helper.testDragon;

        // Create a list of targets
        ArrayList<MovingEntity> targets = new ArrayList<MovingEntity>();
        targets.add(vampire);
        targets.add(zombie);
        targets.add(slug);
        // Dragon will target slug since its the smallest curr hp
        assertTrue(dragon.getAttackPreference(targets).equals(slug));

        zombie.setCurrHP(5);
        slug.setCurrHP(5);
        // Dragon will still target slug since its the smallest curr hp AND smallest max hp
        assertTrue(dragon.getAttackPreference(targets).equals(slug));

        vampire.setCurrHP(1);
        // Dragon will target vampire, since its the smallest curr hp despite highest max hp
        assertTrue(dragon.getAttackPreference(targets).equals(vampire));

    }
}
