package unsw.loopmania.game_mode;

import org.javatuples.Pair;
import java.util.List;
import java.util.Random;

import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.items.ConfusedItem;
import unsw.loopmania.items.Item;

public class ConfusingWorld extends LoopManiaWorld {

    public ConfusingWorld(int width, int height, List<Pair<Integer, Integer>> orderedPath) {
        super(width,  height, orderedPath);
        setGamemode("Confusing");
    }

    @Override
    public Item generateRareItem(int bound) {
        //return super.generateRareItem(bound);

        // obtain the possible rare items from parent world
        List<Item> possibleRareItems = getPossibleRareItems(); 
        if (possibleRareItems.size() == 0) {
            return null;
        }
        // 1/bound chance of not returning null
        Random random = new Random();
        int chance = random.nextInt(bound);
        if (chance != 0) {
            return null;
        }
        // got a hit! return a confused rare item
        ConfusedItem rareItem = new ConfusedItem();
        // give the possible items it can make combinations of
        rareItem.setWorldRareItems(possibleRareItems); 

        return rareItem;
    }
    
}
