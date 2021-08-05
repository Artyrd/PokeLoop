package test.battleTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Test;

import javafx.beans.property.SimpleIntegerProperty;
import test.testHelpers.BasicHelper;
import unsw.loopmania.MovingEntity;
import unsw.loopmania.buildings.TowerBattler;
import unsw.loopmania.buildings.TowerBuilding;
import unsw.loopmania.enemies.*;
import unsw.loopmania.Character;
import unsw.loopmania.Ally;
import unsw.loopmania.battle.BattleSimulator;

public class AutoBattleTest {
   
    /**
     * Simple testing of battling, if hero successfully kills a single slug
     */
    @Test
    public void testBasicAutoBattle() {
        BasicHelper helper = new BasicHelper();
        // Set up character and enemies
        Character character = helper.testCharacter;
        Slug slug = helper.testSlug;
        ArrayList<MovingEntity> fighters = new ArrayList<MovingEntity>();
        fighters.add(slug);
        BattleSimulator battler = new BattleSimulator(character, fighters, null);
        
        boolean battleResult = battler.runBattle();
        // The character should win a battle against one slug
        assertTrue(battleResult);
        // Check the health of the slug, should be 0 and defeated
        assertEquals(slug.getCurrHP(), 0, 0.001);
        assertTrue(battler.getDefeated().contains(slug));
        // Check the health of the character, should be damaged but not defeated
        assertTrue(character.getCurrHP() < character.getMaxHP());
        assertTrue(character.getCurrHP() > 0);
    }

    /**
     * Testing of battling multiple slugs
     */
    @Test
    public void testAutoBattleMoreEnemies() {
        BasicHelper helper = new BasicHelper();
        Character character = helper.testCharacter;

        // Level up the character and improve stats
        character.gainXP(700);
        // 700xp should get to level 5
        assertEquals(character.getLevel(), 5);
        // Equip a sword and increase attack damage
        character.equip(helper.testSword);
        assertEquals(character.getCurrATK(), 9, 0.001); // 4atk base + 5atk from sword

        // Spawn 5 slugs
        int numSlugs = 5;
        ArrayList<MovingEntity> fighters = new ArrayList<MovingEntity>();
        for (int count = numSlugs; count > 0; count --) {
            Slug newSlug = new Slug(null);
            fighters.add(newSlug);
        }
        BattleSimulator battler = new BattleSimulator(character, fighters, null);
        
        boolean battleResult = battler.runBattle();
        // The character should win the battle, but sustain some damage
        assertTrue(battleResult);
        assertTrue(character.getCurrHP() < character.getMaxHP());
        assertTrue(character.getCurrHP() > 0);
        // All slugs should be defeated
        assertEquals(battler.getDefeated().size(), numSlugs);
    }

    /**
     * Testing of battling different enemies
     */
    @Test
    public void testAutoBattleMixedEnemies() {
        BasicHelper helper = new BasicHelper();
        Character character = helper.testCharacter;

        // Level up the character and improve stats
        character.gainXP(2700); 
        // 2700 xp should get to level 10
        assertEquals(character.getLevel(), 10);
        // Equip a sword and increase attack damage
        character.equip(helper.testSword);
        assertEquals(character.getCurrATK(), 11.5, 0.001); // 6.5atk base + 5atk from sword
        character.equip(helper.testArmour);

        // Spawn enemies of each type
        ArrayList<MovingEntity> fighters = new ArrayList<MovingEntity>();
        fighters.add(helper.testSlug);
        fighters.add(helper.testZombie);
        fighters.add(helper.testVampire);

        BattleSimulator battler = new BattleSimulator(character, fighters, null);
        
        boolean battleResult = battler.runBattle();
        // The character should lose the battle
        assertFalse(battleResult);
        // Character should be defeated
        assertTrue(character.getCurrHP() <= 0);
        // Slug and zombie should be defeated, but vampire survives
        assertEquals(helper.testSlug.getCurrHP(), 0, 0.01);
        assertEquals(helper.testZombie.getCurrHP(), 0, 0.01);
        assertTrue(helper.testVampire.getCurrHP() > 0);
        assertEquals(battler.getDefeated().size(), 2);

    }

