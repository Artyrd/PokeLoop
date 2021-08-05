package test.itemsTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Random;

import org.junit.jupiter.api.Test;

import test.testHelpers.BasicHelper;
import unsw.loopmania.items.Sword;
import unsw.loopmania.items.Weapon;
import unsw.loopmania.status.VampireDebuff;
import unsw.loopmania.status.Zombified;

public class ProtectionTest {

    /**
     * Test that the armour halves the damage dealt by enemies before 
     * base defense is applied
     */
    @Test
    public void armourTest() {

        BasicHelper help = new BasicHelper();

        help.testCharacter.equip(help.testArmour);
        help.testSlug.setCritChance(0);
        help.testZombie.setCritChance(0);
        help.testVampire.setCritChance(0);

        // Final damage dealt to the character should be halved and decrease base defense
        assertEquals(-2, help.testSlug.attack(help.testCharacter, null, null));
        assertEquals(0, help.testZombie.attack(help.testCharacter, null, null));
        assertEquals(4, help.testVampire.attack(help.testCharacter, null, null));
    }

    /**
     * Property test the shield 60% resisting vampiredebuffs via Unit Test
     */
    @Test
    public void shieldResistVampDebuffTest() {

        BasicHelper help = new BasicHelper();

        int success = 0;
        int fail = 0;

        for (int tick = 0; tick < 1000; tick++) {
            boolean result = help.testShield.resistStatus(new VampireDebuff());
            if (result) {
                success++;
            } else {
                fail++;
            }
        }
        // On average should block 60% of the time
        assertEquals(success, 600, 10);
        assertEquals(fail, 400, 10);
    }
    /**
     * Seeded Test that the shield reduces the chance of critical vampire debuffs from crit attacks
     */
    @Test
    public void shieldBattleTest() {

        BasicHelper help = new BasicHelper();

        help.testCharacter.equip(help.testShield);
        help.testShield.setMaxDurability(100);
        help.testCharacter.setMaxHP(10000);
        help.testCharacter.setCurrHP(10000);

        //help.testVampire.critAttack(help.testCharacter);
        help.testVampire.setCritChance(0);
        int roll;
        Random random = new Random(2511);
        for (int attacks = 20; attacks > 0; attacks--) {
            // clean the character
            help.testCharacter.clearStatuses();
            // apply the debuff
            help.testVampire.critAttack(help.testCharacter, null, null);
            roll = random.nextInt(100);
            if (roll < 60) {
                // Predict that the shield will block the attack
                help.testCharacter.setCurrHP(100);
                help.testVampire.attack(help.testCharacter, null, null);
                // Vampire deals normal damage
                assertTrue(help.testCharacter.getCurrHP() >= 86);
            } else {
                // Predict that the shield will not block the attack
                help.testCharacter.setCurrHP(100);
                help.testVampire.attack(help.testCharacter, null, null);
                // Vampire deals critical extra damage
                assertTrue(help.testCharacter.getCurrHP() <= 86);
            }
        }
 
    }

    /**
     * Unit Test that the shield doesnt resist other statuses
     */
    @Test
    public void shieldNoResistTest() {

        BasicHelper help = new BasicHelper();

        for (int tick = 0; tick < 100; tick++) {
            assertFalse(help.testShield.resistStatus(new Zombified()));
        }
    }

    /**
     * Test that the helmet reduces incoming attacks by 10% before
     * the base defense is applied
     * Also test that wearing the helmet reduces outgoing attacks by 1atk
     */
    @Test
    public void helmetTest() {

        BasicHelper help = new BasicHelper();

        assertEquals(help.testCharacter.getCurrATK(), 2);
        help.testCharacter.equip(help.testHelmet);
        assertEquals(help.testCharacter.getCurrATK(), 1);
        // set crits to 0%
        help.testCharacter.setCritChance(0);
        help.testSlug.setCritChance(0);
        help.testZombie.setCritChance(0);
        help.testVampire.setCritChance(0);

        // Final damage dealt to the character should be decreased by 10% and decrease base defense
        assertEquals(1.6, help.testSlug.attack(help.testCharacter, null, null), 0.01);
        assertEquals(5.2, help.testZombie.attack(help.testCharacter, null, null), 0.01);
        assertEquals(12.4, help.testVampire.attack(help.testCharacter, null, null), 0.01);
        
        help.testCharacter.equip(help.testSword);
        assertEquals(help.testCharacter.getCurrATK(), 6);

        // Character should also deal less damage due to decreased visibility
        assertEquals(help.testCharacter.attack(help.testSlug, null, null) , 6.0, 0.01); //slug def = 0
        assertEquals(help.testCharacter.attack(help.testZombie, null, null) , 5.0, 0.01); //zombie def = 1
        assertEquals(help.testCharacter.attack(help.testVampire, null, null) , 3.0, 0.01); //vamp def = 3

    }

