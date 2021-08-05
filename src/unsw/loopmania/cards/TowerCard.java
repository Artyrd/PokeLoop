package unsw.loopmania.cards;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.buildings.TowerBuilding;

/**
 * represents a vampire castle card in the backend game world
 */
public class TowerCard extends Card {
    
    public TowerCard(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
        setID("TowerCard");
        setBuilding(new TowerBuilding(x, y));
        setName("Regirock's Tower Card");
        setValue(10);
    }

    @Override
    public boolean canPlaceBuilding(int x, int y, LoopManiaWorld world) {
        if (world.adjacentToPath(x, y)) {
            return true;
        }
        return false;
    }    
    
}