    /**
     * Now give the character some potions to use in battle
     */
    @Test
    public void testAutoBattleMixedEnemiesWithPotions() {
        BasicHelper helper = new BasicHelper();
        Character character = helper.testCharacter;

        // Level up the character and improve stats
        character.gainXP(2700); 
        // 2700 xp for lvl 10
        assertEquals(character.getLevel(), 10);
        // Equip a sword and increase attack damage
        character.equip(helper.testSword);
        assertEquals(character.getCurrATK(), 11.5, 0.001); // 6.5atk base + 5atk from sword
        // Equip armour
        character.equip(helper.testArmour);

        // Spawn 25 potions for use in battle
        for(int i = 0; i < 25; i++){
            character.addItem("Potion");
        }

        assert(character.getPotions() == 25);

        // Spawn enemies of each type
        ArrayList<MovingEntity> fighters = new ArrayList<MovingEntity>();
        fighters.add(helper.testSlug);
        fighters.add(helper.testZombie);
        fighters.add(helper.testVampire);

        BattleSimulator battler = new BattleSimulator(character, fighters, null);
        
        boolean battleResult = battler.runBattle();
        // The character should win the battle, but sustain some damage
        assertTrue(battleResult);
        assertTrue(character.getCurrHP() < character.getMaxHP());
        assertTrue(character.getCurrHP() > 0);
        // All enemies should be defeated
        assertEquals(helper.testSlug.getCurrHP(), 0, 0.01);
        assertEquals(helper.testZombie.getCurrHP(), 0, 0.01);
        assertEquals(helper.testVampire.getCurrHP(), 0, 0.01);
        assertEquals(battler.getDefeated().size(), 3);

    }

    /**
     * Test if the character dies
     */
    @Test
    public void testKillCharacterAutoBattle() {
        BasicHelper helper = new BasicHelper();
        Character character = helper.testCharacter;

        // Spawn 10 vampires to ensure character defeat
        ArrayList<MovingEntity> fighters = new ArrayList<MovingEntity>();
        for (int count = 10; count > 0; count --) {
            Vampire newVampire = new Vampire(null);
            fighters.add(newVampire);
        }
        BattleSimulator battler = new BattleSimulator(character, fighters, null);
        
        boolean battleResult = battler.runBattle();
        // Character should lose and have depleted health
        assertFalse(battleResult);
        assertTrue(character.getCurrHP() == 0);
    }

    /**
     * Testing of battling with allies and different enemies
     */
    @Test
    public void testAutoBattleAlliesAndEnemies() {
        BasicHelper helper = new BasicHelper();
        Character character = helper.testCharacter;

        // Level up the character and improve stats
        character.gainXP(2700); 
        // 2700 xp for lvl 10
        assertEquals(character.getLevel(), 10);
        // Equip a sword and increase attack damage
        character.equip(helper.testSword);
        assertEquals(character.getCurrATK(), 11.5, 0.001); // 6.5atk base + 5atk from sword
        // Equip some armour
        character.equip(helper.testArmour);

        // Spawn 5 enemies of not vampire type
        ArrayList<MovingEntity> fighters = new ArrayList<MovingEntity>();
        fighters.add(new Slug(null));
        fighters.add(new Slug(null));
        fighters.add(new Zombie(null));
        fighters.add(new Zombie(null));
        fighters.add(new Zombie(null));

        // Spawn 3 allies
        fighters.add(new Ally());
        fighters.add(new Ally());
        fighters.add(new Ally());

        BattleSimulator battler = new BattleSimulator(character, fighters, null);
        
        boolean battleResult = battler.runBattle();
        // The character's team should win the battble but sustain some damage
        assertTrue(battleResult);
        assertTrue(character.getCurrHP() < character.getMaxHP());
        assertTrue(character.getCurrHP() > 0);
        // Make sure only 5 enemies are defeated
        assertEquals(battler.getDefeated().size(), 5); 

    }
    
