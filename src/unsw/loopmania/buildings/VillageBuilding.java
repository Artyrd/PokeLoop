package unsw.loopmania.buildings;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.Character;
import unsw.loopmania.LoopManiaWorld;

public class VillageBuilding extends Building {
    
    double healValue;

    public VillageBuilding(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
        setID("VillageBuilding");
        healValue = 25;
    }

    /**
     * Heal the character when in range
     */
    @Override
    public void update(LoopManiaWorld world) {
        Character hero = world.getCharacter();
        
        if (hero.getX() == this.getX() && hero.getY() == this.getY()) {
            hero.restoreHP(healValue);
        }

    }
}
