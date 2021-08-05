package unsw.loopmania.movement;

import java.util.Random;

import unsw.loopmania.MovingEntity;

/**
 * MovementBehaviour strategy for a zombie class. A zombie will move every three character 
 * moves and will walk in a straight line for a distance of two, then walk back in the
 * previous direction. 
 */
public class ZombieMovementBehaviour implements MovementBehaviour{

    private int directionChoice = (new Random()).nextInt(2);    // Random initial direction the zombie will walk in until maxDistance
    private int maxDistance = 2;
    private int currDistance = 0;
    private int rest = 3;
    private int wait = 0;

    /**
     * Getter for currPosition (used in testing)
     * @return currDistance
     */
    public int getCurrPosition(){
        return currDistance;
    }

    /**
     * Getter for maxDistance (used in testing)
     * @return maxDistance
     */
    public int getMaxDistance(){
        return maxDistance;
    }

    public void setDirectionChoice(int direction) {
        directionChoice = direction;
    }

    /**
     * Move the zombie along the path
     * @param position current position
     * @param self the zombie being moved
     */
    @Override
    public void move(PathPosition position, MovingEntity self) {
        if(wait < rest){
            wait++;
        } else {
            wait = 0;
            // Move in a clockwise or anticlockwise direction for a maxDistance, 
            // Clockwise direction
            if (directionChoice == 0){ 
                if(currDistance < maxDistance){  
                    position.moveUpPath();
                    currDistance++;
                } else {                // Max distance travelled, now reverse back in opposite direction
                    directionChoice = 1; 
                    position.moveDownPath();
                    currDistance--;
                }
            } 
            // Anticlockwise direction
            else { 
                if(currDistance > -maxDistance){
                    position.moveDownPath();
                    currDistance--;
                } else {
                    directionChoice = 0;
                    position.moveUpPath();
                    currDistance++;
                }
            }
        }
        
    }
    
}
