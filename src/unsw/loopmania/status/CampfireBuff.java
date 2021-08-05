package unsw.loopmania.status;

import unsw.loopmania.MovingEntity;

public class CampfireBuff implements Status {

    private String id;
    //private int duration;

    public CampfireBuff() {
        this.id = "CampfireBuff";
        //duration = 1;
    }

    @Override
    public String getID() {
        return this.id;
    }

    /**
     * Apply the status to the entity, giving them double damage
     * @param entity the entity receiving the status
     */
    @Override
    public void applyStatus(MovingEntity entity) {
        entity.setCurrATK(entity.getCurrATK() * 2); 
    }

    /**
     * End the status to the entity, reduce their damage back to original (half)
     * @param entity the entity receiving the status
     */
    @Override
    public void endStatus(MovingEntity entity) {
        entity.setCurrATK(entity.getCurrATK() / 2);
        id = "null";
    }

    /**
     * Continue the status to the entity during battle
     * @param entity the entity receiving the status
     */
    @Override
    public void battleTick(MovingEntity entity) {
        // do nothing, battle tick lasts forever
    }
    
    /**
     * Continue the status to the entity during world travel
     * @param entity the entity receiving the status
     */
    @Override
    public void worldTick(MovingEntity entity) {
        //duration--;
        endStatus(entity);
    }

}
