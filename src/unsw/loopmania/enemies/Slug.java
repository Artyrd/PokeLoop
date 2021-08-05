package unsw.loopmania.enemies;

import unsw.loopmania.movement.PathPosition;
import unsw.loopmania.movement.RandomMovementBehaviour;
import unsw.loopmania.battle.BasicBattleBehaviour;

/**
 * A slug class, subclass of enemy. A slug will walk in a random direction.
 * 
 * Battle stats:
 * ATK: 4
 * DEF: 0
 * HP: 12
 * Crit Chance: 0%
 * 
 * Loot of a slug is found inside SlugReward strategy class.
 */
public class Slug extends Enemy{

    /**
     * Constructor of Slug class
     * Slugs inherit move from Enemy class, which is a random movement pattern
     * @param position
     */
    public Slug(PathPosition position) {
        super(position, 12, 4, 0, 0, false, new RandomMovementBehaviour(5), new BasicBattleBehaviour(), new SlugReward());
        setID("Slug");
        setBattleRadius(1);
        setSupportRadius(2);
    }


}
