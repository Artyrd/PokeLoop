package unsw.loopmania.items;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.MovingEntity;
import unsw.loopmania.enemies.Boss;
import unsw.loopmania.status.Status;

public class TreeStump extends Shield {

    public TreeStump(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
        setID("TreeStump");
        setScalarReduction(0.4);
        setName("Cerubi's Tree Stump");
        setMaxDurability(20 + 10 * (this.getStar() - 1));
        setPrice(300 + 25 * (this.getStar() - 1));
    }

    /**
     * Reduce incoming damage buy 40%
     * If the enemy is a boss, reduce incoming damage by 80%
     * @param enemy receiving damage from
     * @param damage received
     * @return the net damage received after reductions
     */
    @Override
    public double reduceDamage(MovingEntity enemy, double damage) {
        decreaseDurability(1);
        if (enemy instanceof Boss) {
            return damage * (1 - this.getScalarReduction() * 2);
        } // else
        return damage * (1 - this.getScalarReduction());
    }

    /**
     * This item does not resist any statuses
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
        setPrice(300 + 25 * (star - 1));
        setMaxDurability(20 + 10 * (this.getStar() - 1));
    }
}
