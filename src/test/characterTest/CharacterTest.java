package test.characterTest;

import org.junit.jupiter.api.Test;

import test.testHelpers.BasicHelper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import unsw.loopmania.enemies.Slug;
import unsw.loopmania.Character;

public class CharacterTest {

    /**
     * Tests if the Character moves correctly in a straight line
     */
    @Test
    void testCharacterStraightMovement() {
        BasicHelper t = new BasicHelper();
        Character hero = t.testCharacter;
        assertTrue(hero.getX() == 1);
        for (int loop = 1; loop < t.pathLength; loop++) {
            assertTrue(hero.getX() == loop);
            hero.move();
        }
    }
    /**
     * Tests if the Character moves correcly in a simple loop
     */
    @Test
    void testCharacterLoopMovement() {
        BasicHelper t = new BasicHelper();
        t.LoadBasicLoopWorld();
        Character hero = t.testCharacter;

        assertTrue(hero.getX() == 1);
        assertTrue(hero.getY() == 1);
        // loop once
        for (int loop = 0; loop < t.totalLoopLength; loop++) {
            hero.move();
        }
        assertTrue(hero.getX() == 1);
        assertTrue(hero.getY() == 1);
        // loop again
        for (int loop = 0; loop < t.totalLoopLength; loop++) {
            hero.move();
        }
        assertTrue(hero.getX() == 1);
        assertTrue(hero.getY() == 1);
    }

    /**
     * Tests the correct stat increases of the Character
     */
    @Test
    void testCharacterLevelUp() {
        BasicHelper t = new BasicHelper();
        Character hero = t.testCharacter;
        // assert correct base stats at level 1
        assertEquals(hero.getLevel() , 1);
        assertEquals(hero.getMaxHP() , 100);
        assertEquals(hero.getBaseATK() , 2);
        assertEquals(hero.getBaseDEF() , 1);
        // Character needs 100xp to level up from lvl1 -> lvl2
        hero.gainXP(100);
        assertEquals(hero.getLevel() , 2);
        assertEquals(hero.getMaxHP() , 105);
        assertEquals(hero.getBaseATK() , 2.5);
        assertEquals(hero.getBaseDEF() , 1.3);
        // Character needs 100xp to level up from lvl2 -> lvl3
        hero.gainXP(150);
        assertEquals(hero.getLevel() , 3);
        assertEquals(hero.getMaxHP() , 110);
        assertEquals(hero.getBaseATK() , 3.0);
        assertEquals(hero.getBaseDEF(), 1.6);
        // Character has cummulatively 2700XP at lvl 10
        hero.gainXP(2700);
        assertEquals(hero.getLevel(), 10);
        assertEquals(hero.getMaxHP(), 145, 0.001);
        assertEquals(hero.getBaseATK(), 6.5, 0.001);
        assertEquals(hero.getBaseDEF(), 3.7, 0.001);
    }

    /**
     * test Character attacking and receiving damage
     */
    @Test
    void testCharacterBattleBasic() {
        // Initialise character
        BasicHelper t = new BasicHelper();
        Character hero = t.testCharacter;
        assertTrue(hero.getCurrHP() == 100);
        assertTrue(hero.getCurrATK() == 2);
        assertTrue(hero.getCurrDEF() == 1);
        // Initialise slug
        Slug slug = t.testSlug;
        assertTrue(slug.getCurrHP() == 12);
        assertTrue(slug.getCurrATK() == 4);
        assertEquals(slug.getCurrDEF(), 0);

        // Basic battle
        hero.setCritChance(0);
        hero.attack(slug, null, null);
        assertEquals(10, slug.getCurrHP());
        slug.attack(hero, null, null);
        assertEquals(97, hero.getCurrHP()); // slug dealt 1 less damage bc of hero's 1 DEF
        assertEquals(100, hero.getMaxHP()); // make sure max hp didnt change
    }
    
    /**
     * Test equipping the character with a Sword and attacking
     */
    @Test
    void testCharacterEquipSwordBattle() {
        BasicHelper t = new BasicHelper();
        Character hero = t.testCharacter;
        assertTrue(hero.getCurrATK() == 2);
        hero.equip(t.testSword); // sword gives +5 ATK
        assertTrue(hero.getCurrATK() == 7);
        Slug slug = t.testSlug;
        assertTrue(slug.getCurrHP() == 12);

        // setting crit chance to 0 to test basic attack
        hero.setCritChance(0);
        hero.attack(slug, null, null);
        assertEquals(5, slug.getCurrHP());
    }
    /**
     * Test equipping the character with different items and having the stat change
     */
    @Test
    void testCharacterChangeEquipWeapon() {
        BasicHelper t = new BasicHelper();
        Character hero = t.testCharacter;
        assertTrue(hero.getCurrATK() == 2);
        hero.equip(t.testSword); // sword gives +5 ATK
        assertTrue(hero.getCurrATK() == 7);
        hero.equip(t.testStaff); // staff gives +1 ATK
        assertTrue(hero.getCurrATK() == 3);
    }

    /**
     * Testing Character gaining gold
     */
    @Test
    void testCharacterGetGold() {
        BasicHelper t = new BasicHelper();
        Character hero = t.testCharacter;
        assertEquals(0, hero.getGold());
        hero.addItem(t.tenGold);
        assertEquals(10, hero.getGold());
    }
    /**
     * Testing Character gaining and using potions
     */
    @Test
    void testCharacterGetPotion() {
        BasicHelper t = new BasicHelper();
        Character hero = t.testCharacter;
        assertEquals(0, hero.getPotions());
        hero.addItem(t.testPotion);
        assertEquals(1, hero.getPotions());
        // potions restore 30 hp
        assertEquals(hero.getCurrHP(), 100, 0.001);
        hero.reduceHP(30);
        assertEquals(hero.getCurrHP(), 70, 0.001);
        hero.usePotion();
        assertEquals(0, hero.getPotions());
        assertEquals(hero.getCurrHP(), 100, 0.001);
    }

    @Test
    void testCharacterNoPotion() {
        BasicHelper t = new BasicHelper();
        Character hero = t.testCharacter;
        assertEquals(0, hero.getPotions());
        assertEquals(hero.getCurrHP(), 100, 0.001);
        assertEquals(hero.usePotion(),0);
        assertEquals(0, hero.getPotions());
        assertEquals(hero.getCurrHP(), 100, 0.001);
    }


}
