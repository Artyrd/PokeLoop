package unsw.loopmania.items;

import unsw.loopmania.MovingEntity;

/**
 * represents an equipped or unequipped weapon in the backend world
 */
public interface Weapon {


    /**
     * A normal attack with a weapon
     * @param enemy the enemy to receive the attack
     * @param charAtk the original attack value of the character
     * @return the final value of damage dealt
     */
    public double attack(MovingEntity enemy, double charAtk);

    /**
     * A critical attack with a weapon
     * @param enemy the enemy to receive the attack
     * @param charAtk the original attack value of the character
     * @return the final value of damage dealt
     */
    public double critAttack(MovingEntity enemy, double charAtk);
}
