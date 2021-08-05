package unsw.loopmania.buildings;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.enemies.ElanMuske;
import unsw.loopmania.movement.PathPosition;

public class ElanSpawnerBuilding extends Spawner{
    
    private boolean elanSpawned;

    public ElanSpawnerBuilding(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
        setID("ElanSpawnerBuilding");
        elanSpawned = false;
    }

    /**
     * Spawns Elan musk on the platform
     */
    @Override
    public void update(LoopManiaWorld world) {
        // if we already spawned elan, return
        if (elanSpawned) {
            return;
        }
        PathPosition elanPosition = new PathPosition(getX(), getY(), world.getOrderedPath());
        world.addBoss(new ElanMuske(elanPosition));
        elanSpawned = true;
        System.out.println("Spawning Elan Muske !!!!");
    }

}
