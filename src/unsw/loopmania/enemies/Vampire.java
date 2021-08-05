package unsw.loopmania.enemies;

import java.util.ArrayList;

import unsw.loopmania.battle.BasicBattleBehaviour;
import unsw.loopmania.battle.VampireBattleBehaviour;
import unsw.loopmania.movement.PathPosition;
import unsw.loopmania.movement.VampireMovementBehaviour;
import unsw.loopmania.observer.Observer;
import unsw.loopmania.observer.Subject;

/**
 * A vampire class, subclass of enemy. A vampire will walk in a random direction.
 * 
 * Battle stats:
 * ATK: 16
 * DEF: 3
 * HP: 120
 * Crit Chance: 40%
 * 
 * Loot of a vampire is found inside VampireReward strategy class.
 */
public class Vampire extends Enemy implements Observer{

    //private ArrayList<Subject> listCampfire = new ArrayList<Subject>();
    private VampireMovementBehaviour vampMovement;

    /** 
     * Constructor of Vampire class. 
     * Vampires inherit move from Enemy class, which is a random movement pattern
     * @param position
     */
    public Vampire(PathPosition position){
        super(position, 120, 16, 3, 40, false, null , new VampireBattleBehaviour(new BasicBattleBehaviour()), new VampireReward());
        setID("Vampire");
        setBattleRadius(3);
        setSupportRadius(4);

        vampMovement = new VampireMovementBehaviour();
        setMovementBehaviour(vampMovement);
    }

    @Override
    public void update(Subject sub) {
        ArrayList<Subject> listCampfire = vampMovement.getCampfireList();
        listCampfire.add(sub);
    }
   
}
