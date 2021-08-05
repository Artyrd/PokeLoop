package test.battleTest;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import test.testHelpers.BasicHelper;
import unsw.loopmania.enemies.Slug;
import unsw.loopmania.enemies.Vampire;
import unsw.loopmania.items.Staff;
import unsw.loopmania.status.CampfireBuff;
import unsw.loopmania.Character;

/**
 * Testing the battle behaviour interaction between a vampire, character and allies.
 * A vampire can basic attack, and can critical attack allies or character to
 * apply a VampireDebuff. 
 */
public class VampireBattleTest {
    
    /**
     * Testing damage dealt and received by a vampire and character.
     */
    @Test
    public void testVampireBattleAttack(){
        BasicHelper helper = new BasicHelper();
        // Set up character and vampire
        Character character = helper.testCharacter;
        Vampire vampire = helper.testVampire;
        
        // Check character Stats
        assertTrue(character.getCurrHP() == 100);
        assertTrue(character.getCurrATK() == 2);
        assertTrue(character.getCurrDEF() == 1);

        // Check vampire stats
        assertTrue(vampire.getCurrHP() == 120);
        assertTrue(vampire.getCurrATK() == 16);
        assertTrue(vampire.getCurrDEF() == 3);

        character.setCritChance(0);
        vampire.setCritChance(0);

        // Attack character
        vampire.attack(character, null, null);
        assertTrue(character.getCurrHP() == 85);    // Vampire has ATK of 16, character has DEF of 1
        assertTrue(vampire.getCurrHP() == 120);

        // Attack vampire
        character.attack(vampire, null, null);
        assertTrue(vampire.getCurrHP() == 120);    // Character has ATK of 2, but vampire has DEF of 3
        assertTrue(character.getCurrHP() == 85);

    }

    /**
     * Test the effects of the campfire on the vampire
     */
    @Test
    public void testCampfireBuffVampire() {

        BasicHelper helper = new BasicHelper();
        // Set up character and vampire
        Character character = helper.testCharacter;
        character.receiveStatus(new CampfireBuff());
        Vampire vampire = helper.testVampire;
        vampire.setCritChance(0);
        character.setCritChance(0);

        // test that the character deals more damage to the vampire under campfire
        character.attack(vampire, null, null);
        System.err.println(vampire.getCurrHP());
        assertTrue(vampire.getCurrHP() == 119);

    }

    /**
     * Testing debuff a vampire will deal when critically attacking
     */
    @Test
    public void testVampireCritAttack(){
        BasicHelper helper = new BasicHelper();
        Character character = helper.testCharacter;
        Vampire vampire = helper.testVampire;
        
        assertEquals(character.getCurrHP(), 100, 0.001); // 

        vampire.setCritChance(0);
        // vampires normally do 16atk
        vampire.attack(character, null, null);
        assertEquals(character.getCurrHP(), 85, 0.001); // 

        // heal character back up
        character.setCurrHP(100);

        vampire.critAttack(character, null, null);
        assertTrue(character.getStatusIDs().contains("VampireDebuff"));
        assertTrue(character.getCurrHP() <= 77);

        // heal character back up
        character.setCurrHP(100);

        vampire.attack(character, null, null);
        assertTrue(character.getStatusIDs().contains("VampireDebuff"));
        assertTrue(character.getCurrHP() < 85); // should be dealt more damage
    }

    /**
     * Testing vampire debuffs cannot be applied to an enemy when vampires are tranced
     */
    @Test
    public void testVampireCritAttackEnemy(){
        BasicHelper helper = new BasicHelper();
        // Set up character and enemies
        Character character = helper.testCharacter;
        Vampire vampire = helper.testVampire;
        Slug slug = helper.testSlug;
        Staff staff = helper.testStaff;

        // trance the vampire
        character.equip(staff);
        character.critAttack(vampire, null, null);
        assertTrue(vampire.getStatusIDs().contains("Tranced"));

        // A tranced vampire cannot apply a debuff to enemies
        vampire.critAttack(slug, null, null);
        assertFalse(slug.getStatusIDs().contains("VampireDebuff"));

    }

}
