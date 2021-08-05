package unsw.loopmania.cards;

import javafx.beans.property.SimpleIntegerProperty;

/**
 * represents a vampire castle card in the backend game world, to be paired with a building later
 */
public class StageBossCard extends SummoningCard {

    public StageBossCard(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
        setID("StageBossCard");
        setName("Gym Leader Challenge Card");
        setValue(15);
    }

}
