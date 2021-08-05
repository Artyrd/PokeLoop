package unsw.loopmania.status;

import java.util.Random;

import unsw.loopmania.MovingEntity;

public class VampireDebuff implements Status {

    private String id;
    private int duration;

    public VampireDebuff() {
        this.id = "VampireDebuff";

        // Assign a random duration to the debuff
        Random random = new Random();
        int amount = random.nextInt(7);
        if (amount < 3) {
            this.duration = 3;
        }
        else {
            this.duration = amount;
        }
    }

    @Override
    public String getID() {
        return this.id;
    }

    /**
     * Apply the status to the entity, increasing damage taken from vampires
     * @param entity the entity receiving the status
     */
    @Override
    public void applyStatus(MovingEntity entity) {
        
        // Assign a random value of extra damage received
        Random randomExtraDmg = new Random();
        double extraDmg = randomExtraDmg.nextInt(10) + 1;
        entity.receiveAttack(entity, extraDmg);
        // Reduce the duration of the effect
        duration = duration - 1;
        // End after duration = 0
        if (duration == 0) {
            endStatus(entity);
        }
    }

    @Override
    public void endStatus(MovingEntity entity) {
        id = "null";
        //entity.setStatus(null);
    }

    @Override
    public void battleTick(MovingEntity entity) {
        // do nothing
        // VampireDebuff only applied when a vampire attacks
    }
    
    @Override
    public void worldTick(MovingEntity entity) {
        // do nothing
    }

}
