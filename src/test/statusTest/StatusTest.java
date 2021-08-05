package test.statusTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

import javafx.beans.property.SimpleIntegerProperty;
import test.testHelpers.BasicHelper;
import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.buildings.CampfireBuilding;
import unsw.loopmania.status.Confused;
import unsw.loopmania.status.Frozen;
import unsw.loopmania.status.Paralysed;
import unsw.loopmania.status.Poisoned;
import unsw.loopmania.status.Sleeping;
import unsw.loopmania.status.Status;
import unsw.loopmania.status.Stunned;

public class StatusTest {

    /**
     * Test that when the staff successfully trances an enemy, that 
     * the enemy receives the tranced behaviour
     */
    @Test 
    public void tranceTest() {
        BasicHelper help = new BasicHelper();

        help.testCharacter.equip(help.testStaff);

        help.testCharacter.critAttack(help.testSlug, null, null);
        assertTrue(help.testSlug.getStatusIDs().contains("Tranced"));
        help.testCharacter.critAttack(help.testZombie, null, null);
        assertTrue(help.testZombie.getStatusIDs().contains("Tranced"));
        help.testCharacter.critAttack(help.testVampire, null, null);
        assertTrue(help.testVampire.getStatusIDs().contains("Tranced"));
    }

    /**
     * Test that when the vampire critically attacks the character,
     * applies the vampire debuff to the character
     * Test that when the vampire attacks the character normally thereafter,
     * that they do bonus damage
     */
    @Test
    public void vampireDebuffTest() {
        BasicHelper help = new BasicHelper();

        help.testCharacter.setMaxHP(100);
        help.testCharacter.setCurrHP(100);
        help.testCharacter.setCurrDEF(0);
        help.testVampire.setCritChance(0);

        // 0 armor char means vamp does the full 16 dmg
        assertEquals(help.testVampire.attack(help.testCharacter, null, null), 16, 0.01);
        
        assertEquals(help.testCharacter.getCurrHP(), 84, 0.1);

        help.testVampire.critAttack(help.testCharacter, null, null); // debuff should be applied
        assertTrue(help.testCharacter.getStatusIDs().contains("VampireDebuff"));
        help.testCharacter.setCurrHP(100); // heal the character back...

        // 0 armor char means vamp does the full 16 dmg
        assertTrue(help.testVampire.attack(help.testCharacter, null, null) == 16); // vampire's attack still does the normal damage... BUT
        assertTrue(help.testCharacter.getCurrHP() < 84);         // character should have been dealt extra damage from the debuff
    
    }

    /**
     * Test that when a zombie critically attacks an ally, 
     * they become zombified and a zombified status is applied
     */
    @Test
    public void zombifiedTest() {
        BasicHelper help = new BasicHelper();

        help.testZombie.critAttack(help.testAlly, null, null);
        assertTrue(help.testAlly.getStatusIDs().contains("Zombified"));
        assertTrue(help.testAlly.isFriendly() == false);
    }

    /**
     * Test that when a zombie is tranced, they return back to being tranced after
     */
    @Test
    public void rezombifiedTest() {
        BasicHelper help = new BasicHelper();

        // Zombify an ally
        help.testZombie.critAttack(help.testAlly, null, null);
        assertTrue(help.testAlly.getStatusIDs().contains("Zombified"));
        assertTrue(help.testAlly.isFriendly() == false);

        // Equip a staff and trance the ex-ally zombie
        help.testCharacter.equip(help.testStaff);
        help.testCharacter.critAttack(help.testAlly, null, null);
        assertTrue(help.testAlly.getStatusIDs().contains("Tranced"));

        // Naturally decrease the tranced status effect to end
        for(int tick = 0; tick < 4; tick++){
            help.testAlly.getStatusByID("Tranced").applyStatus(help.testAlly);
        }

        // Check that the ex-ally zombie is no longer tranced but is back to being a zombie
        assertFalse(help.testAlly.getStatusIDs().contains("Tranced"));
        assertTrue(help.testAlly.getStatusIDs().contains("Zombified"));
        assertTrue(help.testAlly.isFriendly() == false);

    }

