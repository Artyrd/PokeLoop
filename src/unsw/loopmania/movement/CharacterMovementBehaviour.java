package unsw.loopmania.movement;

import unsw.loopmania.MovingEntity;

public class CharacterMovementBehaviour implements MovementBehaviour{
    /**
     * The Character only moves down along the path
     */
    @Override
    public void move(PathPosition position, MovingEntity self) {
        position.moveDownPath();
    }
        
}
