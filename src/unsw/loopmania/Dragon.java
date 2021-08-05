package unsw.loopmania;

import unsw.loopmania.battle.DragonBattleBehaviour;
import unsw.loopmania.movement.DragonMovementBehaviour;
import unsw.loopmania.movement.PathPosition;

public class Dragon extends MovingEntity {
    /**
     * Creates a Dragon entity, which is a friendly unit that flies around the map.
     * Normally spawned from the Dragon Spawner Card
     */

    public Dragon(PathPosition position) {
        super(position, 200, 15, 5, 25, true, new DragonMovementBehaviour(), new DragonBattleBehaviour());
        setID("Dragon");
        setSupportRadius(6);
    }

}
