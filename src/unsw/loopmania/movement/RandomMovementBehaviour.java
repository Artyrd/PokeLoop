package unsw.loopmania.movement;

import java.util.Random;

import unsw.loopmania.MovingEntity;

/**
 * MovementBehaviour strategy for a slug, which will move in a random direction every
 * five character moves
 */
public class RandomMovementBehaviour implements MovementBehaviour {

    private Random directionChoice = (new Random(2511));
    private int rest;
    private int wait = 0;

    public RandomMovementBehaviour(int rest){
        this.rest = rest;
    }

    /**
     * Randomly move the entity on the path
     * @param position current position
     * @param self the entity being moved
     */
    @Override
    public void move(PathPosition position, MovingEntity self) {
        if(wait < rest){
            wait++;
        } else {
            wait = 0;
            int direction = directionChoice.nextInt(2);
            if (direction == 0) {
                position.moveUpPath();
            } else {
                position.moveDownPath();
            }
            
            
        }    
    }
    
}
