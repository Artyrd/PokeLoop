package test.enemiesTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import test.testHelpers.BasicHelper;
import unsw.loopmania.enemies.ElanMuskeReward;
import unsw.loopmania.enemies.SlugReward;
import unsw.loopmania.enemies.StageBossReward;
import unsw.loopmania.enemies.VampireReward;
import unsw.loopmania.enemies.ZombieReward;
import unsw.loopmania.enemies.DoggieReward;
import unsw.loopmania.enemies.*;


/**
 * Testing for reward behaviour of different enemy types. Each enemy has a reward class which, when 
 * called, will randomly generate rewards in a String list based on the loot each enemy is expected
 * to drop.
 */
public class rewardTest {
    
    /**
     * A slug can drop at most 2 items and 2 cards.
     * The items can be Sword, Potion, Armour, Shield, Helmet or Staff.
     * The cards can be Trap, Zombie Pit or Campfire.
     * Not all items and cards have the same drop rate.
     * A slug has up to a 80% multiplier of dropping 5g.
     */
    @Test
    void testSlugReward(){
        
        // Generate all possible rewards a slug can drop
        SlugReward slugReward = new SlugReward();
        List<String> slugLoot = slugReward.generateAllRewards();  

        // Tests the amount of gold given from a slug is less than 5
        String lastItem = slugLoot.get(slugLoot.size() - 1);
        int gold = Integer.parseInt(lastItem);
        assert(gold <= 5);
        slugLoot.remove(slugLoot.size() - 1);  
    
        // Create a list of all possible loot that can be dropped by the slug
        String[] possibleLoot = 
        {
            "Sword", "Potion", "Armour", "Shield", "Helmet", "Staff",
            "TrapCard", "ZombiePitCard", "CampfireCard", "TheOneRing", "Anduril", "TreeStump"
        };
        List<String> possibleLootList = Arrays.asList(possibleLoot);

        // Tests the number of slug rewards is under 4 (max 2 items, 2 cards and 1 sack of gold)
        assert(slugLoot.size() <= 5);
        
        // Tests all loot generated is within the possible loot table
        for(String loot : slugLoot){
            assert(possibleLootList.contains(loot));
        }

        // Tests the experienced from a slug is 20
        assert(slugReward.generateExperience() == 20);

        System.out.println("Successfully passed slug test");

    }

    /**
     * A zombie can drop at most 3 items and 3 cards.
     * The items can be Sword, Potion, Armour, Shield, Helmet, Staff, Stake.
     * The cards can be Tower, Village, Barracks, Vampire Castle, Zombie Pit, Campfire.
     * Not all items and cards have the same drop rate.
     * A zombie has a 100% chance of dropping up to 7g, and up to a 60% multiplier of an extra 12g.
     */
    @Test
    void testZombieReward(){
        
        // Generate all possible rewards a zombie can drop
        ZombieReward zombieReward = new ZombieReward();
        List<String> zombieLoot = zombieReward.generateAllRewards();
        
        String lastItem = zombieLoot.get(zombieLoot.size() - 1);
        int gold = Integer.parseInt(lastItem);
        assert(gold >= 7);
        assert(gold <= 14);
        zombieLoot.remove(lastItem);

        String[] possibleLoot = 
        {
            "Sword", "Potion", "Armour", "Shield", "Helmet", "Staff", "Stake", 
            "TowerCard", "VillageCard", "BarracksCard", "VampireCastleCard", 
            "ZombiePitCard", "CampfireCard", "TheOneRing", "Anduril", "TreeStump"
        };
    
        List<String> possibleLootList = Arrays.asList(possibleLoot);

        // A zombie can drop at most 6 entities (3 items and 3 cards) and two sacks of gold
        assert(zombieLoot.size() <= 8);
        
        for(String loot : zombieLoot){
            assert(possibleLootList.contains(loot));
        }

        assert(zombieReward.generateExperience() == 40);

        System.out.println("Successfully passed zombie test");
    }

