package unsw.loopmania.movement;

import unsw.loopmania.MovingEntity;

/**
 * MovementBehaviour interface 
 * All moving entity classes will have a movement behaviour strategy that will 
 * be implemented by their movement behaviour class
 */
public interface MovementBehaviour {
    public void move(PathPosition position, MovingEntity self);
}
