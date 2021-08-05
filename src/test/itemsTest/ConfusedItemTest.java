package test.itemsTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.Test;

import test.testHelpers.BetterHelper;
import unsw.loopmania.items.ConfusedItem;

public class ConfusedItemTest {
    

    /**
     * Test a combination with only the ring (non confused item)
     */
    @Test
    public void testOneRing() {
        BetterHelper help = new BetterHelper();

        ConfusedItem item = new ConfusedItem();
        // The world can have a ring, no anduril, no tree stump
        item.setWorldRareItems(true, false, false);
        help.testCharacter.equip(item);
        help.testCharacter.setCritChance(0);
        help.testDoggie.setCritChance(0);

        // Kill and revive the character
        for (int i = 0; i < 5; i++) {
            help.testDoggie.attack(help.testCharacter, null, null);
        }
        item.revive(help.testCharacter);
        assertEquals(100, help.testCharacter.getCurrHP());
        // Item should be broken now
        assertEquals(false, item.shouldExist().get());
    }

    /**
     * Test a combination of the ring with anduril attack attributes
     */
    @Test
    public void testOneRingAnduril() {
        BetterHelper help = new BetterHelper();

        ConfusedItem item = new ConfusedItem();
        // The world can have a ring, anduril, but no tree stump
        item.setWorldRareItems(true, true, false);
        item.setTestCombinations("TheOneRing", "Anduril");
        help.testCharacter.equip(item);
        help.testCharacter.setCritChance(0);
        help.testDoggie.setCritChance(0);

        // Character should 3x damage to bosses and revive when dead
        // char attack 2 + weapon attack 15 - enemy defense 0 = 17
        assertEquals(17, help.testCharacter.attack(help.testSlug, null, null));
        // char attack 2 + weapon attack 15 - enemy defense 1 = 16
        assertEquals(16, help.testCharacter.attack(help.testZombie, null, null));
        // char attack 2 + weapon attack 15 - enemy defense 3 = 14
        assertEquals(14, help.testCharacter.attack(help.testVampire, null, null));
        // (2 + 15) * 3 - 3
        assertEquals(48, help.testCharacter.attack(help.testDoggie, null, null));
        // (2 + 15) * 3 - 5
        assertEquals(46, help.testCharacter.attack(help.testElanMuske, null, null));

        // Character should not apply a burn effect
        help.testCharacter.critAttack(help.testDoggie, null, null);
        assertEquals(0, help.testDoggie.getStatusList().size());

        // Kill and revive the character
        // Doggie takes exactly 5 hits to kill character
        for (int i = 0; i < 5; i++) {
            help.testDoggie.attack(help.testCharacter, null, null);
        }
        assertEquals(100, help.testCharacter.getCurrHP());
        // Item should be broken now
        assertEquals(false, item.shouldExist().get());
    }

    /**
     * Test a combination of the ring with tree stump defense attributes
     */
    @Test
    public void testOneRingTreeStump() {
        BetterHelper help = new BetterHelper();

        ConfusedItem item = new ConfusedItem();
        // The world can have a ring, anduril, but no tree stump
        item.setWorldRareItems(true, false, true);
        item.setTestCombinations("TheOneRing", "TreeStump");
        help.testCharacter.equip(item);
        help.testSlug.setCritChance(0);
        help.testZombie.setCritChance(0);
        help.testVampire.setCritChance(0);
        help.testDoggie.setCritChance(0);
        help.testElanMuske.setCritChance(0);

        // Character should receive less damage from enemies and revive when dead
        assertEquals(1.4, help.testSlug.attack(help.testCharacter, null, null), 0.01); // 4 * 0.6 - 1
        assertEquals(3.8, help.testZombie.attack(help.testCharacter, null, null), 0.01); // 8 * 0.6 - 1
        assertEquals(8.6, help.testVampire.attack(help.testCharacter, null, null), 0.01); // 16 * 0.6 - 1
        assertEquals(4, help.testDoggie.attack(help.testCharacter, null, null), 0.01); // 25 * 0.2 - 1
        assertEquals(7, help.testElanMuske.attack(help.testCharacter, null, null), 0.01); // 40 * 0.2 - 1

        // Kill and revive the character
        // doggie takes exactly 19 hits to kill the character
        for (int i = 0; i < 19; i++) {
            help.testDoggie.attack(help.testCharacter, null, null);
        }
        assertEquals(100, help.testCharacter.getCurrHP());
        // Item should be broken now
        assertEquals(false, item.shouldExist().get());
    }

