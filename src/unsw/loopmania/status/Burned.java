package unsw.loopmania.status;

import java.util.Random;

import unsw.loopmania.MovingEntity;

public class Burned implements Status {

    private String id;
    private int duration;
    private Random rand;

    public Burned() {
        this.id = "Burned";
        rand = new Random();
        this.duration = rand.nextInt(5) + 2;
    }

    @Override
    public String getID() {
        return this.id;
    }

    public void setID(String id) {
        this.id = id;
    }

    /**
     * Apply the burned status on the entity, causing damage for a random amount
     * between 1 and 5, and decrease the duration of burning
     * If duration is 0, end the status
     * @param entity : the entity the effect is being applied to
     */
    @Override
    public void applyStatus(MovingEntity entity) {
        if(duration == 0){
            endStatus(entity);
            return;
        }
        entity.reduceHP(rand.nextInt(5) + 1);
        duration = duration - 1;
    }

    /**
     * End the effect, set the ID of the effect to null
     * @param entity : the entity the effect is being ended for
     */
    @Override
    public void endStatus(MovingEntity entity) {
        this.id = "null";
    }

    /**
     * At each turn of battle, apply the status effect
     * @param entity : the entity the effect is being applied to
     */
    @Override
    public void battleTick(MovingEntity entity) {
        applyStatus(entity);
        
    }

    /**
     * The status effect does not apply outside of battle
     * @param entity : the entity the effect is applied to
     */
    @Override
    public void worldTick(MovingEntity entity) {
        return;
    }
    
}
