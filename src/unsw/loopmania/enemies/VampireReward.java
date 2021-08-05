package unsw.loopmania.enemies;

/**
 * VampireReward subclass which will be instantiated when a vampire dies.
 * 
 * A vampire can drop items specified in array items, with corresponding 
 * percentages in itemPercentages. Cards that can be dropped are specified 
 * in array cards with corresponding percentages in cardPercentages. Maximum 
 * number of items and cards dropped by a vampire is 3 each.
 * 
 * A vampire has a 100% chance of dropping up to 15g, and up to a 40% multiplier of an extra 25g.
 */
public class VampireReward extends RewardBehaviour {
    
    private static String[] items = {"Sword", "Potion", "Shield", "Armour", "Helmet", "Staff", "Stake"};
    private static int[] itemPercentages = {40, 40, 40, 40, 40, 40, 25};
    private static int maxItems = 3;
    private static String[] cards = {"ZombiePitCard", "CampfireCard", "TowerCard", "VillageCard", "BarracksCard", "VampireCastleCard"};
    private static int[] cardPercentages = {50, 50, 35, 35, 35, 35};
    private static int maxCards = 3;
    private static int[] gold = {15, 25};
    private static int[] goldPercentages = {100, 40};


    public VampireReward(){
        super(items, itemPercentages, maxItems, cards, cardPercentages, maxCards, gold, goldPercentages, 100);
    }
}
