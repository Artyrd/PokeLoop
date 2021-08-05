package unsw.loopmania.buildings;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.StaticEntity;

/**
 * A building class is a superclass to: 
 *      - Vampire Castle
 *      - Zombie Pit
 *      - Tower
 *      - Village
 *      - Barracks
 *      - Trap
 *      - Campfire
 *      - Hero's Castle 
 * This class has a set position on the map and have and a buildingEffect depending 
 * on the position of the character, position of enemies and the current loop 
 * number.
 */
public abstract class Building extends StaticEntity {

    public Building(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
    }

    public void update(LoopManiaWorld world) {}

}
