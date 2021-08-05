package unsw.loopmania.battle;

import java.util.ArrayList;

import unsw.loopmania.MovingEntity;
import unsw.loopmania.status.Status;

/**
 * BattleBehaviour strategy for a basic battle behaviour (which is implemented by slugs and allies).
 * A critical attack in the basic behaviour will strike for 1.5 * normal attack damage.
 */
public class BasicBattleBehaviour implements BattleBehaviour {

    /**
     * Deal basic damage to the target
     * @param source the attacking entity
     * @param target the entity being attacked
     * @param currATK the base damage being dealt
     */
    @Override
    public double attack(MovingEntity source, MovingEntity target, double currATK) {
        double damageDealt;
        damageDealt = target.receiveAttack(source, currATK);
        return damageDealt;
    }

    /**
     * Deal critical damage to the target of base * 1.5
     * @param source the attacking entity
     * @param target the entity being attacked
     * @param currATK the base damage being dealt
     * @param alliesOfAttacker allies of source entity
     * @param enemiesOfAttacker enemies of source entity
     */
    @Override
    public double critAttack(MovingEntity source, MovingEntity target, double currATK, ArrayList<MovingEntity> alliesOfAttacker, ArrayList<MovingEntity> enemiesOfAttacker) {
        double damageDealt;
        damageDealt = target.receiveAttack(source, 1.5 * currATK);
        return damageDealt;
    }

    /**
     * Receive an attack of amount damage, reduce damage from current defense
     * @param source the source of damage
     * @param self the entity being damaged
     * @param damage the amount of damage
     * @return the net damage received after reductions
     */
    @Override
    public double receiveAttack(MovingEntity source, MovingEntity self, double damage) {
        double netDamage = damage - self.getCurrDEF();
        self.reduceHP(netDamage);
        return netDamage;
    }

    /**
     * Receive a status effect
     * @param self the entity receiving the effect
     * @param status the status being applied
     * @return true: the status was received
     */
    @Override
    public boolean receiveStatus(MovingEntity self, Status status) {
        self.setStatus(status);
        status.applyStatus(self);
        return true;
    }

    @Override
    public MovingEntity getAttackPreference(ArrayList<MovingEntity> targets) {
        return null;
    }
    
}
