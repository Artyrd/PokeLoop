package unsw.loopmania.status;

import unsw.loopmania.MovingEntity;

public class Stunned implements Status {

    private int duration;
    private String id;

    public Stunned(){
        this.id = "Stunned";
        this.duration = 2;
    }


    @Override
    public String getID() {
        return this.id;
    }

    public void setID(String id) {
        this.id = id;
    }

    /**
     * Apply the stunned status to the entity and decrease duration. 
     * If duration is 0, end the status
     * @param entity the entity the status is being applied to
     */
    @Override
    public void applyStatus(MovingEntity entity) {
        if(duration == 0){
            endStatus(entity);
            return;
        }
        duration = duration - 1;
        System.out.println(entity.getID() + " is stunned!");
        
        
    }

    /**
     * End the status and set its ID to null
     * @param entity the entity the status is being ended for
     */
    @Override
    public void endStatus(MovingEntity entity) {
        id = "null";
    }

    /**
     * Apply the status at each tick in battle
     * @param entity the entity the status is being applied to
     */
    @Override
    public void battleTick(MovingEntity entity) {
        applyStatus(entity);
        
    }

    /**
     * The status does not exist outside of battle
     * @param entity
     */
    @Override
    public void worldTick(MovingEntity entity) {
        return;
    }
    
}
