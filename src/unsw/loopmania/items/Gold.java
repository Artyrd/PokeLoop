package unsw.loopmania.items;

import java.util.Random;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.movement.PathPosition;

public class Gold extends SpawnableItem {
    
    public Gold(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
        setValue();
    }
 
    /**
     * When creating a gold item in the world:
     * @param pathPosition the path position in the world
     */
    public Gold(PathPosition pathPosition) {
        super(pathPosition);
        value = 0;
        setValue();
    }

    private void setValue() {
        setID("Gold");
        // Randomly set the value of the gold to be from 5-25
        Random random = new Random();
        int amount = random.nextInt(26);
        if (amount < 5) {
            setValue(5);
        }
        else {
            setValue(amount);
        }
    }

}
