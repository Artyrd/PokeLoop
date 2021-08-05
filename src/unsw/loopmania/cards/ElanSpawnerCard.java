package unsw.loopmania.cards;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.buildings.ElanSpawnerBuilding;

/**
 * represents an Elan Spawner card in the backend game world
 */
public class ElanSpawnerCard extends SummoningCard {

    public ElanSpawnerCard(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
        setID("ElanSpawnerCard");
        setBuilding(new ElanSpawnerBuilding(x, y));
        setName("Team Rocket's Card");
        setValue(15);
    }    

}
