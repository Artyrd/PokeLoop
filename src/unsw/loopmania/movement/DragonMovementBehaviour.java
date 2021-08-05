package unsw.loopmania.movement;


import org.javatuples.Pair;

import unsw.loopmania.MovingEntity;

/**
 * MovementBehaviour strategy for a dragon class. 
 * A dragon will move once every 2 character moves.
 * It will initially move right, until it reaches the right most path's X coord,
 * then it flies anticlockwise around the perimeter of the world
 * in a rectangle within bounds of the topmost, rightmost, bottommost, and leftmost path.
 * The flying dragon can traverse off the path.
 */
public class DragonMovementBehaviour implements MovementBehaviour{

    private int minX;
    private int minY;
    private int maxX;
    private int maxY;

    private boolean reachedPerimeter = false;
    private boolean calculatedPerimeter = false;
    private boolean rest = true;


    /**
     * Move the dragon along the path
     */
    @Override
    public void move(PathPosition position, MovingEntity self) {
        if (!calculatedPerimeter) {
            calculatePerimeter(position);
        }
        // dragon rests once every 2 char move ticks
        if (!rest) {
            rest = true;
            return;
        } else {
            rest = false;
        }

        if (!reachedPerimeter) {
            moveToPerimeter(position);
            return;
        }

        int currX = position.getX().get();
        int currY = position.getY().get();

        /* now move around the map */
        if (currX == minX && currY != maxY) {
            // if we are on the left edge but havent reached the top, fly down
            position.getY().set(currY + 1);
        } else if (currY == minY && currX != minX) {
            // if we are on the top edge but have reached the right, fly left
            position.getX().set(currX - 1);
        } else if (currX == maxX && currY != minY) {
            // if we are on the right edge but have reached the bottom, fly up
            position.getY().set(currY - 1);
        } else if (currY == maxY && currX != maxX) {
            // if we are on the bottom edge but have reached the left, fly right
            position.getX().set(currX + 1);
        }
        
    }

    /**
     * Move to the perimeter of their movement
     * @param position current position
     */
    private void moveToPerimeter(PathPosition position) {
        int currX = position.getX().get();
        position.getX().set(currX + 1);
        if (position.getX().get() == maxX) {
            reachedPerimeter = true;
        }
    }

    /**
     * Calculate the limit of their movement
     * @param position current position
     */
    private void calculatePerimeter(PathPosition position) {
        minX = position.getX().get();
        maxX = position.getX().get();
        minY = position.getY().get();
        maxY = position.getY().get();
        for (Pair<Integer, Integer> pair: position.getOrderedPath()) {
            int x = pair.getValue0();
            int y = pair.getValue1();
            if (x < minX) {
                minX = x;
            }
            if (x > maxX) {
                maxX = x;
            }
            if (y < minY) {
                minY = y;
            }
            if (y > maxY) {
                maxY = y;
            }
        }

        if (position.getX().get() == maxX) {
            reachedPerimeter = true;
        }

        calculatedPerimeter = true;

    }
    
}
