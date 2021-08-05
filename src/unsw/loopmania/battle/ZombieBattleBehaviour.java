package unsw.loopmania.battle;

import unsw.loopmania.MovingEntity;
import unsw.loopmania.status.Status;

import java.util.ArrayList;

import unsw.loopmania.Character;
import unsw.loopmania.status.Zombified;

/**
 * Zombie Battle behaviour strategy which can hold the basic battle behaviour.
 * A zombie behaviour has a special critical attack that will apply Zombified
 * status to allies.
 */
public class ZombieBattleBehaviour implements BattleBehaviour{
    
    private BattleBehaviour baseBattle;

    /**
     * Constructor to quickly make a defauly zombie
     */
    public ZombieBattleBehaviour() {
        this.baseBattle = new BasicBattleBehaviour();
    }
    /**
     * Constructor to zombify existing entity
     */
    public ZombieBattleBehaviour(BattleBehaviour baseBattleBehaviour) {
        this.baseBattle = baseBattleBehaviour;
    }

    /**
     * Deal basic damage to the target
     * @param source the attacking entity
     * @param target the entity being attacked
     * @param currATK the base damage being dealt
     */
    @Override
    public double attack(MovingEntity source, MovingEntity target, double currATK) {
        double damage = baseBattle.attack(source, target, currATK);
        return damage;
    }

    /**
     * Deal critical damage to the target of base * 1.5
     * and zombify friendly allies, turning them into permanent enemies
     * @param source the attacking entity
     * @param target the entity being attacked
     * @param currATK the base damage being dealt
     * @param alliesOfAttacker allies of source entity
     * @param enemiesOfAttacker enemies of source entity
     */
    @Override
    public double critAttack(MovingEntity source, MovingEntity target, double currATK, ArrayList<MovingEntity> alliesOfAttacker, ArrayList<MovingEntity> enemiesOfAttacker) {
        if(target.isFriendly() && !(target instanceof Character)){
            target.receiveStatus(new Zombified());
        }
        double damageDealt = baseBattle.critAttack(source, target, currATK, alliesOfAttacker, enemiesOfAttacker);
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
     * If the status is zombified, dont receive the status
     * @param self the entity receiving the effect
     * @param status the status being applied
     * @return true: the status was received
     */
    @Override
    public boolean receiveStatus(MovingEntity self, Status status) {
        if (status instanceof Zombified) {
            return false;
        }
        self.setStatus(status);
        status.applyStatus(self);
        return true;
    }
    @Override
    public MovingEntity getAttackPreference(ArrayList<MovingEntity> targets) {
        return null;
    }

    
    

}
