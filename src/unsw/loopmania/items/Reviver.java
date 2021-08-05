package unsw.loopmania.items;

import unsw.loopmania.Character;

public interface Reviver {

    /**
     * Revive a dead character (single use)
     * @param character to be revived
     * @return if revival was successful
     */
    public boolean revive(Character character);
   
}
