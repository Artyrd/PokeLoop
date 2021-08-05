package unsw.loopmania.buildings;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.Dragon;
import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.movement.PathPosition;

public class DragonSpawnerBuilding extends Spawner{
    
    public DragonSpawnerBuilding(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
        setID("DragonSpawnerBuilding");
    }

    /**
     * Spawns the dragon then destroys the building
     */
    @Override
    public void update(LoopManiaWorld world) {
        PathPosition dragonPosition = new PathPosition(getX(), getY(), world.getOrderedPath());
        world.addFriendlyEntity(new Dragon(dragonPosition));
        System.out.println("Spawning Dragon!!!!");
        this.destroy();
    }

}
