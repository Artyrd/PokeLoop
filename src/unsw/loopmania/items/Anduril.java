package unsw.loopmania.items;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.MovingEntity;
import unsw.loopmania.enemies.Boss;
import unsw.loopmania.status.Burned;

public class Anduril extends Sword {

    public Anduril(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
        setID("Anduril");
        setATK(15 * this.getStar());
        setName("Anduril, Flame of the West");
        setMaxDurability(20 + 10 * (this.getStar() - 1));
        setPrice(300 + 25 * (this.getStar() - 1));
    }

    /**
     * Attack an enemy and deal charAtk amount of damage
     * If the enemy is a boss, deal 3x damage
     * @param enemy 
     * @param charAtk
     * @return damage dealt by the weapon
     */
    @Override
    public double attack(MovingEntity enemy, double charAtk) {
        decreaseDurability(1); 
        if (enemy instanceof Boss) {
            return charAtk * 3;
        }
        return charAtk;
    }
    
    /**
     * Critically attack an enemy, deal charCritAtk amount of damage and apply a 
     * Burned status effect on the enemy
     * If the enemy is a boss, deal 3x damage
     * @param enemy 
     * @param charAtk
     * @return damage dealt by the weapon
     */
    @Override
    public double critAttack(MovingEntity enemy, double charCritAtk) {
        decreaseDurability(2);
        enemy.receiveStatus(new Burned());
        if (enemy instanceof Boss) {
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
        setATK(15 * star);
        setPrice(300 + 25 * (star - 1));
        setMaxDurability(20 + 10 * (this.getStar() - 1));
    }
    
}
