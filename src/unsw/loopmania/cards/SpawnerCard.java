package unsw.loopmania.cards;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.LoopManiaWorld;

/**
 * Subclass of card which represents spawners adjacent to the path
 */
public abstract class SpawnerCard extends Card {

    public SpawnerCard(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
    }

    public boolean canPlaceBuilding(int x, int y, LoopManiaWorld world) {
        if (world.adjacentToPath(x, y)) {
            return true;
        }
        return false;
    }
    


}
