package unsw.loopmania.buildings;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.Ally;
import unsw.loopmania.Character;
import unsw.loopmania.LoopManiaWorld;

public class BarracksBuilding extends Building{
    
    public BarracksBuilding(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
        setID("BarracksBuilding");
    }

    /**
     * Give the character an ally when visited
     */
    @Override
    public void update(LoopManiaWorld world) {
        
        Character hero = world.getCharacter();

        if (hero.getX() == this.getX() && hero.getY() == this.getY()) {
            world.addAlly(new Ally());
        }

    }

}
