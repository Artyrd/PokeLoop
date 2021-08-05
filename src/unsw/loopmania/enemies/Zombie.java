package unsw.loopmania.enemies;

import unsw.loopmania.battle.BasicBattleBehaviour;
import unsw.loopmania.battle.ZombieBattleBehaviour;
import unsw.loopmania.movement.PathPosition;
import unsw.loopmania.movement.ZombieMovementBehaviour;

/**
 * A zombie class, subclass of enemy. A zombie will walk in one random direction until it reaches
 * maximum distance travelled, then it will reverse and walk in the opposite direction.
 * 
 * Battle stats:
 * ATK: 8
 * DEF: 1
 * HP: 40
 * Crit Chance: 20%
 * 
 * Loot of a zombie is found inside ZombieReward strategy class.
 */
public class Zombie extends Enemy {

    /**
     * Constructor of Zombie class
     * Zombies use ZombieMovementBehaviour which will cause them to walk in one direction a distance, then walk back
     * @param position
     */
    public Zombie(PathPosition position){
        super(position, 40, 8, 1, 20, false, new ZombieMovementBehaviour(), new ZombieBattleBehaviour(new BasicBattleBehaviour()), new ZombieReward());
        setID("Zombie");
        setBattleRadius(2);
        setSupportRadius(3);
    }


}
