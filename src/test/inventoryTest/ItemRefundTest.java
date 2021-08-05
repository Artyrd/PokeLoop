package test.inventoryTest;

import org.junit.jupiter.api.Test;

import test.testHelpers.BasicHelper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import unsw.loopmania.items.Helmet;
import unsw.loopmania.items.Potion;
import unsw.loopmania.items.Sword;


import unsw.loopmania.Character;

public class ItemRefundTest {

    /**
     * Test character gaining gold and xp from excess items
     */
    @Test
    void testCantHoldAllTheseSwords() {
        BasicHelper h = new BasicHelper();
        Character hero = h.testCharacter;
        assertEquals(hero.getGold(), 0);
        assertEquals(hero.getXP(), 0);
        assertEquals(hero.getInventory().getItemCount(), 0);
        // max inventory slots is 4 x 4 = 16
        // so give one extra
        Sword lastSword = new Sword(null, null);
        for (int item = 0; item < 17; item++) {
            Sword sword = new Sword(null,null);
            sword.setStar(1);
            hero.addItem(sword);
            lastSword = sword;
        }
        int goldRefund = lastSword.getPrice()/2;
        int xpRefund = lastSword.getPrice();
        assertEquals(hero.getGold(), goldRefund);
        assertEquals(hero.getXP(), xpRefund);
        assertEquals(hero.getInventory().getItemCount(), 16);

    }

    
    /**
     * Test that the oldest item is being replaced
     */
    @Test
    void testOldestItem() {
        BasicHelper h = new BasicHelper();
        Character hero = h.testCharacter;
        assertEquals(hero.getGold(), 0);
        assertEquals(hero.getXP(), 0);
        assertEquals(hero.getInventory().getItemCount(), 0);

        Sword sword = new Sword(null, null);
        sword.setStar(1);
        hero.addItem(sword);
        // now give 16 more items to kick out sword
        for (int item = 0; item < 16; item++) {
            hero.addItem(new Helmet(null, null));
        }
        int goldRefund = sword.getPrice()/2;
        int xpRefund = sword.getPrice();
        assertEquals(hero.getGold(), goldRefund);
        assertEquals(hero.getXP(), xpRefund);
        assertEquals(hero.getInventory().getItemCount(), 16);
        for (int item = 0; item < 16; item++) {
            assertTrue(hero.getInventory().getItemByPositionInUnequippedInventoryItems(item) instanceof Helmet);
        }

    }
    /**
     * Test character gaining gold and xp from excess items
     */
    @Test
    void testCanHoldAllThePotions() {
        BasicHelper h = new BasicHelper();
        Character hero = h.testCharacter;
        assertEquals(hero.getGold(), 0);
        assertEquals(hero.getXP(), 0);
        assertEquals(hero.getPotions(), 0);

        // max inventory slots is 4 x 4 = 16, but not including potions
        for (int item = 0; item < 20; item++) {
            hero.addItem(new Potion(null, null));
        }
        assertEquals(hero.getGold(), 0);
        assertEquals(hero.getXP(), 0);
        assertEquals(hero.getPotions(), 20);

    }
}
