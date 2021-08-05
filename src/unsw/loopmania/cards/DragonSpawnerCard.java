package unsw.loopmania.cards;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.buildings.DragonSpawnerBuilding;

/**
 * represents a vampire castle card in the backend game world
 */
public class DragonSpawnerCard extends SummoningCard {

    public DragonSpawnerCard(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
        setID("DragonSpawnerCard");
        setBuilding(new DragonSpawnerBuilding(x, y));
        setName("Charizard's Pokeball");
        setValue(99);
    }    

}
