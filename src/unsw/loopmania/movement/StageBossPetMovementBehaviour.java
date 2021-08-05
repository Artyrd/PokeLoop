package unsw.loopmania.movement;

import unsw.loopmania.MovingEntity;

public class StageBossPetMovementBehaviour implements MovementBehaviour{
    /**
     * The Stage Boss Pet oscillates 1 tile around its spawn position
     * It moves once every 3 character moves
     */

    private int rest = 3;
    private int wait = 0;

    private int direction = 1;

    private boolean establishedBase = false;
    private int baseX;
    private int baseY;


    @Override
    public void move(PathPosition position, MovingEntity self) {

        if (!establishedBase) {
            establishBase(position);
        }

        if (wait < rest){
            wait++;
            return;
        }
        wait = 0;

        if (direction == 1) {
            position.moveUpPath();
        } else {
            position.moveDownPath();
        }

        if (!atCentre(position)) {
            reverseDirection();
        }

    }
    
    private boolean atCentre(PathPosition position) {
        if (baseX == position.getX().get() && 
                baseY == position.getY().get() ) {
            return true;
        } else {
            return false;
        }
    }

    private void establishBase(PathPosition position) {
        baseX = position.getX().get();
        baseY = position.getY().get();
        establishedBase = true;
    }

    private void reverseDirection() {
        if (direction == 1) {
            direction = -1;
        } else {
            direction = 1;
        }
    }
}
