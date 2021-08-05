package unsw.loopmania.cards;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.buildings.VampireCastleBuilding;

/**
 * represents a vampire castle card in the backend game world
 */
public class VampireCastleCard extends SpawnerCard {
    public VampireCastleCard(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
        this.setValue(200);
        setID("VampireCastleCard");
        setBuilding(new VampireCastleBuilding(x, y));
        setName("Darkrai's Vampire Castle Card");
        setValue(20);
    }    
    
}
