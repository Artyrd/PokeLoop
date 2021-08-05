package unsw.loopmania.items;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.movement.PathPosition;

public class SpawnableItem extends Item {

    protected int value;
    private PathPosition position;
   
    /**
     * When creating an item in the inventory:
     * @param x inventory x coord
     * @param y inventory y coord
     */
    public SpawnableItem(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
        value = 0; // so getValue doesnt throw null errors
        position = null;
    }
 
    /**
     * When creating an item in the world:
     * @param pathPosition the path position in the world
     */
    public SpawnableItem(PathPosition pathPosition) {
        super(null, null);
        value = 0; // so getValue doesnt throw null errors
        this.position = pathPosition;
    }


    public int getValue() {
        return value;
    }
    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public int getX() {
        return x().get();
    }
    @Override
    public int getY() {
        return y().get();
    }

    public SimpleIntegerProperty x() {
        return position.getX();
    }

    public SimpleIntegerProperty y() {
        return position.getY();
    }


}
