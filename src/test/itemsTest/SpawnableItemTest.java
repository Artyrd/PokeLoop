package test.itemsTest;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import java.util.List;

import test.testHelpers.*;
import unsw.loopmania.items.Gold;
import unsw.loopmania.items.Potion;
import unsw.loopmania.items.SpawnableItem;

public class SpawnableItemTest {

    /**
     * Test that a consumed potion heals the character for the amount of
     * 30 health
     */
    @Test
    public void potionTest() {
        BasicHelper help = new BasicHelper();
        help.testVampire.setCritChance(0);

        // Decrease the health of the character 
        help.testVampire.attack(help.testCharacter, null, null);
        help.testVampire.attack(help.testCharacter, null, null);
        help.testVampire.attack(help.testCharacter, null, null);
        assertTrue(help.testCharacter.getCurrHP() == 55);

        // Spawn a potion and use it
        help.testCharacter.addItem(help.testPotion);
        help.testCharacter.usePotion();
        assertTrue(help.testCharacter.getCurrHP() == 85);
    }

    /**
     * Test that gold increases the inventory gold
     */
    @Test 
    public void increaseGoldTest() {
        BasicHelper help = new BasicHelper();

        assertTrue(help.testCharacter.getGold() == 0);
        help.testCharacter.addItem(help.tenGold);

        assertTrue(help.testCharacter.getGold() > 0);

    }

    /**
     * Property based unit test the distribution of spawning items
     * 1/20 chance of spawning each tick
     */
    @Test
    public void spawningItemsTest() {
        BetterHelper h = new BetterHelper();
        int potions = 0;
        int gold = 0;

        for (int count = 0; count < 10000; count++) {
            List<SpawnableItem> result = h.testWorld.possiblySpawnItems();
            // if an item spawned, record it
            if (result.size()>0) {
                if (result.get(0) instanceof Potion) {
                    potions++;
                } else if (result.get(0) instanceof Gold) {
                    gold++;
                }
                
                // check the Items where spawned with the map boundaries
                for (SpawnableItem item : h.testWorld.getSpawnedItems()) {
                    assertTrue(item.getX() < 100);
                    assertTrue(item.getY() < 100);
                }
                
                // clear so we dont hit the max spawned items cap
                h.testWorld.getSpawnedItems().clear();
            }
        }
        
        // total items should be ~10000/20
        assertEquals(potions + gold, 500, 50);
        // gold shouldve spawned more than potions
        assertTrue(gold > potions);

    }
}
