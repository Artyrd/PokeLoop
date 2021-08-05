package test.itemsTest;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import test.testHelpers.BasicHelper;


public class WeaponTest {

    /**
     * Test the damage of the sword in addition to the base damage of the 
     * character
     */
    @Test
    public void testSword() {
        BasicHelper help = new BasicHelper();

        help.testCharacter.equip(help.testSword);
        help.testCharacter.setCritChance(0);

        // Final damage dealt to enemies using a swords normal attack
        assertTrue(help.testCharacter.attack(help.testSlug, null, null) == 7);
        assertTrue(help.testCharacter.attack(help.testZombie, null, null) == 6);
        assertTrue(help.testCharacter.attack(help.testVampire, null, null) == 4);
    }

    /**
     * Test the damage of the stake in addition to the base damage of the 
     * character
     * Stake does triple damage against the vampire
     */
    @Test
    public void testStake() {

        BasicHelper help = new BasicHelper();
        help.testCharacter.equip(help.testStake);
        help.testCharacter.setCritChance(0);

        // Final damage dealt to enemies using a stakes normal attack
        assertEquals(help.testCharacter.attack(help.testSlug, null, null), 5);
        assertEquals(help.testCharacter.attack(help.testZombie, null, null), 4);
        assertEquals(help.testCharacter.attack(help.testVampire, null, null), 12);
        
    }
    /**
     * Test the crit damage of the stake in addition to the base damage of the 
     * character
     * Stake does triple damage against the vampire
     */
    @Test
    public void testStakeCrit() {

        BasicHelper help = new BasicHelper();
        help.testCharacter.equip(help.testStake);
        // a normal crit w/ stake = (2 + 3)*1.5 = 7.5 damage

        assertEquals(help.testCharacter.critAttack(help.testSlug, null, null), 7.5);
        assertEquals(help.testCharacter.critAttack(help.testZombie, null, null), 6.5);
        // vs vampire: 7.5 * 3 = 22.5, minus 3def of vampire= 19.5
        assertEquals(help.testCharacter.critAttack(help.testVampire, null, null), 19.5);
        
    }
    /**
     * Test the damage of the staff in addition to the base damage of the 
     * character
     */
    @Test
    public void testStaff() {

        BasicHelper help = new BasicHelper();
        help.testCharacter.equip(help.testStaff);
        help.testCharacter.setCritChance(0);

        // Final damage dealt to enemies using a staffs normal attack
        assertTrue(help.testCharacter.attack(help.testSlug, null, null) == 3);
        assertTrue(help.testCharacter.attack(help.testZombie, null, null) == 2);
        assertTrue(help.testCharacter.attack(help.testVampire, null, null) == 0);
    }

    /**
     * Test the trance effect of the staff
     * Further testing on this effect in the status tests
     */
    @Test
    public void testStaffTrance() {

        BasicHelper help = new BasicHelper();
        help.testCharacter.equip(help.testStaff);
        help.testCharacter.setCritChance(0);

        help.testCharacter.critAttack(help.testZombie, null, null);
        assertTrue(help.testZombie.getStatusIDs().contains("Tranced"));

    }

    /**
     * Test the damage of anduril against normal enemies and bosses
     */
    @Test 
    public void testAnduril() {

        BasicHelper help = new BasicHelper();
        help.testCharacter.equip(help.testAnduril);
        help.testCharacter.setCritChance(0);

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
        
        // (char attack 2 + weapon attack 15) * 1.5^2 - enemy defense 0 = 25.5
        assertEquals(25.5, help.testCharacter.critAttack(help.testSlug, null, null));
        // (char attack 2 + weapon attack 15) * 1.5^2 - enemy defense 1 = 24.5
        assertEquals(24.5, help.testCharacter.critAttack(help.testZombie, null, null));
        // (char attack 2 + weapon attack 15) * 1.5^2 - enemy defense 3 = 22.5
        assertEquals(22.5, help.testCharacter.critAttack(help.testVampire, null, null));
        // (2 + 15) * 1.5 * 3 - 3 
        assertEquals(73.5, help.testCharacter.critAttack(help.testDoggie, null, null));
        // (2 + 15) * 1.5 * 3 - 5
        assertEquals(71.5, help.testCharacter.critAttack(help.testElanMuske, null, null));

    }

    /**
     * Test the attack damage of a weapon at different tiers
     * Each tier multiplies the weapon attack attribute by the star amount
     * eg, star = 2: atk * 2
     */
    @Test
    public void testWeaponTiers() {
        BasicHelper help = new BasicHelper();
        // Set up character and dummy enemy
        help.testCharacter.setCritChance(0);
        help.testSlug.setMaxHP(1000000);
        help.testSlug.setCurrHP(1000000);
        
        // Test sword attacks at different tiers
        // Reset attack before each test
        help.testSword.setATK(5);
        help.testCharacter.equip(help.testSword);
        help.testSword.setStar(1, help.testCharacter);
        assertEquals(7, help.testCharacter.attack(help.testSlug, null, null));
        help.testSword.setATK(5);
        help.testSword.setStar(2, help.testCharacter);
        assertEquals(12, help.testCharacter.attack(help.testSlug, null, null));
        help.testSword.setATK(5);
        help.testSword.setStar(3, help.testCharacter);
        assertEquals(17, help.testCharacter.attack(help.testSlug, null, null));

        // Test staff attacks at different tiers
        // Reset attack before each test
        help.testStaff.setATK(1);
        help.testCharacter.equip(help.testStaff);
        help.testStaff.setStar(1, help.testCharacter);
        assertEquals(3, help.testCharacter.attack(help.testSlug, null, null));
        help.testStaff.setATK(1);
        help.testStaff.setStar(2, help.testCharacter);
        assertEquals(4, help.testCharacter.attack(help.testSlug, null, null));
        help.testStaff.setATK(1);
        help.testStaff.setStar(3, help.testCharacter);
        assertEquals(5, help.testCharacter.attack(help.testSlug, null, null));

        // Test stake attacks at different tiers
        // Reset attack before each test
        help.testStake.setATK(3);
        help.testCharacter.equip(help.testStake);
        help.testStake.setStar(1, help.testCharacter);
        assertEquals(5, help.testCharacter.attack(help.testSlug, null, null));
        help.testStake.setATK(3);
        help.testStake.setStar(2, help.testCharacter);
        assertEquals(8, help.testCharacter.attack(help.testSlug, null, null));
        help.testStake.setATK(3);
        help.testStake.setStar(3, help.testCharacter);
        assertEquals(11, help.testCharacter.attack(help.testSlug, null, null));

        // Test anduril attacks at different tiers
        // Reset attack before each test
        help.testAnduril.setATK(15);
        help.testCharacter.equip(help.testAnduril);
        help.testAnduril.setStar(1, help.testCharacter);
        assertEquals(17, help.testCharacter.attack(help.testSlug, null, null));
        help.testAnduril.setATK(15);
        help.testAnduril.setStar(2, help.testCharacter);
        assertEquals(32, help.testCharacter.attack(help.testSlug, null, null));
        help.testAnduril.setATK(15);
        help.testAnduril.setStar(3, help.testCharacter);
        assertEquals(47, help.testCharacter.attack(help.testSlug, null, null));
    }

}
