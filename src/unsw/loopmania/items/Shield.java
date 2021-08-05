package unsw.loopmania.items;

import java.util.Random;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.MovingEntity;
import unsw.loopmania.status.Status;
import unsw.loopmania.status.VampireDebuff;

public class Shield extends Item implements Protection {
   
    Random random;
    
    public Shield(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
        setID("Shield");
        setDEF(1 * this.getStar());
        setPrice(40 + 25 * (this.getStar() - 1));
        random = new Random(2511);
        setName("Shield of Justice");
        setType("Shield");
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
     * Shield have a 60% chance to resist the Vampire debuff
     * @param status to be received
     * @return boolean of whether a status effect was received or not
     */
    @Override
    public boolean resistStatus (Status status) {
        if (status instanceof VampireDebuff) {
            if (random.nextInt(100) < 60) {
            System.out.println("shieldblock");
                return true;
            }
            System.out.println("shieldnothing");
            
        } // else
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
        setPrice(40 + 25 * (star - 1));
        setMaxDurability(20 + 10 * (this.getStar() - 1));
    }
}
