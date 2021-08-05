package test.itemsTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


import org.junit.jupiter.api.Test;

import test.testHelpers.BasicHelper;
import unsw.loopmania.items.TheOneRing;

public class TheOneRingTest {

    /**
     * Test that the one ring revives the character upon taking fatal damage
     */
    @Test
    public void reviveTest() {

        BasicHelper help = new BasicHelper();
        // give the ring...
        help.testCharacter.equip(new TheOneRing());
        help.testVampire.setCritChance(0); 
        assertEquals(help.testCharacter.getCurrHP(), 100, 0.1); // lvl 1 char at full hp

        // vampires have 16ATK, char DEF = 1 => 15dmg each attack
        // takes atleast 7 attacks to kill character
        for (int attack = 0; attack < 7; attack++) {
            help.testVampire.attack(help.testCharacter, null, null);
        }
        // character should revive with full hp
        assertEquals(help.testCharacter.getCurrHP(), 100, 0.01);

        // kill him again lol
        for (int attack = 0; attack < 7; attack++) {
            help.testVampire.attack(help.testCharacter, null, null);
        }
        // character should stay dead
        assertEquals(help.testCharacter.getCurrHP(), 0, 0.01);
    }

    /**
     * Test that the one ring doesnt activate on a non faltal damage
     */
    @Test
    public void noReviveTest() {

        BasicHelper help = new BasicHelper();
        // give the ring...
        TheOneRing ring = new TheOneRing();
        help.testCharacter.equip(ring);
        help.testVampire.setCritChance(0); 
        assertEquals(help.testCharacter.getCurrHP(), 100, 0.1); // lvl 1 char at full hp

        // vampires have 16ATK, char DEF = 1 => 15dmg each attack
        // takes atleast 7 attacks to kill character
        for (int attack = 0; attack < 6; attack++) {
            help.testVampire.attack(help.testCharacter, null, null);
        }
        ring.revive(help.testCharacter);
        // character shouldnt revive with full hp
        assertTrue(help.testCharacter.getCurrHP() < 100);
    }



}