    /**
     * Test a combination of the anduril only
     */
    @Test
    public void testAnduril() {
        BetterHelper help = new BetterHelper();

        ConfusedItem item = new ConfusedItem();
        // The world can have a no ring, anduril, no tree stump
        item.setWorldRareItems(false, true, false);
        help.testCharacter.equip(item);
        help.testCharacter.setCritChance(0);

        // Character should 3x damage to bosses and revive when dead
        // char attack 2 + weapon attack 15 - enemy defense 0 = 17
        assertEquals(17, help.testCharacter.attack(help.testSlug, null, null));
        // char attack 2 + weapon attack 15 - enemy defense 1 = 16
        assertEquals(16, help.testCharacter.attack(help.testZombie, null, null));
        // char attack 2 + weapon attack 15 - enemy defense 3 = 14
        assertEquals(14, help.testCharacter.attack(help.testVampire, null, null));
        // (2 + 15) * 3 - 3
        assertEquals(48, help.testCharacter.attack(help.testDoggie, null, null));
        // (2 + 15) * 3 - 5
        assertEquals(46, help.testCharacter.attack(help.testElanMuske, null, null));

        // Character should apply a burn effect
        help.testCharacter.critAttack(help.testDoggie, null, null);
        assertEquals(1, help.testDoggie.getStatusList().size());
    }

    /**
     * Test a combination of the anduril with a revive attribute that breaks
     */
    @Test
    public void testAndurilRingNoRevive() {
        BetterHelper help = new BetterHelper();

        ConfusedItem item = new ConfusedItem();
        // The world can have a no ring, anduril, no tree stump
        item.setWorldRareItems(true, true, false);
        item.setTestCombinations("Anduril", "TheOneRing");
        help.testDoggie.setMaxHP(100000);
        help.testDoggie.setCurrHP(100000);
        help.testDoggie.setCritChance(0);
        help.testCharacter.equip(item);
        help.testCharacter.setCritChance(0);

        // Character should 3x damage to bosses and revive when dead
        // char attack 2 + weapon attack 15 - enemy defense 0 = 17
        assertEquals(17, help.testCharacter.attack(help.testSlug, null, null));
        // char attack 2 + weapon attack 15 - enemy defense 1 = 16
        assertEquals(16, help.testCharacter.attack(help.testZombie, null, null));
        // char attack 2 + weapon attack 15 - enemy defense 3 = 14
        assertEquals(14, help.testCharacter.attack(help.testVampire, null, null));
        // (2 + 15) * 3 - 3
        assertEquals(48, help.testCharacter.attack(help.testDoggie, null, null));
        // (2 + 15) * 3 - 5
        assertEquals(46, help.testCharacter.attack(help.testElanMuske, null, null));

        // Character should apply a burn effect
        help.testCharacter.critAttack(help.testDoggie, null, null);
        assertEquals(1, help.testDoggie.getStatusList().size());

        // Test that after the sword breaks, the character can no longer be revived
        // Break the sword
        for (int i = 0; i < 13; i++) {
            help.testCharacter.attack(help.testDoggie, null, null);
        }
        assertEquals(0, item.getCurrDurability());
        // let doggie kill character
        for (int i = 0; i < 13; i++) {
            help.testDoggie.attack(help.testCharacter, null, null);
        }
        // should stay dead
        assertEquals(0, help.testCharacter.getCurrHP());
    }


