package unsw.loopmania.items;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.MovingEntity;

/**
 * represents an equipped or unequipped sword in the backend world
 */
public class Sword extends Item implements Weapon {
    public Sword(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
        setID("Sword");
        setATK(5 * this.getStar());
        setPrice(60 + 25 * (this.getStar() - 1));
        setName("Giratina's Distortion Sword");
        setType("Weapon");
        setMaxDurability(20 + 10 * (this.getStar() - 1));
    }

    /**
     * A normal attack with a sword
     * @param enemy the enemy to receive the attack
     * @param charAtk the original attack value of the character
     * @return the final value of damage dealt
     */
    @Override
    public double attack(MovingEntity enemy, double charAtk) {
        decreaseDurability(1);
        return charAtk;
    }

    /**
     * Sword crit attack
     * @param enemy the enemy being hit
     * @param charCritAtk the Character's atk value * 1.5 already multiplied
     */
    @Override
    public double critAttack(MovingEntity enemy, double charCritAtk) {
        decreaseDurability(2);
        return charCritAtk;
    }
    
    /**
     * Set the star, attack and price of the item
     * @param star the new star tier
     */
    @Override
    public void setStar(int star) {
        super.setStar(star);
        setATK(5 * star);
        setPrice(60 + 25 * (star - 1));
        setMaxDurability(20 + 10 * (this.getStar() - 1));
    }
}
