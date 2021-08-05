package unsw.loopmania.buildings;

import java.util.ArrayList;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.Character;
import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.status.CampfireBuff;
import unsw.loopmania.observer.Observer;
import unsw.loopmania.observer.Subject;

public class CampfireBuilding extends Building implements Subject{
    
    private ArrayList<Observer> listVampire = new ArrayList<Observer>();

    public CampfireBuilding(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
        setID("CampfireBuilding");
    }

    /**
     * Buff the character if within range
     */
    @Override
    public void update(LoopManiaWorld world) {

        // if the character is in range of the campfire and in battle
        // and adds buff if condition is true
        Character hero = world.getCharacter();

        int startX = this.getX() - 2;
        int startY = this.getY() - 2;

        // looks at a grid around the campfire with the CampFire in the centre
        for (int dx = 0; dx < 5; dx++) {
            for (int dy = 0; dy < 5; dy++) {
                if (hero.getX() == startX + dx && hero.getY() == startY + dy /* && hero is in battle */) {
                    hero.updateStats();
                    // if (!hero.hasStatus("CampfireBuff")) {
                    //    hero.receiveStatus(new CampfireBuff) <- a buff that lasts one worldtick, but infinitely applies the buff
                    // }
                    hero.receiveStatus(new CampfireBuff());
                    return;
                }
                
            }
        }

    }

    @Override
    public void registerObserver(Observer observer) {
        if(!listVampire.contains(observer)){
            listVampire.add(observer);
        }   
    }

    @Override
    public void removeObserver(Observer observer) {
        listVampire.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for(Observer observer : listVampire){
            observer.update(this);
        } 
    }


}