    /**
     * Test a combination of the anduril with a revive attribute
     */
    @Test
    public void testAndurilRingRevive() {
        BetterHelper help = new BetterHelper();

        ConfusedItem item = new ConfusedItem();
        // The world can have a no ring, anduril, no tree stump
        item.setWorldRareItems(true, true, false);
        item.setTestCombinations("Anduril", "TheOneRing");
        help.testDoggie.setMaxHP(100000);
        help.testDoggie.setCurrHP(100000);
        help.testDoggie.setCritChance(0);
        help.testCharacter.equip(item);
        help.testCharacter.setCritChance(0);

        // Character should 3x damage to bosses and revive when dead
        // char attack 2 + weapon attack 15 - enemy defense 0 = 17
        assertEquals(17, help.testCharacter.attack(help.testSlug, null, null));
        // char attack 2 + weapon attack 15 - enemy defense 1 = 16
        assertEquals(16, help.testCharacter.attack(help.testZombie, null, null));
        // char attack 2 + weapon attack 15 - enemy defense 3 = 14
        assertEquals(14, help.testCharacter.attack(help.testVampire, null, null));
        // (2 + 15) * 3 - 3
        assertEquals(48, help.testCharacter.attack(help.testDoggie, null, null));
        // (2 + 15) * 3 - 5
        assertEquals(46, help.testCharacter.attack(help.testElanMuske, null, null));

        // Character should apply a burn effect
        help.testCharacter.critAttack(help.testDoggie, null, null);
        assertEquals(1, help.testDoggie.getStatusList().size());

        assertTrue(0 <item.getCurrDurability());
        // let doggie kill character
        for (int i = 0; i < 5; i++) {
            help.testDoggie.attack(help.testCharacter, null, null);
        }
        // should revive
        assertEquals(100, help.testCharacter.getCurrHP());
    }

    /**
     * Test a combination of anduril with tree stump defense attributes
     */
    @Test
    public void testAndurilTreeStump() {
        BetterHelper help = new BetterHelper();

        ConfusedItem item = new ConfusedItem();
        // The world can have a no ring, anduril, tree stump
        item.setWorldRareItems(false, true, true);
        item.setTestCombinations("Anduril", "TreeStump");
        help.testCharacter.equip(item);
        help.testCharacter.setCritChance(0);
        help.testSlug.setCritChance(0);
        help.testZombie.setCritChance(0);
        help.testVampire.setCritChance(0);
        help.testDoggie.setCritChance(0);
        help.testElanMuske.setCritChance(0);

        // Character should deal bonus damage to enemies and even higher to bosses
        // char attack 2 + weapon attack 15 - enemy defense 0 = 17
        assertEquals(17, help.testCharacter.attack(help.testSlug, null, null));
        // char attack 2 + weapon attack 15 - enemy defense 1 = 16
        assertEquals(16, help.testCharacter.attack(help.testZombie, null, null));
        // char attack 2 + weapon attack 15 - enemy defense 3 = 14
        assertEquals(14, help.testCharacter.attack(help.testVampire, null, null));
        // (2 + 15) * 3 - 3
        assertEquals(48, help.testCharacter.attack(help.testDoggie, null, null));
        // (2 + 15) * 3 - 5
        assertEquals(46, help.testCharacter.attack(help.testElanMuske, null, null));

        // Character should apply a burn effect
        help.testCharacter.critAttack(help.testDoggie, null, null);
        assertEquals(1, help.testDoggie.getStatusList().size());

        // Character should receive less damage from enemies
        assertEquals(1.4, help.testSlug.attack(help.testCharacter, null, null), 0.01); // 4 * 0.6 - 1
        assertEquals(3.8, help.testZombie.attack(help.testCharacter, null, null), 0.01); // 8 * 0.6 - 1
        assertEquals(8.6, help.testVampire.attack(help.testCharacter, null, null), 0.01); // 16 * 0.6 - 1
        assertEquals(4, help.testDoggie.attack(help.testCharacter, null, null), 0.01); // 25 * 0.2 - 1
        assertEquals(7, help.testElanMuske.attack(help.testCharacter, null, null), 0.01); // 40 * 0.2 - 1

        // Attacks to character should not decrease durability since its not the base item
        for (int i = 0; i < 13; i++) {
            help.testSlug.attack(help.testCharacter, null, null);
        }
        assertEquals(13, item.getCurrDurability());

        // Attacks with the sword should decrease durability, already used 5, 15 remaining
        help.testDoggie.setMaxHP(10000);
        help.testDoggie.setCurrHP(10000);
        for (int i = 0; i < 13; i++) {
            help.testCharacter.attack(help.testDoggie, null, null);
        }
        assertEquals(0, item.getCurrDurability());
    }