    /**
     * Test the damage reduction of tree stump against normal enemies and bosses
     */
    @Test
    public void testTreeStump() {

        BasicHelper help = new BasicHelper();
        // set crits to 0%
        help.testCharacter.setCritChance(0);
        help.testSlug.setCritChance(0);
        help.testZombie.setCritChance(0);
        help.testVampire.setCritChance(0);
        help.testDoggie.setCritChance(0);
        help.testElanMuske.setCritChance(0);

        help.testCharacter.equip(help.testTreeStump);
        
        // Final damage dealt to the character should be decreased by 40% and decrease base defense
        assertEquals(0.4, help.testSlug.attack(help.testCharacter, null, null), 0.01); // 4 * 0.6 - 2
        assertEquals(2.8, help.testZombie.attack(help.testCharacter, null, null), 0.01); // 8 * 0.6 - 2
        assertEquals(7.6, help.testVampire.attack(help.testCharacter, null, null), 0.01); // 16 * 0.6 - 2
        assertEquals(3, help.testDoggie.attack(help.testCharacter, null, null), 0.01); // 25 * 0.2 - 2
        assertEquals(6, help.testElanMuske.attack(help.testCharacter, null, null), 0.01); // 40 * 0.2 - 2
    }

    /**
     * Test the damage reduction of protection items on each tier
     * Each tier multiplies the defense attribute by the star amount
     * eg, star = 2: def * 2
     */
    @Test
    public void testProtectionTier() {
        BasicHelper help = new BasicHelper();
        help.testVampire.setCritChance(0);
        help.testCharacter.setMaxHP(100000);
        help.testCharacter.setCurrHP(100000);

        // Test the armour at different tiers
        // Reset the defense before each test
        help.testCharacter.equip(help.testArmour);
        help.testArmour.setDEF(3);
        help.testArmour.setStar(1, help.testCharacter);
        assertEquals(4, help.testVampire.attack(help.testCharacter, null, null));
        help.testArmour.setDEF(3);
        help.testArmour.setStar(2, help.testCharacter);
        assertEquals(1, help.testVampire.attack(help.testCharacter, null, null));
        help.testArmour.setDEF(3);
        help.testArmour.setStar(3, help.testCharacter);
        assertEquals(-2, help.testVampire.attack(help.testCharacter, null, null));
        help.testCharacter.getInventory().getEquipComposite().removeByType("Armour");

        // Test the helmet at different tiers
        // Reset the defense before each test
        help.testHelmet.setDEF(1);
        help.testCharacter.equip(help.testHelmet);
        help.testHelmet.setStar(1, help.testCharacter);
        assertEquals(12.4, help.testVampire.attack(help.testCharacter, null, null));
        help.testHelmet.setDEF(1);
        help.testHelmet.setStar(2, help.testCharacter);
        assertEquals(11.4, help.testVampire.attack(help.testCharacter, null, null));
        help.testHelmet.setDEF(1);
        help.testHelmet.setStar(3, help.testCharacter);
        assertEquals(10.4, help.testVampire.attack(help.testCharacter, null, null));
        help.testCharacter.getInventory().getEquipComposite().removeByType("Helmet");

        // Test the shield at different tiers
        // Reset the defense before each test
        help.testShield.setDEF(1);
        help.testCharacter.equip(help.testShield);
        help.testShield.setStar(1, help.testCharacter);
        assertEquals(14, help.testVampire.attack(help.testCharacter, null, null));
        help.testShield.setDEF(1);
        help.testShield.setStar(2, help.testCharacter);
        assertEquals(13, help.testVampire.attack(help.testCharacter, null, null));
        help.testShield.setDEF(1);
        help.testShield.setStar(3, help.testCharacter);
        assertEquals(12, help.testVampire.attack(help.testCharacter, null, null));
        help.testCharacter.getInventory().getEquipComposite().removeByType("Shield");

        // Test the tree stump at different tiers
        // Reset the defense before each test
        help.testTreeStump.setDEF(1);
        help.testCharacter.equip(help.testTreeStump);
        help.testTreeStump.setStar(1, help.testCharacter);
        assertEquals(7.6, help.testVampire.attack(help.testCharacter, null, null));
        help.testTreeStump.setDEF(1);
        help.testTreeStump.setStar(2, help.testCharacter);
        assertEquals(6.6, help.testVampire.attack(help.testCharacter, null, null));
        help.testTreeStump.setDEF(1);
        help.testTreeStump.setStar(3, help.testCharacter);
        assertEquals(5.6, help.testVampire.attack(help.testCharacter, null, null));
    
    }

    @Test
    public void testListEquipment(){
        BasicHelper help = new BasicHelper();
        help.testCharacter.equip(help.testSword);
        String equips = "[Equipment:  Sword]";
        assertEquals(equips,help.testCharacter.getInventory().getEquipComposite().listEquipment());

    }

    @Test
    public void testWeapon(){
        BasicHelper help = new BasicHelper();
        help.testCharacter.equip(help.testSword);
        Weapon weapon = help.testCharacter.getInventory().getEquipComposite().getWeapon();
        assert(weapon instanceof Sword);
    }

}
