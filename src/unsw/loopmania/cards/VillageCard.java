package unsw.loopmania.cards;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.buildings.VillageBuilding;

/**
 * represents a vampire castle card in the backend game world
 */
public class VillageCard extends Card {
    
    public VillageCard(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
        setID("VillageCard");
        setBuilding(new VillageBuilding(x, y));
        setName("Twinleaf Town Card");
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
