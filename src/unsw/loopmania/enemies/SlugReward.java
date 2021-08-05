package unsw.loopmania.enemies;

/**
 * SlugReward subclass which will be instantiated when a slug dies.
 * 
 * A slug can drop items specified in array items, with corresponding 
 * percentages in itemPercentages. Cards that can be dropped are specified 
 * in array cards with corresponding percentages in cardPercentages. Maximum 
 * number of items and cards dropped by a slug is 2 each.
 * 
 * A slug has up to a 80% multiplier of dropping 5g.
 */
public class SlugReward extends RewardBehaviour{

    private static String[] items = {"Sword", "Potion", "Armour", "Shield", "Helmet", "Staff"};
    private static int[] itemPercentages = {40, 40, 20, 20, 20, 10};
    private static int maxItems = 2;
    private static String[] cards = {"TrapCard", "ZombiePitCard", "CampfireCard"};
    private static int[] cardPercentages = {40, 40, 20};
    private static int maxCards = 2;
    private static int[] gold = {5};
    private static int[] goldPercentages = {80};

    public SlugReward(){
        super(items, itemPercentages, maxItems, cards, cardPercentages, maxCards, gold, goldPercentages, 20);
    }
    
}