    /**
     * A vampire can drop at most 3 items and 3 cards.
     * The items can be Sword, Potion, Shield, Armour, Helmet, Staff or Stake.
     * The cards can be Zombie Pit, Campfire, Tower, Village, Barracks, Vampire Castle.
     * Not all items and cards have the same drop rate.
     * A vampire has a 100% chance of dropping up to 15g, and up to a 40% multiplier of an extra 25g.
     */
    @Test
    void testVampireReward(){
        VampireReward vampireReward = new VampireReward();
        List<String> vampireLoot = vampireReward.generateAllRewards();

        String lastItem = vampireLoot.get(vampireLoot.size() - 1);
        int gold = Integer.parseInt(lastItem);
        assert(gold >= 15);
        assert(gold <= 25);
        vampireLoot.remove(lastItem);

        String[] possibleLoot = 
        {
            "Sword", "Potion", "Shield", "Armour", "Helmet", "Staff", "Stake", 
            "ZombiePitCard", "CampfireCard", "TowerCard", "VillageCard", 
            "BarracksCard", "VampireCastleCard", "TheOneRing", "Anduril", "TreeStump"
        };
    
        List<String> possibleLootList = Arrays.asList(possibleLoot);

        // A vampire can drop at most 6 entities (3 items and 3 cards) and two sacks of gold
        assert(vampireLoot.size() <= 9);
        
        for(String loot : vampireLoot){
            System.err.println(loot);
            assert(possibleLootList.contains(loot));
        }

        assert(vampireReward.generateExperience() == 100);

        System.out.println("Successfully passed vampire test");

    }

     /**
     * Test the gold rewards from elan
     */
    @Test
    void testElanMuskeGold(){
        ElanMuskeReward elanMuskeReward = new ElanMuskeReward();
        List<String> elanLoot = elanMuskeReward.generateAllRewards();

        String lastItem = elanLoot.get(elanLoot.size() - 1);
        int gold = Integer.parseInt(lastItem);
        assert(gold >= 400);
        assert(gold <= 650);
    }

    /**
     * Test the rewards from doggie coin
     * Includes one instance of Doggie coin
     */
    @Test 
    void testDoggieReward(){
        DoggieReward doggieReward = new DoggieReward();
        List<String> doggieLoot = doggieReward.generateAllRewards();
        // Check doggie gives doggie coin
        assert(doggieLoot.get(doggieLoot.size() - 1) == "DoggieCoin");

        // Check gold reward from doggie
        String goldItem = doggieLoot.get(doggieLoot.size() - 2);
        int gold = Integer.parseInt(goldItem);
        assert(gold >= 150);
        assert(gold <= 250);
        doggieLoot.remove(goldItem);
        doggieLoot.remove(doggieLoot.get(doggieLoot.size() - 1));

        String[] possibleLoot = 
        {
            "Sword", "Potion", "Shield", "Armour", "Helmet", "Staff", "Stake", 
            "ZombiePitCard", "CampfireCard", "TowerCard", "VillageCard", 
            "BarracksCard", "VampireCastleCard", "TheOneRing", "Anduril", "TreeStump"
        };
    
        // Check that doggie can give the above possible loot, other than gold and doggie coin
        List<String> possibleLootList = Arrays.asList(possibleLoot);
        BasicHelper helper = new BasicHelper();
        Doggie doggie = helper.testDoggie;
        List<String> rewards = doggie.giveReward();
        rewards.remove(rewards.size() - 1);
        rewards.remove(rewards.size() - 1);
        for(String reward: rewards){
            assertTrue(possibleLootList.contains(reward));
        }

        // Loot can contain 4 items, 1 rare item, 3 cards
        assert(doggieLoot.size() <= 8);

        for(String item: doggieLoot){
            assertTrue(possibleLootList.contains(item));
        }

    }

    @Test
    void testStageBossReward(){
        int[] gold = {100};
        int xp = 1000;
        StageBossReward stageBossReward = new StageBossReward(gold, xp); 
        List<String> bossLoot = stageBossReward.generateAllRewards();
        int goldLoot = Integer.parseInt(bossLoot.get(0));

        assert(goldLoot == 100);

    }



    /**
     * Test the reward behaviour of elan
     */
    @Test
    void testElanReward(){
        BasicHelper helper = new BasicHelper();
        ElanMuske elan = helper.testElanMuske;

        // Check the cards that elan can reward 
        List<String> cards = elan.generateCardRewards();
        String[] availCards = {"ZombiePitCard", "CampfireCard", "TowerCard", "VillageCard", "BarracksCard", "VampireCastleCard"};
        List<String> availCardsList = Arrays.asList(availCards);
        for(String card: cards){
            assertTrue(availCardsList.contains(card));
        }

        // Check the items that elan can reward
        List<String> items = elan.generateItemRewards();
        String[] availItems = {"Sword", "Potion", "Armour", "Shield", "Helmet", "Staff", "Stake"};
        List<String> availItemsList = Arrays.asList(availItems);
        for(String item: items){
            assertTrue(availItemsList.contains(item));
        }

        // Check the gold that elan can reward
        int gold = elan.generateGoldRewards();
        assert(gold >= 400);
        assert(gold <= 650);

        assert(elan.generateExperience() == 3000);

    }

}
