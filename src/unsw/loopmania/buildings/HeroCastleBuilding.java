package unsw.loopmania.buildings;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.Character;
import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.LoopManiaWorld.GAME_STATE;

public class HeroCastleBuilding extends Building{
    
    public HeroCastleBuilding(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
    }

    /**
     * Show the shop at when visited at certain intervals
     */
    @Override
    public void update(LoopManiaWorld world) {

        Character hero = world.getCharacter();
        
        if (hero.getX() == this.getX() && hero.getY() == this.getY()) {
            world.addLoop();
            world.gameState = GAME_STATE.SHOP;
        }

    }

}