    /**
     * Test that a campfire buffs the character
     */
    @Test
    public void campfireBuff(){
        
        BasicHelper h = new BasicHelper();
        LoopManiaWorld testWorld = new LoopManiaWorld(12, 12, h.orderedPath);
        CampfireBuilding cf = new CampfireBuilding(new SimpleIntegerProperty(11), new SimpleIntegerProperty(11));
        h.LoadBasicLoopWorld();
        testWorld.setCharacter(h.testCharacter);
        testWorld.addBuilding(cf);

        // make the character walk into the battle radius of the campfire
        for (int tick = 0; tick < 20; tick++) {
            testWorld.runTickMoves();
            testWorld.updateBuildings();
        }

        // check the character has the campfire buff
        assertTrue(h.testCharacter.getStatusIDs().contains("CampfireBuff"));

        // make sure the buff doesnt get overwriten
        h.testVampire.critAttack(h.testCharacter, null, null);
        assertTrue(h.testCharacter.getStatusIDs().contains("VampireDebuff"));
        assertTrue(h.testCharacter.getStatusIDs().contains("CampfireBuff"));

        for(int tick = 0; tick < 7; tick++){
            Status vampdebuff = h.testCharacter.getStatusByID("VampireDebuff");
            if (vampdebuff != null) vampdebuff.applyStatus(h.testCharacter);
        }

        // campfire debuff still there after vampdebuff expires
        assertFalse(h.testCharacter.getStatusIDs().contains("VampireDebuff"));
        assertTrue(h.testCharacter.getStatusIDs().contains("CampfireBuff"));

    }
    
    /**
     * Test the burn status effect of Anduril
     */
    @Test
    public void testBurned() {
        BasicHelper help = new BasicHelper();

        help.testCharacter.equip(help.testAnduril);
        help.testCharacter.setCritChance(0);
        assertEquals(300, help.testDoggie.getCurrHP());
        
        // Critically attack the enemy and apply a burned effect
        assertEquals(73.5, help.testCharacter.critAttack(help.testDoggie, null, null));
        assertTrue(226.5 > help.testDoggie.getCurrHP());
        
        // Check that the status has been applied
        assertEquals(1, help.testDoggie.getStatusList().size());
        assertEquals("Burned", help.testDoggie.getStatusIDs().get(0));

        // Apply one tick of the burned status
        Status burned = help.testDoggie.getStatusList().get(0);
        help.testDoggie.setCurrHP(100);
        burned.applyStatus(help.testDoggie);
        // Burn status should deal between 1-5 damage
        assertTrue(help.testDoggie.getCurrHP() < 99.5);
        assertTrue(help.testDoggie.getCurrHP() > 94.5);
    }

    /**
     * Test the poison effect on the character
     */
    @Test
    public void testPoison() {
        BasicHelper help = new BasicHelper();

        assertEquals(100, help.testCharacter.getCurrHP());
        // Poison the character
        Poisoned status = new Poisoned();
        help.testCharacter.receiveStatus(status);

        // Poison deals 1-5 damage to the character every application
        assertTrue(help.testCharacter.getCurrHP() < 100);
        System.err.println(help.testCharacter.getCurrHP());
        assertTrue(help.testCharacter.getCurrHP() > 94);
        
    }

    /**
     * Test stunning status effects from stage bosses (and its branches) on the character
     */
    @Test
    public void testStunning() {
        BasicHelper help = new BasicHelper();

        // Set up stunned statuses
        Stunned stunned = new Stunned();
        Paralysed paralysed = new Paralysed();
        Sleeping sleeping = new Sleeping();
        Frozen frozen = new Frozen();

        // Check that when afflicted by any of the above statuses, the character cannot attack
        help.testCharacter.receiveStatus(stunned);
        assertEquals(0, help.testCharacter.attack(help.testSlug, null, null));
        stunned.endStatus(help.testCharacter);

        help.testCharacter.receiveStatus(paralysed);
        assertEquals(0, help.testCharacter.attack(help.testSlug, null, null));
        paralysed.endStatus(help.testCharacter);

        help.testCharacter.receiveStatus(sleeping);
        assertEquals(0, help.testCharacter.attack(help.testSlug, null, null));
        sleeping.endStatus(help.testCharacter);
        
        help.testCharacter.receiveStatus(frozen);
        assertEquals(0, help.testCharacter.attack(help.testSlug, null, null));
        frozen.endStatus(help.testCharacter);
    }

    /**
     * Test confusion status effects from stage bosses on allies
     */
    @Test
    public void testConfusion() {
        BasicHelper help = new BasicHelper();

        // Check that when confused, the ally is no longer friendly
        Confused confused = new Confused();
        help.testAlly.receiveStatus(confused);
        assertEquals(false, help.testAlly.isFriendly());
    }

}
