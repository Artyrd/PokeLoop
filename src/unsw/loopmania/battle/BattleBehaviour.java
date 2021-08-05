package unsw.loopmania.battle;

import java.util.ArrayList;

import unsw.loopmania.MovingEntity;
import unsw.loopmania.status.Status;

public interface BattleBehaviour {
    /**
     * Commands the source entity to attack the target entity
     * @param source - the attacker
     * @param target - the Entity being attacked
     * @param currATK - the currATK of the attacker
     * @return the pre-mitigated damage dealt by the attack
     */
    public double attack(MovingEntity source, MovingEntity target, double currATK);

    /**
     * Commands the source entity to critically attack the target entity
     * @param source - the attacker
     * @param target - the Entity being attacked
     * @param currATK - the currATK of the attacker
     * @return the pre-mitigated damage dealt by the attack
     */
    public double critAttack(MovingEntity source, MovingEntity target, double currATK, ArrayList<MovingEntity> alliesOfAttacker, ArrayList<MovingEntity> enemiesOfAttacker);

    /**
     * Applies damage from an attack to oneself.
     * Mitigates the raw damage value from an attack and applies the net result as health loss.
     * @param source - the Entity attacking
     * @param self - the Entity being attacked
     * @param damage - the damage dealt by the attack
     * @return - the net damage received post mitigation
     */
    public double receiveAttack(MovingEntity source,  MovingEntity self, double damage);

    /**
     * Applies damage from an attack to oneself.
     * Mitigates the raw damage value from an attack and applies the net result as health loss.
     * @param source - the Entity attacking
     * @param self - the Entity being attacked
     * @param status - the status being afflicted
     * @return - if the Status effect was successful
     */
    public boolean receiveStatus(MovingEntity self, Status status);

    /**
     * Finds the preferred attack target in arraylist targets. Preferred target is based on
     * enemy type
     * @param targets
     * @return the attack target
     */
    public MovingEntity getAttackPreference(ArrayList<MovingEntity> targets);

}
