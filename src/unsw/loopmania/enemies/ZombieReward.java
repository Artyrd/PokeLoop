package unsw.loopmania.enemies;

/**
 * ZombieReward subclass which will be instantiated when a zombie dies.
 * 
 * A zombie can drop items specified in array items, with corresponding 
 * percentages in itemPercentages. Cards that can be dropped are specified 
 * in array cards with corresponding percentages in cardPercentages. Maximum 
 * number of items and cards dropped by a zombie is 3 each.
 * 
 * A zombie has a 100% chance of dropping up to 7g, and up to a 60% multiplier of an extra 12g.
 */
public class ZombieReward extends RewardBehaviour {
    
    private static String[] items = {"Sword", "Potion", "Shield", "Armour", "Helmet", "Staff", "Stake"};
    private static int[] itemPercentages = {40, 40, 40, 30, 30, 30, 15};
    private static int maxItems = 3;
    private static String[] cards = {"ZombiePitCard", "TowerCard", "VillageCard", "CampfireCard", "BarracksCard", "VampireCastleCard"};
    private static int[] cardPercentages = {50, 25, 25, 25, 20, 20};
    private static int maxCards = 3;
    private static int[] gold = {7, 12};
    private static int[] goldPercentages = {100, 60};

    public ZombieReward(){
        super(items, itemPercentages, maxItems, cards, cardPercentages, maxCards, gold, goldPercentages, 40);
    }
}
