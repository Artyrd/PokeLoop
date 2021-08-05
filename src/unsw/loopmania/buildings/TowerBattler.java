package unsw.loopmania.buildings;

import unsw.loopmania.MovingEntity;
import unsw.loopmania.battle.BasicBattleBehaviour;

public class TowerBattler extends MovingEntity {

    TowerBuilding towerBuilding;

    /**
     * A TowerBattler MovingEntity to represent the tower in battles
     */
    public TowerBattler(TowerBuilding towerBuilding) {
        super(null, 10000, 5, 10000, 25, true, null, new BasicBattleBehaviour());
        setID("TowerBattler");
        this.towerBuilding = towerBuilding;
        setSupportRadius(2);
        // set support radius = 2
    }

    // returns the location of the tower    
    @Override
    public int getX() {
        return towerBuilding.getX();
    }

    @Override
    public int getY() {
        return towerBuilding.getY();
    }

    @Override
    public void destroy() {
        towerBuilding.setNotActive();
        super.destroy();
    }

}
