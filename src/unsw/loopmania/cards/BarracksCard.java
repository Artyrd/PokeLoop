package unsw.loopmania.cards;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.buildings.BarracksBuilding;

/**
 * represents a vampire castle card in the backend game world
 */
public class BarracksCard extends Card {
    public BarracksCard(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
        setID("BarracksCard");
        setBuilding(new BarracksBuilding(x, y));
        setName("Barracks Card");
        setValue(10);
    }

    @Override
    public boolean canPlaceBuilding(int x, int y, LoopManiaWorld world) {
        if (world.isPath(x, y)) {
            return true;
        }
        return false;
    }    
}
