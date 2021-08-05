package unsw.loopmania.items;

import unsw.loopmania.MovingEntity;
import unsw.loopmania.status.Status;

public interface Protection {

    /**
    * Calculates the net damage post item mitigation (not counting DEF)
    * Solely Item effects
    * @param damage - the received damage post DEF stat mitigation
    * @return the net damage
    */
    public double reduceDamage(MovingEntity enemy, double damage);

    /**
     * Calculates if the item may successfully resist status effects
     * @param status - the status being applied
     * @return true if the character sucessfully resisted the status, false otherwise
     */
    public boolean resistStatus(Status status);
 
}
