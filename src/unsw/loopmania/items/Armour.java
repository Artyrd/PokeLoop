package unsw.loopmania.items;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.MovingEntity;
import unsw.loopmania.status.Status;

public class Armour extends Item implements Protection {

    public Armour(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
        setID("Armour");
        setDEF(3 * this.getStar());
        setScalarReduction(0.5);
        setPrice(50 + 25 * (this.getStar() - 1));
        setName("Groudon's Earthen Armour");
        setType("Armour");
        setMaxDurability(20 + 10 * (this.getStar() - 1));
    }

    /**
    * Calculates the net damage post item mitigation (not counting DEF)
    * Solely Item effects
    * @param damage - the received damage post DEF stat mitigation
    * @return the net damage
    */
    @Override
    public double reduceDamage(MovingEntity enemy, double damage) {
        double netDamage = damage * (1 - this.getScalarReduction());
        decreaseDurability(1);
        return netDamage;
    }

    /**
     * Calculates if the item may successfully resist status effects
     * @param status - the status being applied
     * @return true if the character sucessfully resisted the status, false otherwise
     */
    @Override
    public boolean resistStatus(Status status) {
        return false;
    }
    
    /**
     * Set the star, defense and price of the item
     * @param star the new star tier
     */
    @Override
    public void setStar(int star) {
        super.setStar(star);
        setDEF(3 * star);
        setPrice(50 + 25 * (star - 1));
        setMaxDurability(20 + 10 * (this.getStar() - 1));
    }
}
