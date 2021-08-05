package unsw.loopmania.enemies;

import java.util.ArrayList;
import java.util.List;

public class DoggieReward extends RewardBehaviour {

    private static String[] items = {"Sword", "Potion", "Armour", "Shield", "Helmet", "Staff", "Stake"};
    private static int[] itemPercentages = {50, 50, 50, 50, 50, 50, 50};
    private static int maxItems = 4;
    private static String[] cards = {"ZombiePitCard", "CampfireCard", "TowerCard", "VillageCard", "BarracksCard", "VampireCastleCard"};
    private static int[] cardPercentages = {50, 50, 50, 50, 50, 50};
    private static int maxCards = 3;
    private static int[] gold = {150, 250};
    private static int[] goldPercentages = {100, 40};

    public DoggieReward() {
        super(items, itemPercentages, maxItems, cards, cardPercentages, maxCards, gold, goldPercentages, 1000);
    }
    
    @Override
    public List<String> generateAllRewards(){
        List<String> rewards = new ArrayList<String>();
        rewards.addAll(super.generateAllRewards());
        rewards.add("DoggieCoin");
        return rewards;
    }


}
