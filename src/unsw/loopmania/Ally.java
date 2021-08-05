package unsw.loopmania;

import unsw.loopmania.battle.BasicBattleBehaviour;
import unsw.loopmania.movement.PathPosition;

public class Ally extends MovingEntity {
    /**
     * Creates an ally, by defauly does not have a position on the path
     */
    public Ally() {
        super(null, 50, 1, 1, 25, true, null, new BasicBattleBehaviour());
        setID("Ally");
    }

    public Ally(PathPosition position) {
        super(position, 50, 1, 1, 25, true, null, new BasicBattleBehaviour());
        setID("Ally");
    }

}
