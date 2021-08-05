package unsw.loopmania.cards;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.buildings.TrapBuilding;

/**
 * represents a vampire castle card in the backend game world
 */
public class TrapCard extends Card {
    
    public TrapCard(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
        setID("TrapCard");
        setBuilding(new TrapBuilding(x, y));
        setName("Electric Trap Card");
        setValue(5);
    }

    @Override
    public boolean canPlaceBuilding(int x, int y, LoopManiaWorld world) {
        if (world.isPath(x, y)) {
            return true;
        }
        return false;
    }    
}
