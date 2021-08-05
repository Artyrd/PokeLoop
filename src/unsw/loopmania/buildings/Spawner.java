package unsw.loopmania.buildings;

import javafx.beans.property.SimpleIntegerProperty;

import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.movement.PathPosition;

/**
 * Spawner class is a sub class of buildings and a super class to:
 *      - VampireCastleBuilding
 *      - ZombiePitBuilding
 *      - BarracksBuilding
 * This class has a has a list of entities that it can spawn onto the map
 * depending on character's position and current loop number.
 */
public class Spawner extends Building{

    public Spawner(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
        setID("Spawner");
    }
    
    @Override
    public void update(LoopManiaWorld world) {}

    /**
     * @return a PathPosition object thats adjacent to the Spawner
     */
    public PathPosition closestPath(LoopManiaWorld world) {

        int startX = this.getX() - 1;
        int startY = this.getY() - 1;

        // looks at a 3x3 square with the Spawner in the centre
        for (int dx = 0; dx < 3; dx++) {
            for (int dy = 0; dy < 3; dy++) {
                if (world.isPath(startX + dx, startY + dy)) {
                    return new PathPosition(startX + dx, startY + dy, world.getOrderedPath());
                }
            }
        }

        return null;

    }


}