    /**
     * Testing of battling with trancing, allies, and different enemies
     */
    @Test
    public void testAutoBattleTrance() {
        BasicHelper helper = new BasicHelper();
        Character character = helper.testCharacter;

        // Level up the character and improve stats
        character.gainXP(50000);
        character.restoreHP(999);
        // Equip a staff and armour
        character.equip(helper.testStaff);
        character.equip(helper.testArmour);

        ArrayList<MovingEntity> fighters = new ArrayList<MovingEntity>();
        /* create 5 enemies and 5 allies */
        fighters.add(new Slug(null));
        fighters.add(new Slug(null));
        fighters.add(new Zombie(null));
        fighters.add(new Zombie(null));
        fighters.add(new Vampire(null));

        fighters.add(new Ally());
        fighters.add(new Ally());
        fighters.add(new Ally());
        fighters.add(new Ally());
        fighters.add(new Ally());

        BattleSimulator battler = new BattleSimulator(character, fighters, null);
        
        boolean battleResult = battler.runBattle();
        // character should win the battle, but sustain some damage
        assertTrue(battleResult);
        assertTrue(character.getCurrHP() < character.getMaxHP());
        // make sure only 5 enemies are defeated
        System.out.println(battler.getDefeated().toString());
        assertEquals(battler.getDefeated().size(), 5); 

    }

    @Test
    public void testBattleWithPotions() {
        BasicHelper helper = new BasicHelper();
        Character character = helper.testCharacter;

        // Spawn 25 potions for use in battle
        character.equip(helper.testSword);
        for(int itemNum = 0; itemNum < 25; itemNum++){
            character.addItem("Potion");
        }
        assert(character.getPotions() == 25);

       // Equip a sword and armour
       character.equip(helper.testSword);
       character.equip(helper.testArmour);

        // Spawn 3 zombie enemies
        ArrayList<MovingEntity> fighters = new ArrayList<MovingEntity>();
        for(int fighterNum = 0; fighterNum < 3; fighterNum++){
            fighters.add(new Zombie(null));
        }

        BattleSimulator battler = new BattleSimulator(character, fighters, null);
        boolean battleResult = battler.runBattle();
        // Character should win the battle, but sustain some damage
        assertTrue(battleResult);
        assertTrue(character.getCurrHP() < character.getMaxHP());
        assertTrue(character.getCurrHP() > 50);

    }

    @Test
    public void testBattleWithTowers() {
        BasicHelper helper = new BasicHelper();
        Character character = helper.testCharacter;

        ArrayList<MovingEntity> fighters = new ArrayList<MovingEntity> ();
        ArrayList<MovingEntity> structures = new ArrayList<MovingEntity>();
        // Level up the character and equip a sword
        character.gainXP(1000);
        character.equip(helper.testSword);

        // Spawn 3 zombies, and 3 towers for battle
        for(int fighterNum = 0; fighterNum < 3; fighterNum++){
            fighters.add(new Zombie(null));
            TowerBuilding tower = new TowerBuilding(new SimpleIntegerProperty(fighterNum), new SimpleIntegerProperty(1));
            structures.add(new TowerBattler(tower));
        }

        BattleSimulator battler = new BattleSimulator(character, fighters, structures);
        boolean battleResult = battler.runBattle();
        // The character should win the battle
        assertTrue(battleResult);

    }

    @Test
    public void testBasicAutoBattleWithElan() {
        BasicHelper helper = new BasicHelper();
        
        // Set up character and enemies
        Character character = helper.testCharacter;
        ElanMuske elanMuske = helper.testElanMuske;
        Slug slug = helper.testSlug;
        ArrayList<MovingEntity> fighters = new ArrayList<MovingEntity>();
        fighters.add(slug);
        fighters.add(elanMuske);
        elanMuske.setCritChance(100);
        BattleSimulator battler = new BattleSimulator(character, fighters, null);
        
        boolean battleResult = battler.runBattle();
        // The character should win a battle against one slug
        assertFalse(battleResult);
        // Check the health of the slug, should be 0 and defeated
        assertTrue(slug.getCurrHP() == slug.getMaxHP());

        // Check the health of the character, should be damaged but not defeated
        assertTrue(character.getCurrHP() == 0);
        assertTrue(elanMuske.getCurrHP() > 0);
    }
        
}