package unsw.loopmania.movement;

import java.util.ArrayList;

import org.javatuples.Pair;

import unsw.loopmania.observer.Subject;
import unsw.loopmania.MovingEntity;
import unsw.loopmania.buildings.CampfireBuilding;

/**
 * MovementBehaviour strategy for a vampire 
 * which will move anticlockwise towards the character
 * Campfires cause vampires to run away
 */
public class VampireMovementBehaviour implements MovementBehaviour {

    private enum Direction {
        CLOCKWISE,
        ANTICLOCKWISE
    }

    private ArrayList<Subject> listCampfire;
    private Direction currDirection;
    private int rest;
    private int wait;

    public VampireMovementBehaviour() {
        listCampfire = new ArrayList<Subject>();
        currDirection = Direction.ANTICLOCKWISE;
        rest = 2;
        wait = 0;
    }

    public ArrayList<Subject> getCampfireList() {
        return listCampfire;
    }

    /**
     * Move the vampire along the path
     * @param position current position
     * @param self the vampire being moved
     */
    @Override
    public void move(PathPosition position, MovingEntity self) {
        if(wait < rest){
            wait++;
        } else {
            wait = 0;
            
            // Case when listCampfire is not empty, move away from closest campfire
            if(listCampfire.size() > 0){
                moveAwayFromCampfire(position, self);
            } else {     // Handle case when listCampfire is empty, in which, move towards player
                moveCurrDirction(position);
            }

        }
    }

    /**
     * Move the vampire according to a certain direction
     * @param position current position
     */
    private void moveCurrDirction(PathPosition position) {
        if (currDirection.equals(Direction.ANTICLOCKWISE)) {
            position.moveUpPath();
        } else {
            position.moveDownPath();
        }
    }

    /**
     * If the vampire is near a campfire, walk away from it by changing the direction of travel
     * @param position current position
     * @param self vampire being moved
     */
    private void moveAwayFromCampfire(PathPosition position, MovingEntity self){
        Subject campfire = closestCampfire(self).getValue0();
        double distanceToCampfireSQ = closestCampfire(self).getValue1();
        // if campfire is straightline-dist more than 3 tiles away, it doesnt care
        if (distanceToCampfireSQ > 9) {
            moveCurrDirction(position);
            return;
        }
        CampfireBuilding campfireBuilding = (CampfireBuilding) campfire;
        position.moveUpPath(); // temporarily move up
        currDirection = Direction.ANTICLOCKWISE;
        if(findDistanceSquared(campfireBuilding.getX(), campfireBuilding.getY(), self.getX(), self.getY()) 
                <= distanceToCampfireSQ){    // Moving up path made us closer to campfire, move away instead
            position.moveDownPath(); // backtrack...
            position.moveDownPath(); // moved away
            currDirection = Direction.CLOCKWISE;
        }
    }

    /**
     * Get the location of the cloest campfire relative to the vampire
     * @param self the vampire
     * @return the position of the closest campfire
     */
    private Pair<Subject, Double> closestCampfire(MovingEntity self){
        Subject closest = null;
        int selfX = self.getX();
        int selfY = self.getY();
        double closestDistanceSQ = Double.POSITIVE_INFINITY;
        for(Subject sub : listCampfire){
            CampfireBuilding campfire = (CampfireBuilding) sub;
            int campX = campfire.getX();
            int campY = campfire.getY();

            double distanceSquared = findDistanceSquared(campX, campY, selfX, selfY);
            if(distanceSquared < closestDistanceSQ){
                closestDistanceSQ = distanceSquared;
                closest = sub;
            }
        }

        return new Pair<Subject, Double>(closest, closestDistanceSQ);
    }

    /**
     * Calculate the distance squared between the vampire and the campfire
     * @param campX
     * @param campY
     * @param selfX
     * @param selfY
     * @return the numerical distance
     */
    private double findDistanceSquared(int campX, int campY, int selfX, int selfY){
        double deltaX = (double) campX - (double) selfX;
        double deltaY = (double) campY - (double) selfY;
        double distanceSquared = Math.pow(deltaX, 2) + Math.pow(deltaY, 2);
        return distanceSquared;
    }

}
