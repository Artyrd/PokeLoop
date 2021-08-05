package unsw.loopmania.cards;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.buildings.CampfireBuilding;

/**
 * represents a vampire castle card in the backend game world
 */
public class CampfireCard extends Card {


    public CampfireCard(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
        setID("CampfireCard");
        setBuilding(new CampfireBuilding(x, y));
        setName("Solrock's Campfire Card");
        setValue(5);
    }

    @Override
    public boolean canPlaceBuilding(int x, int y, LoopManiaWorld world) {
        if (world.isPath(x, y)) {
            return false;
        }
        return true;
    }

}
