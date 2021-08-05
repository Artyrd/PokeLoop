package unsw.loopmania.items;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.movement.PathPosition;

public class Potion extends SpawnableItem {

    public Potion (SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
        setValue();
        setName("Health Potion");
    }

    /**
     * When creating a Potion item in the world:
     * @param pathPosition the path position in the world
     */
    public Potion(PathPosition pathPosition) {
        super(pathPosition);
        value = 0;
        setValue();
    }

    // Each potion costs 10 to buy and heals for 30
    private void setValue() {
        setID("Potion");
        setPrice(10);
        setValue(30);
        
    }
}
