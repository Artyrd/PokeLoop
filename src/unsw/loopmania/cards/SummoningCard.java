package unsw.loopmania.cards;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.LoopManiaWorld;

/**
 * subclass of card that represents a card that summons an entity be it a Boss or another special MovingEntity
 */
public abstract class SummoningCard extends Card {

    public SummoningCard(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
    }

    /**
     * Bosses can only be spawned on the path
     */
    public boolean canPlaceBuilding(int x, int y, LoopManiaWorld world) {
        if (world.isPath(x, y)) {
            return true;
        }
        return false;
    }
    


}
