package unsw.loopmania.enemies;

public class ElanMuskeReward extends RewardBehaviour {

    private static String[] items = {"Sword", "Potion", "Armour", "Shield", "Helmet", "Staff", "Stake"};
    private static int[] itemPercentages = {70, 70, 70, 70, 70, 70, 70};
    private static int maxItems = 4;
    private static String[] cards = {"ZombiePitCard", "CampfireCard", "TowerCard", "VillageCard", "BarracksCard", "VampireCastleCard"};
    private static int[] cardPercentages = {70, 70, 70, 70, 70, 70};
    private static int maxCards = 4;
    private static int[] gold = {400, 650};
    private static int[] goldPercentages = {100, 40};

    public ElanMuskeReward() {
        super(items, itemPercentages, maxItems, cards, cardPercentages, maxCards, gold, goldPercentages, 3000);
    }
    
}