    /**
     * Test a combination of tree stump only
     */
    @Test
    public void testTreeStump() {
        BetterHelper help = new BetterHelper();

        ConfusedItem item = new ConfusedItem();
        // The world can have a no ring, no anduril, tree stump
        item.setWorldRareItems(false, false, true);
        help.testCharacter.equip(item);
        help.testSlug.setCritChance(0);
        help.testZombie.setCritChance(0);
        help.testVampire.setCritChance(0);
        help.testDoggie.setCritChance(0);
        help.testElanMuske.setCritChance(0);

        // Character should receive less damage from enemies and revive when dead
        assertEquals(1.4, help.testSlug.attack(help.testCharacter, null, null), 0.01); // 4 * 0.6 - 1
        assertEquals(3.8, help.testZombie.attack(help.testCharacter, null, null), 0.01); // 8 * 0.6 - 1
        assertEquals(8.6, help.testVampire.attack(help.testCharacter, null, null), 0.01); // 16 * 0.6 - 1
        assertEquals(4, help.testDoggie.attack(help.testCharacter, null, null), 0.01); // 25 * 0.2 - 1
        assertEquals(7, help.testElanMuske.attack(help.testCharacter, null, null), 0.01); // 40 * 0.2 - 1

    }

    /**
     * Test a combination of tree stump with anduril attack attributes
     */
    @Test
    public void testTreeStumpAnduril() {
        BetterHelper help = new BetterHelper();

        ConfusedItem item = new ConfusedItem();
        // The world can have a no ring, anduril, tree stump
        item.setWorldRareItems(false, true, true);
        item.setTestCombinations("TreeStump", "Anduril");
        help.testCharacter.equip(item);
        help.testCharacter.setCritChance(0);
        help.testSlug.setCritChance(0);
        help.testZombie.setCritChance(0);
        help.testVampire.setCritChance(0);
        help.testDoggie.setCritChance(0);
        help.testElanMuske.setCritChance(0);

        // Character should receive less damage from enemies
        assertEquals(1.4, help.testSlug.attack(help.testCharacter, null, null), 0.01); // 4 * 0.6 - 1
        assertEquals(3.8, help.testZombie.attack(help.testCharacter, null, null), 0.01); // 8 * 0.6 - 1
        assertEquals(8.6, help.testVampire.attack(help.testCharacter, null, null), 0.01); // 16 * 0.6 - 1
        assertEquals(4, help.testDoggie.attack(help.testCharacter, null, null), 0.01); // 25 * 0.2 - 1
        assertEquals(7, help.testElanMuske.attack(help.testCharacter, null, null), 0.01); // 40 * 0.2 - 1

        // Character should deal bonus damage to enemies and even higher to bosses
        // char attack 2 + weapon attack 15 - enemy defense 0 = 17
        assertEquals(17, help.testCharacter.attack(help.testSlug, null, null));
        // char attack 2 + weapon attack 15 - enemy defense 1 = 16
        assertEquals(16, help.testCharacter.attack(help.testZombie, null, null));
        // char attack 2 + weapon attack 15 - enemy defense 3 = 14
        assertEquals(14, help.testCharacter.attack(help.testVampire, null, null));
        // (2 + 15) * 3 - 3
        assertEquals(48, help.testCharacter.attack(help.testDoggie, null, null));
        // (2 + 15) * 3 - 5
        assertEquals(46, help.testCharacter.attack(help.testElanMuske, null, null));

        // Character should not apply a burn effect
        help.testCharacter.critAttack(help.testDoggie, null, null);
        assertEquals(0, help.testDoggie.getStatusList().size());

        // Attacks with the should not decrease durability as it is not the base item
        help.testDoggie.setMaxHP(10000);
        help.testDoggie.setCurrHP(10000);
        for (int i = 0; i < 10; i++) {
            help.testCharacter.attack(help.testDoggie, null, null);
        }
        assertEquals(15, item.getCurrDurability());

        // Should break after 20 total uses of defending, we already used 15
        for (int i = 0; i < 15; i++) {
            help.testSlug.attack(help.testCharacter, null, null);
        }
        assertEquals(0, item.getCurrDurability());
    }

