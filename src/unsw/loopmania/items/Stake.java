package unsw.loopmania.items;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.MovingEntity;
import unsw.loopmania.enemies.Vampire;

public class Stake extends Item implements Weapon {

    public Stake(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
        setID("Stake");
        setATK(3 * this.getStar());
        setPrice(55 + 25 * (this.getStar() - 1));
        setName("Your Neighbour's Fence Stake");
        setType("Weapon");
        setMaxDurability(10 + 10 * (this.getStar() - 1));
    } 

    /**
     * A normal attack with a stake
     * If the enemy target is a vampire, deal extra damage
     * @param enemy the enemy to receive the attack
     * @param charAtk the original attack value of the character
     * @return the final value of damage dealt
     */
    @Override
    public double attack(MovingEntity enemy, double charAtk) {
        decreaseDurability(1);
        // If the enemy is a vampire, a stake does x3 damage to them 
        if (enemy instanceof Vampire) {
            return charAtk * 3;
        }
        return charAtk;
    }

    /**
     * A critical attack with a stake
     * if the enemy target is a vampire, deal extra extra damage
     * @param enemy the enemy to receive the attack
     * @param charCritAtk the original attack value of the character * 1.5 with the crit already multiplied
     * @return the final value of damage dealt
     */
     @Override
    public double critAttack(MovingEntity enemy, double charCritAtk) {

        decreaseDurability(2);
        if (enemy instanceof Vampire) {
            return charCritAtk * 3;
        }
        return charCritAtk;
    }
    
    /**
     * Set the star, attack and price of the item
     * @param star the new star tier
     */
    @Override
    public void setStar(int star) {
        super.setStar(star);
        setATK(3 * star);
        setPrice(55 + 25 * (star - 1));
        setMaxDurability(10 + 10 * (this.getStar() - 1));
    }
}
