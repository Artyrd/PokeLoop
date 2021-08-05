package unsw.loopmania.cards;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.buildings.ZombiePitBuilding;

/**
 * represents a vampire castle card in the backend game world
 */
public class ZombiePitCard extends SpawnerCard {

    public ZombiePitCard(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
        setID("ZombiePitCard");
        setBuilding(new ZombiePitBuilding(x, y));
        setName("Gengar's Zombie Pit Card");
        setValue(15);
    }    

}
