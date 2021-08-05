package unsw.loopmania.buildings;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.LoopManiaWorld;

public class TowerBuilding extends Building{
    
    private boolean towerActive;

    public TowerBuilding(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
        setID("TowerBuilding");
        towerActive = false;
    }

    /**
     * Activate the tower
     */
    @Override
    public void update(LoopManiaWorld world) {
        if (towerActive == false) {
            TowerBattler towerBattler = new TowerBattler(this);
            world.addEntity(towerBattler);
            world.addActiveStructures(towerBattler);
            towerActive = true;
        }
    }

	public void setNotActive() {
        towerActive = false;
	}



}
