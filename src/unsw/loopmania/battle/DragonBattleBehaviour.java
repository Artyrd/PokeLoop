package unsw.loopmania.battle;

import unsw.loopmania.MovingEntity;
import unsw.loopmania.status.Status;

import java.util.ArrayList;

import unsw.loopmania.status.Burned;

/**
 * Dragons burn targets on critical attack.
 * Dragons are immune to all status effects.
 * Dragons eat dead enemies, and heal hp equal to half their target's max hp if they deal the killing blow.
 * Dragons target the lowest hp enemy in battle.
 */
public class DragonBattleBehaviour implements BattleBehaviour{
    
    /**
     * Constructor to quickly make a defauly zombie
     */
    public DragonBattleBehaviour() {
    }

    /**
     * Deal basic damage to the target, eat the target to execute
     * @param source the attacking entity
     * @param target the entity being attacked
     * @param currATK the base damage being dealt
     */
    @Override
    public double attack(MovingEntity source, MovingEntity target, double currATK) {
        double damageDealt;
        damageDealt = target.receiveAttack(source, currATK);
        eatTarget(source, target);
        return damageDealt;
    }

    /**
     * Deal critical damage to the target of base * 1.5
     * Targets also receive the burned status and can be eaten to execute
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
        target.receiveStatus(new Burned());
        eatTarget(source, target);
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
     * Dragon is immune to all statuses
     */
    @Override
    public boolean receiveStatus(MovingEntity self, Status status) {
        return false;
    }

    /**
     * Dragon attacks the lowest hp target
     */
    @Override
    public MovingEntity getAttackPreference(ArrayList<MovingEntity> targets) {
        if (targets.size() < 1) {
            return null;
        }
        MovingEntity weakestTarget = targets.get(0);
        for(MovingEntity target : targets){
            if (target.getCurrHP() < weakestTarget.getCurrHP()) {
                weakestTarget = target;
            } else if (target.getCurrHP() == weakestTarget.getCurrHP() && target.getMaxHP() < weakestTarget.getMaxHP()) {
                // settle ties with max HP, beyond this: the battle simulator naturally gives the closest enemies/ first spawned enemies in order.
                weakestTarget = target;
            }
        }
        return weakestTarget;
    }

    /**
     * If the dragon deals the killing blow, it eats the target and restores hp.
     * Hp restored = target's maxHP / 2.
     * It also gains maxhp, atk, and def equal to the target's maxhp/10,  atk/10, def/10
     * @param self the dragon
     * @param target the dragon's attack target, post attack
     */
    private void eatTarget(MovingEntity self, MovingEntity target) {
        if (target.getCurrHP() <= 0) {
            self.setMaxHP(self.getMaxHP() + target.getMaxHP()/10);
            self.setCurrATK(self.getCurrATK() + target.getCurrATK()/10);
            self.setCurrDEF(self.getCurrDEF() + target.getCurrDEF()/10);
            self.restoreHP(target.getMaxHP()/2);
        }
    }
    

}
