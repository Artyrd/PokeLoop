package unsw.loopmania.items;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.MovingEntity;
import unsw.loopmania.status.Tranced;

public class Staff extends Item implements Weapon {
    
    public Staff(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x,y);
        setID("Staff");
        setATK(1 * this.getStar());
        setPrice(60 + 25 * (this.getStar() - 1));
        setName("Opal's Wizard Staff");
        setType("Weapon");
        setMaxDurability(15 + 10 * (this.getStar() - 1));
    }

    /**
     * On critical attack to a given enemy, cast a trance and give them a tranced 
     * status effect
     * @param enemy the enemy to receive the critical attack
     * @param charAtk the attack value of the character
     * @return 0 damage dealt, trance casted
     */
    @Override
    public double critAttack(MovingEntity enemy, double charAtk) {
        System.out.println("Trancing!!~~!~!~!");
        enemy.receiveStatus(new Tranced());
        decreaseDurability(2);
        return 0;
    }
    /**
     * A normal attack with a staff
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
     * Set the star, attack and price of the item
     * @param star the new star tier
     */
    @Override
    public void setStar(int star) {
        super.setStar(star);
        setATK(1 * star);
        setPrice(60 + 25 * (star - 1));
        setMaxDurability(15 + 10 * (this.getStar() - 1));
    }
}
