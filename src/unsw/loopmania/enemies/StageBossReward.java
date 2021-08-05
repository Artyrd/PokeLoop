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
public class StageBossReward extends RewardBehaviour{

    private static String[] items = {};
    private static int[] itemPercentages = {};
    private static int maxItems = 0;
    private static String[] cards = {};
    private static int[] cardPercentages = {};
    private static int maxCards = 0;
    //private static int[] gold = {99};
    private static int[] goldPercentages = {100};

    public StageBossReward(int[] gold, int xp){
        super(items, itemPercentages, maxItems, cards, cardPercentages, maxCards, gold, goldPercentages, xp);
    }
    
}
