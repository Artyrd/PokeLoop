package unsw.loopmania.items;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.MovingEntity;
import unsw.loopmania.status.Status;

public class Helmet extends Item implements Protection {
    public Helmet(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
        setID("Helmet");
        setATK(-1.0);
        setDEF(1 * this.getStar());
        setScalarReduction(0.1);
        setPrice(30 + 25 * (this.getStar() - 1));
        setName("Platinum's Protective Hat");
        setType("Helmet");
        setMaxDurability(15 + 10 * (this.getStar() - 1));
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
        setDEF(1 * star);
        setPrice(30 + 25 * (star - 1));
        setMaxDurability(15 + 10 * (this.getStar() - 1));
    }
}
