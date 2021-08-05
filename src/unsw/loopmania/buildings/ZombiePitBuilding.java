package unsw.loopmania.buildings;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.enemies.Zombie;
import unsw.loopmania.movement.PathPosition;

public class ZombiePitBuilding extends Spawner{
    
    int lastLoopSpawn;

    public ZombiePitBuilding(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
        setID("ZombiePitBuilding");
        lastLoopSpawn = 0;
    }

    /**
     * Spawn a zombie each loop
     */
    @Override
    public void update(LoopManiaWorld world) {
        // spawn a zombie if we havent spawned one this loop yet
        int currLoop = world.getLoop();
        if (currLoop > lastLoopSpawn) {
            System.out.println("LastLoopSpawn: " + lastLoopSpawn);
            // get PathPosition
            PathPosition zombiePos = closestPath(world);
            if (zombiePos != null) {
                world.addEnemy(new Zombie(zombiePos));
                lastLoopSpawn = currLoop;
                System.out.printf("Spawning zombie at loop: %d\n", currLoop);
            }
        }
    }

}
