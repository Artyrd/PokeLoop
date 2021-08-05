package unsw.loopmania.buildings;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.enemies.Vampire;
import unsw.loopmania.movement.PathPosition;

/**
 * a basic form of building in the world
 */
public class VampireCastleBuilding extends Spawner{

    int lastLoopSpawn;
    
    public VampireCastleBuilding(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
        setID("VampireCastleBuilding");
        lastLoopSpawn = 0;
    }

    /**
     * Occasionally spawn a vampire
     */
    @Override
    public void update(LoopManiaWorld world) {
        int currLoop = world.getLoop();

        if (currLoop % 5 == 0 && currLoop > lastLoopSpawn) {
            // spawn a vampire every 5 loops
            PathPosition vampirePos = closestPath(world);
            if (vampirePos != null) {
                System.out.println("SPAWNING VAMPIRE");
                world.addEnemy(new Vampire(vampirePos));
                lastLoopSpawn = currLoop;
            }
        }

    }

}
