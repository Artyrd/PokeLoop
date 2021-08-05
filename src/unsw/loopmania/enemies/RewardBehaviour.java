package unsw.loopmania.enemies;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A reward class that determines what an enemy will drop as loot based on applying random
 * generation according to the possible loot an enemy can drop.
 * The class will return a String list which can then be referenced to an id table 
 * to spawn the corresponding entity in the world.
 */
public class RewardBehaviour {
    protected String[] items;    // List of items an enemy can drop
    protected int[] itemPercentages;
    protected int maxItems;               // Maximum number of items an enemy can drop 
    protected String[] cards;   // List of cards an enemy can drop
    protected int[] cardPercentages;
    protected int maxCards;               // Maximum number of cards an enemy can drop
    protected int[] goldPercentages;      // Array of percentages that enemy can drop gold
    protected int[] gold;           // Array of gold tiers that enemy can drop
    protected int experience;
    protected String[] rareItems;
    protected int[] rareItemPercentage;

    /**
     * Constructor for RewardBehaviour
     * @param items
     * @param itemPercentages
     * @param maxItems
     * @param cards
     * @param cardPercentages
     * @param maxCards
     * @param goldSacks
     */
    public RewardBehaviour(String[] items, int[] itemPercentages, int maxItems, String[] cards,
            int[] cardPercentages, int maxCards, int[] gold, int[] goldPercentages, int experience)
    {
        this.items = items;
        this.itemPercentages = itemPercentages;
        this.maxItems = maxItems;
        this.cards = cards;
        this.cardPercentages = cardPercentages;
        this.maxCards = maxCards;
        this.goldPercentages = goldPercentages;
        this.gold = gold;
        this.experience = experience;
    }

    /**
     * Method to randomly generate rewards (items or cards) based on what is specified in 
     * the array reward. Can only generate up to a maximum number of rewards maxRewards.
     * @param reward
     * @param maxReward
     * @return List of Strings containing the entities in the loot
     */
    public List<String> generateReward(String[] reward, int[] rewardPercentages, int maxReward){
        List <String> rewards = new ArrayList<String>();
        int available = reward.length;
        int numRewards = (new Random()).nextInt(maxReward + 1);
        int curr = 0;
        while(curr < numRewards){
            int index = (new Random()).nextInt(available);
            if(!rewards.contains(reward[index])){
                if(new Random().nextInt(101) < rewardPercentages[index]){
                    rewards.add(reward[index]);
                } 
            }
            curr++;
        }
        return rewards;
    }

    /**
     * Method to randomly generate gold based on the specified percentages and gold "tiers"
     * in the goldPercentages and gold arrays. 
     * @return integer totalGold an enemy will drop
     */
    public int generateGold(){
        int totalGold = 0;
        for(int goldNum = 0; goldNum < gold.length; goldNum++){
            int goldTier = gold[goldNum];
            int goldTierPercentage = goldPercentages[goldNum];
            int dropPercentage = 0;
            if(goldTierPercentage < 100){
                dropPercentage = (new Random()).nextInt(goldTierPercentage + 1);    // dropPercentage will be from 0 to goldTierPercentage inclusive
            } else {
                dropPercentage = 100;
            }
            double goldDrop = dropPercentage * goldTier/100;
            totalGold += Math.round(goldDrop);
        }
        System.out.println("gold is " + totalGold);

        return totalGold;

    }

    /**
     * Method to randomly generate a rare item based on specified percentages
     * @param rareItems
     * @param rareItemPercentage
     * @return
     */
    public List<String> generateRareItem(String[] rareItems, int[] rareItemPercentage){
        List<String> rareItemReward = new ArrayList<String>();
        List<Integer> percentageList = new ArrayList<Integer>();
        List<String> rareItemList = new ArrayList<String>();

        for(int itemIndx = 0; itemIndx < rareItems.length; itemIndx++){
            percentageList.add(rareItemPercentage[itemIndx]);
            rareItemList.add(rareItems[itemIndx]);
        }
        /*
        // If not all items are 100% percentage, only reward the 100% items
        int numHundredPercent = 0;
        boolean containsHundredPercent = false;
        for(Integer percentage: percentageList){
            if(percentage.equals(100)){
                numHundredPercent++;
                containsHundredPercent = true;
            }
        }*/

        int randomRareItemIndex = (new Random()).nextInt(rareItems.length);
        String rareItem = rareItems[randomRareItemIndex];
        int dropPercentage = rareItemPercentage[randomRareItemIndex];
        if(new Random().nextInt(101) < dropPercentage){
                rareItemReward.add(rareItem);
                System.out.println("Rare item is " + rareItem);
        }
        /*
        if(numHundredPercent < rareItems.length && containsHundredPercent){ // Not all items have 100% percentage, only reward 100%
            List<String> itemsToDrop = new ArrayList<String>();
            for(int itemIndx = 0; itemIndx < rareItems.length; itemIndx++){
                if(rareItemPercentage[itemIndx] >= 100){
                    itemsToDrop.add(rareItems[itemIndx]);
                }
            }
            int randomRareItemIndex = (new Random()).nextInt(itemsToDrop.size());
            System.out.println("Rare item is " + itemsToDrop.get(randomRareItemIndex));
            rareItemReward.add(itemsToDrop.get(randomRareItemIndex));
        } else {    // Randomly choose an item to reward
            int randomRareItemIndex = (new Random()).nextInt(rareItems.length);
            String rareItem = rareItems[randomRareItemIndex];
            int dropPercentage = rareItemPercentage[randomRareItemIndex];
            if(new Random().nextInt(101) < dropPercentage){
                rareItemReward.add(rareItem);
                System.out.println("Rare item is " + rareItem);
            }
        }*/

        return rareItemReward;
    }

    /**
     * Method for generating all rewards an enemy will drop when it dies. This method will be 
     * called by an enemy's rewardBehaviour object to receive all loot.
     * @return List of Strings containing all entities corresponding to loot, the last item 
     *      is a string of gold dropped 
     */
    public List<String> generateAllRewards(){
        List<String> rewards = new ArrayList<String>();
        rewards.addAll(generateReward(items, itemPercentages, maxItems));
        rewards.addAll(generateReward(cards, cardPercentages, maxCards));
        rewards.add(Integer.toString(generateGold()));
        return rewards;
    }

    public List<String> generateCardRewards(){
        return generateReward(cards, cardPercentages, maxCards);
    }

    public List<String> generateItemRewards(){
        return generateReward(items, itemPercentages, maxItems);
    }

    public int generateGoldRewards(){
        return generateGold();
    }

    public int generateExperience(){
        return experience;
    }

}
