package test.itemsTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;

import javafx.beans.property.SimpleIntegerProperty;
import test.testHelpers.BasicHelper;
import unsw.loopmania.items.Anduril;
import unsw.loopmania.items.Staff;
import unsw.loopmania.items.Stake;
import unsw.loopmania.items.Sword;

public class DurabilityTest {
    
    /**
     * Test the durability of the sword
     */
    @Test
    public void testSword() {
        BasicHelper help = new BasicHelper();

        help.testCharacter.setCritChance(0);
        // Enemy dummy
        help.testVampire.setCurrHP(1000);

        // Decrease durability with normal attacks
        help.testCharacter.equip(help.testSword);
        help.testCharacter.attack(help.testVampire, null, null);

        assertEquals(19, help.testSword.getCurrDurability());
        
        for (int i = 0; i < 19; i++) {
            help.testCharacter.attack(help.testVampire, null, null);
        }
        // No more durability, unequipped
        assertEquals(0, help.testSword.getCurrDurability());
        assertEquals(0, help.testCharacter.getInventory().getEquipComposite().getWeapons().size());
        assertEquals(2, help.testCharacter.getCurrATK());
        
        // Decrease durability with crit attacks
        help.testSword = new Sword(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        help.testCharacter.equip(help.testSword);
        //Reset item stats
        help.testSword.setStar(1);
        assertEquals(20, help.testSword.getCurrDurability());

        for (int i = 0; i < 10; i++) {
            help.testCharacter.critAttack(help.testVampire, null, null);
        }
        // No more durability, unequipped
        assertEquals(0, help.testSword.getCurrDurability());
        assertEquals(0, help.testCharacter.getInventory().getEquipComposite().getWeapons().size());
        assertEquals(2, help.testCharacter.getCurrATK());
    }

    /**
     * Test the durability of the stake
     */
    @Test
    public void testStake() {
        BasicHelper help = new BasicHelper();

        help.testCharacter.setCritChance(0);
        // Enemy dummy
        help.testZombie.setCurrHP(1000);

        // Decrease durability with normal attacks
        help.testCharacter.equip(help.testStake);
        help.testCharacter.attack(help.testZombie, null, null);

        assertEquals(9, help.testStake.getCurrDurability());
        
        for (int i = 0; i < 9; i++) {
            help.testCharacter.attack(help.testZombie, null, null);
        }
        // No more durability, unequipped
        assertEquals(0, help.testStake.getCurrDurability());
        assertEquals(0, help.testCharacter.getInventory().getEquipComposite().getWeapons().size());
        assertEquals(2, help.testCharacter.getCurrATK());
        
        // Decrease durability with crit attacks
        help.testStake = new Stake(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        help.testCharacter.equip(help.testStake);
        // Reset stake stats
        help.testStake.setStar(1);
        assertEquals(10, help.testStake.getCurrDurability());

        for (int i = 0; i < 10; i++) {
            help.testCharacter.critAttack(help.testZombie, null, null);
        }
        // No more durability, unequipped
        assertEquals(0, help.testStake.getCurrDurability());
        assertEquals(0, help.testCharacter.getInventory().getEquipComposite().getWeapons().size());
        assertEquals(2, help.testCharacter.getCurrATK());
    }

    /**
     * Test the durability of the staff
     */
    @Test
    public void testStaff() {
        BasicHelper help = new BasicHelper();

        help.testCharacter.setCritChance(0);
        // Enemy dummy
        help.testZombie.setCurrHP(1000);

        // Decrease durability with normal attacks
        help.testCharacter.equip(help.testStaff);
        help.testCharacter.attack(help.testZombie, null, null);

        assertEquals(14, help.testStaff.getCurrDurability());
        
        for (int i = 0; i < 14; i++) {
            help.testCharacter.attack(help.testZombie, null, null);
        }
        // No more durability, unequipped
        assertEquals(0, help.testStaff.getCurrDurability());
        assertEquals(0, help.testCharacter.getInventory().getEquipComposite().getWeapons().size());
        assertEquals(2, help.testCharacter.getCurrATK());
        
        // Decrease durability with crit attacks
        help.testStaff = new Staff(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        help.testCharacter.equip(help.testStaff);
        // Reset staff qualities
        help.testStaff.setStar(1);
        assertEquals(15, help.testStaff.getCurrDurability());

        for (int i = 0; i < 15; i++) {
            help.testCharacter.critAttack(help.testZombie, null, null);
        }
        // No more durability, unequipped
        assertEquals(-2, help.testStaff.getCurrDurability());
        assertEquals(0, help.testCharacter.getInventory().getEquipComposite().getWeapons().size());
        assertEquals(2, help.testCharacter.getCurrATK());
    }
    /**
     * Test the durability of the sword
     */
    @Test
    public void testAnduril() {
        BasicHelper help = new BasicHelper();

        help.testCharacter.setCritChance(0);
        // Enemy dummy
        help.testVampire.setCurrHP(1000);

        // Decrease durability with normal attacks
        help.testCharacter.equip(help.testAnduril);
        help.testCharacter.attack(help.testVampire, null, null);

        assertEquals(19, help.testAnduril.getCurrDurability());
        
        for (int i = 0; i < 19; i++) {
            help.testCharacter.attack(help.testVampire, null, null);
        }
        // No more durability, unequipped
        assertEquals(0, help.testAnduril.getCurrDurability());
        assertEquals(0, help.testCharacter.getInventory().getEquipComposite().getWeapons().size());
        assertEquals(2, help.testCharacter.getCurrATK());
        
        // Decrease durability with crit attacks
        help.testAnduril = new Anduril(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        help.testCharacter.equip(help.testAnduril);
        // Reset item stats
        help.testAnduril.setStar(1);
        assertEquals(20, help.testAnduril.getCurrDurability());

        for (int i = 0; i < 10; i++) {
            help.testCharacter.critAttack(help.testVampire, null, null);
        }
        // No more durability, unequipped
        assertEquals(0, help.testAnduril.getCurrDurability());
        assertEquals(0, help.testCharacter.getInventory().getEquipComposite().getWeapons().size());
        assertEquals(2, help.testCharacter.getCurrATK());
        
    }

    /**
     * Test the durability of armour
     */
    @Test
    public void testArmour() {
        BasicHelper help = new BasicHelper();

        help.testVampire.setCritChance(0);
        help.testCharacter.setCurrHP(1000);

        // Decrease durability with normal attacks
        help.testCharacter.equip(help.testArmour);
        help.testVampire.attack(help.testCharacter, null, null);
        assertEquals(19, help.testArmour.getCurrDurability());
        
        for (int i = 0; i < 19; i++) {
            help.testVampire.attack(help.testCharacter, null, null);
        }
        // No more durability, unequipped
        assertEquals(0, help.testArmour.getCurrDurability());
        assertEquals(0, help.testCharacter.getInventory().getEquipComposite().getProtectionEquips().size());
        assertEquals(1, help.testCharacter.getCurrDEF());
    }

    /**
     * Test the durability of helmet
     */
    @Test
    public void testHelmet() {
        BasicHelper help = new BasicHelper();

        help.testVampire.setCritChance(0);
        help.testCharacter.setCurrHP(1000);

        // Decrease durability with normal attacks
        help.testCharacter.equip(help.testHelmet);
        help.testVampire.attack(help.testCharacter, null, null);
        assertEquals(14, help.testHelmet.getCurrDurability());
        
        for (int i = 0; i < 14; i++) {
            help.testVampire.attack(help.testCharacter, null, null);
        }
        // No more durability, unequipped
        assertEquals(0, help.testHelmet.getCurrDurability());
        assertEquals(0, help.testCharacter.getInventory().getEquipComposite().getProtectionEquips().size());
        assertEquals(1, help.testCharacter.getCurrDEF());
    }
    
    /**
     * Test the durability of shield
     */
    @Test
    public void testShield() {
        BasicHelper help = new BasicHelper();

        help.testVampire.setCritChance(0);
        help.testCharacter.setCurrHP(1000);

        // Decrease durability with normal attacks
        help.testCharacter.equip(help.testShield);
        help.testVampire.attack(help.testCharacter, null, null);
        assertEquals(19, help.testShield.getCurrDurability());
        
        for (int i = 0; i < 19; i++) {
            help.testVampire.attack(help.testCharacter, null, null);
        }
        // No more durability, unequipped
        assertEquals(0, help.testShield.getCurrDurability());
        assertEquals(0, help.testCharacter.getInventory().getEquipComposite().getProtectionEquips().size());
        assertEquals(1, help.testCharacter.getCurrDEF());
    }
    
    /**
     * Test the durability of tree stump
     */
    @Test
    public void testTreeStump() {
        BasicHelper help = new BasicHelper();

        help.testVampire.setCritChance(0);
        help.testCharacter.setCurrHP(1000);

        // Decrease durability with normal attacks
        help.testCharacter.equip(help.testTreeStump);
        help.testVampire.attack(help.testCharacter, null, null);
        assertEquals(19, help.testTreeStump.getCurrDurability());
        for (int i = 0; i < 19; i++) {
            help.testVampire.attack(help.testCharacter, null, null);
        }
        // No more durability, unequipped
        assertEquals(0, help.testTreeStump.getCurrDurability());
        assertEquals(0, help.testCharacter.getInventory().getEquipComposite().getProtectionEquips().size());
        assertEquals(1, help.testCharacter.getCurrDEF());
    }

    /**
     * Test the durability of items of different tiers
     */
    @Test
    public void testTierDurability() {
        BasicHelper help = new BasicHelper();

        // Create a dummy enemy
        help.testDoggie.setMaxHP(100000);
        help.testDoggie.setCurrHP(100000);
        // Set up character
        help.testCharacter.setMaxHP(100000);
        help.testCharacter.setCurrHP(100000);
        help.testCharacter.setCritChance(0);

        /////////////////////////////// Weapons /////////////////////////////////
        // Test the durability of the sword
        help.testCharacter.equip(help.testSword);
        assertEquals(20, help.testSword.getCurrDurability());
        for (int attacks = 0; attacks < 20; attacks++) {
            help.testCharacter.attack(help.testDoggie, null, null);
        }
        assertEquals(0, help.testSword.getCurrDurability());
        // 2 stars
        help.testSword.setStar(2);
        help.testCharacter.equip(help.testSword);
        assertEquals(30, help.testSword.getCurrDurability());
        for (int attacks = 0; attacks < 30; attacks++) {
            help.testCharacter.attack(help.testDoggie, null, null);
        }
        assertEquals(0, help.testSword.getCurrDurability());
        // 3 stars
        help.testSword.setStar(3);
        help.testCharacter.equip(help.testSword);
        assertEquals(40, help.testSword.getCurrDurability());
        for (int attacks = 0; attacks < 40; attacks++) {
            help.testCharacter.attack(help.testDoggie, null, null);
        }
        assertEquals(0, help.testSword.getCurrDurability());
    
        // Test the durability of the staff
        help.testCharacter.equip(help.testStaff);
        assertEquals(15, help.testStaff.getCurrDurability());
        for (int attacks = 0; attacks < 15; attacks++) {
            help.testCharacter.attack(help.testDoggie, null, null);
        }
        assertEquals(0, help.testStaff.getCurrDurability());
        // 2 stars
        help.testStaff.setStar(2);
        help.testCharacter.equip(help.testStaff);
        assertEquals(25, help.testStaff.getCurrDurability());
        for (int attacks = 0; attacks < 25; attacks++) {
            help.testCharacter.attack(help.testDoggie, null, null);
        }
        assertEquals(0, help.testStaff.getCurrDurability());
        // 3 stars
        help.testStaff.setStar(3);
        help.testCharacter.equip(help.testStaff);
        assertEquals(35, help.testStaff.getCurrDurability());
        for (int attacks = 0; attacks < 35; attacks++) {
            help.testCharacter.attack(help.testDoggie, null, null);
        }
        assertEquals(0, help.testStaff.getCurrDurability());

        // Test the durability of the stake
        help.testCharacter.equip(help.testStake);
        assertEquals(10, help.testStake.getCurrDurability());
        for (int attacks = 0; attacks < 10; attacks++) {
            help.testCharacter.attack(help.testDoggie, null, null);
        }
        assertEquals(0, help.testStake.getCurrDurability());
        // 2 stars
        help.testStake.setStar(2);
        help.testCharacter.equip(help.testStake);
        assertEquals(20, help.testStake.getCurrDurability());
        for (int attacks = 0; attacks < 20; attacks++) {
            help.testCharacter.attack(help.testDoggie, null, null);
        }
        assertEquals(0, help.testStake.getCurrDurability());
        // 3 stars
        help.testStake.setStar(3);
        help.testCharacter.equip(help.testStake);
        assertEquals(30, help.testStake.getCurrDurability());
        for (int attacks = 0; attacks < 30; attacks++) {
            help.testCharacter.attack(help.testDoggie, null, null);
        }
        assertEquals(0, help.testStake.getCurrDurability());

        // Test the durability of anduril
        help.testCharacter.equip(help.testAnduril);
        assertEquals(20, help.testAnduril.getCurrDurability());
        for (int attacks = 0; attacks < 20; attacks++) {
            help.testCharacter.attack(help.testDoggie, null, null);
        }
        assertEquals(0, help.testAnduril.getCurrDurability());
        // 2 stars
        help.testAnduril.setStar(2);
        help.testCharacter.equip(help.testAnduril);
        assertEquals(30, help.testAnduril.getCurrDurability());
        for (int attacks = 0; attacks < 30; attacks++) {
            help.testCharacter.attack(help.testDoggie, null, null);
        }
        assertEquals(0, help.testAnduril.getCurrDurability());
        // 3 stars
        help.testAnduril.setStar(3);
        help.testCharacter.equip(help.testAnduril);
        assertEquals(40, help.testAnduril.getCurrDurability());
        for (int attacks = 0; attacks < 40; attacks++) {
            help.testCharacter.attack(help.testDoggie, null, null);
        }
        assertEquals(0, help.testAnduril.getCurrDurability());

        
        /////////////////////////////// Protection //////////////////////////////
        // Test the durability of Armour
        help.testCharacter.equip(help.testArmour);
        assertEquals(20, help.testArmour.getCurrDurability());
        for (int defend = 0; defend < 20; defend++) {
            help.testDoggie.attack(help.testCharacter, null, null);
        }
        assertEquals(0, help.testArmour.getCurrDurability());
        // 2 stars
        help.testArmour.setStar(2);
        help.testCharacter.equip(help.testArmour);
        assertEquals(30, help.testArmour.getCurrDurability());
        for (int defend = 0; defend < 30; defend++) {
            help.testDoggie.attack(help.testCharacter, null, null);
        }
        assertEquals(0, help.testArmour.getCurrDurability());
        // 3 stars
        help.testArmour.setStar(3);
        help.testCharacter.equip(help.testArmour);
        assertEquals(40, help.testArmour.getCurrDurability());
        for (int defend = 0; defend < 40; defend++) {
            help.testDoggie.attack(help.testCharacter, null, null);
        }
        assertEquals(0, help.testArmour.getCurrDurability());

        // Test the durability of the shield
        help.testCharacter.equip(help.testShield);
        assertEquals(20, help.testShield.getCurrDurability());
        for (int defend = 0; defend < 20; defend++) {
            help.testDoggie.attack(help.testCharacter, null, null);
        }
        assertEquals(0, help.testShield.getCurrDurability());
        // 2 stars
        help.testShield.setStar(2);
        help.testCharacter.equip(help.testShield);
        assertEquals(30, help.testShield.getCurrDurability());
        for (int defend = 0; defend < 30; defend++) {
            help.testDoggie.attack(help.testCharacter, null, null);
        }
        assertEquals(0, help.testShield.getCurrDurability());
        // 3 stars
        help.testShield.setStar(3);
        help.testCharacter.equip(help.testShield);
        assertEquals(40, help.testShield.getCurrDurability());
        for (int defend = 0; defend < 40; defend++) {
            help.testDoggie.attack(help.testCharacter, null, null);
        }
        assertEquals(0, help.testShield.getCurrDurability());

        // Test the durability of the helmet
        help.testCharacter.equip(help.testHelmet);
        assertEquals(15, help.testHelmet.getCurrDurability());
        for (int defend = 0; defend < 15; defend++) {
            help.testDoggie.attack(help.testCharacter, null, null);
        }
        assertEquals(0, help.testHelmet.getCurrDurability());
        // 2 stars
        help.testHelmet.setStar(2);
        help.testCharacter.equip(help.testHelmet);
        assertEquals(25, help.testHelmet.getCurrDurability());
        for (int defend = 0; defend < 25; defend++) {
            help.testDoggie.attack(help.testCharacter, null, null);
        }
        assertEquals(0, help.testHelmet.getCurrDurability());
        // 3 stars
        help.testHelmet.setStar(3);
        help.testCharacter.equip(help.testHelmet);
        assertEquals(35, help.testHelmet.getCurrDurability());
        for (int defend = 0; defend < 35; defend++) {
            help.testDoggie.attack(help.testCharacter, null, null);
        }
        assertEquals(0, help.testHelmet.getCurrDurability());

        // Test the durability of the treestump
        help.testCharacter.equip(help.testTreeStump);
        assertEquals(20, help.testTreeStump.getCurrDurability());
        for (int defend = 0; defend < 20; defend++) {
            help.testDoggie.attack(help.testCharacter, null, null);
        }
        assertEquals(0, help.testTreeStump.getCurrDurability());
        // 2 stars
        help.testTreeStump.setStar(2);
        help.testCharacter.equip(help.testTreeStump);
        assertEquals(30, help.testTreeStump.getCurrDurability());
        for (int defend = 0; defend < 30; defend++) {
            help.testDoggie.attack(help.testCharacter, null, null);
        }
        assertEquals(0, help.testTreeStump.getCurrDurability());
        // 3 stars
        help.testTreeStump.setStar(3);
        help.testCharacter.equip(help.testTreeStump);
        assertEquals(40, help.testTreeStump.getCurrDurability());
        for (int defend = 0; defend < 40; defend++) {
            help.testDoggie.attack(help.testCharacter, null, null);
        }
        assertEquals(0, help.testTreeStump.getCurrDurability());
    }
}