    /**
     * Test a combination of the tree stump with revive attributes
     */
    @Test
    public void testTreeStumpRing() {
        BetterHelper help = new BetterHelper();

        ConfusedItem item = new ConfusedItem();
        // The world can have a ring, no anduril, tree stump
        item.setWorldRareItems(true, false, true);
        item.setTestCombinations("TreeStump", "TheOneRing");
        help.testCharacter.equip(item);
        help.testSlug.setCritChance(0);
        help.testZombie.setCritChance(0);
        help.testVampire.setCritChance(0);
        help.testDoggie.setCritChance(0);
        help.testElanMuske.setCritChance(0);

        // Character should receive less damage from enemies and revive when dead
        assertEquals(1.4, help.testSlug.attack(help.testCharacter, null, null), 0.01); // 4 * 0.6 - 1
        assertEquals(3.8, help.testZombie.attack(help.testCharacter, null, null), 0.01); // 8 * 0.6 - 1
        assertEquals(8.6, help.testVampire.attack(help.testCharacter, null, null), 0.01); // 16 * 0.6 - 1
        assertEquals(4, help.testDoggie.attack(help.testCharacter, null, null), 0.01); // 25 * 0.2 - 1
        assertEquals(7, help.testElanMuske.attack(help.testCharacter, null, null), 0.01); // 40 * 0.2 - 1

         // Test that after the shield breaks, the character can no longer be revived
         // Break the shield
         for (int i = 0; i < 15; i++) {
            help.testSlug.attack(help.testCharacter, null, null);
        }
        assertEquals(0, item.getCurrDurability());
        // Kill the character
        for (int i = 0; i < 4; i++) {
            help.testDoggie.attack(help.testCharacter, null, null);
        }

        assertEquals(0, help.testCharacter.getCurrHP());
    }

    /**
     * Test if the character revives twice
     */
    @Test
    public void testDoubleRevive() {
        BetterHelper help = new BetterHelper();

        ConfusedItem item = new ConfusedItem();
        //item.setWorldRareItems(true, true, true);
        item.setTestCombinations("Anduril", "TheOneRing");
        help.testCharacter.equip(item);
        ConfusedItem item2 = new ConfusedItem();
        //item2.setWorldRareItems(true, true, true);
        item2.setTestCombinations("TheOneRing", "Anduril");
        help.testCharacter.equip(item2);
;
        help.testDoggie.setCritChance(0);
;
        // Kill the character
        for (int i = 0; i < 5; i++) {
            help.testDoggie.attack(help.testCharacter, null, null);
        }
        assertEquals(100, help.testCharacter.getCurrHP());
        // Kill the character again
        for (int i = 0; i < 5; i++) {
            help.testDoggie.attack(help.testCharacter, null, null);
        }
        assertEquals(100, help.testCharacter.getCurrHP());
    }

}
