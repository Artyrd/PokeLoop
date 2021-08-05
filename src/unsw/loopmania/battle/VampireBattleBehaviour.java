package unsw.loopmania.battle;

import java.util.ArrayList;

import unsw.loopmania.MovingEntity;
import unsw.loopmania.status.Status;
import unsw.loopmania.status.VampireDebuff;
import unsw.loopmania.Character;

/**
 * Vampire Battle behaviour strategy which can hold the basic battle behaviour.
 * A vampire behaviour has a special critical attack that will apply VampireDebuff
 * to only friendly targets. 
 */
public class VampireBattleBehaviour implements BattleBehaviour {

    private BattleBehaviour baseBattle;

    public VampireBattleBehaviour(BattleBehaviour baseBattle) {
        this.baseBattle = baseBattle;
    }

    /**
     * Deal basic damage to the target
     * If the target is debuffed with the vampire debuff, deal bonus damage
     * @param source the attacking entity
     * @param target the entity being attacked
     * @param currATK the base damage being dealt
     */
    @Override
    public double attack(MovingEntity source, MovingEntity target, double currATK) {
        if(target.getStatusIDs().contains("VampireDebuff")){
            target.getStatusByID("VampireDebuff").applyStatus(target);
        }
        double damage =baseBattle.attack(source, target, currATK);
        return damage;
    }

    /**
     * Deal critical damage to the target of base * 1.5
     * and apply the vampire debuff to friendly allies
     * @param source the attacking entity
     * @param target the entity being attacked
     * @param currATK the base damage being dealt
     * @param alliesOfAttacker allies of source entity
     * @param enemiesOfAttacker enemies of source entity
     */
    @Override
    public double critAttack(MovingEntity source, MovingEntity target, double currATK, ArrayList<MovingEntity> alliesOfAttacker, ArrayList<MovingEntity> enemiesOfAttacker) {
        if(target.isFriendly()){
            target.receiveStatus(new VampireDebuff());
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
        for(MovingEntity target : targets){
            if(target instanceof Character){
                return target;
            }
        }
        return null;
    }
    
}
