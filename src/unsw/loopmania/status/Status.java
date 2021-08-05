package unsw.loopmania.status;

import unsw.loopmania.MovingEntity;

public interface Status {
    
    public String getID();

    /**
     * applies the status' effect, may be called by varying means
     * @param entity the entity receiving the status
     */
    public void applyStatus(MovingEntity entity);

    /**
     * ends the status
     * @param entity the entity whose status is ending
     */
    public void endStatus(MovingEntity entity);

    /**
     * ticks the status with each battle round
     * @param entity the entity whose status is continuing
     */
    public void battleTick(MovingEntity entity);
    /**
     * ticks the status with the world tick
     * @param entity the entity whose status is continuing
     */
    public void worldTick(MovingEntity entity);

}
