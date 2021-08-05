package unsw.loopmania.observer;

import java.util.List;

import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.buildings.*;
import unsw.loopmania.enemies.Enemy;

public class ObserverManager {
    
    private LoopManiaWorld world;

    public ObserverManager(LoopManiaWorld world){
        this.world = world;
    }

    public void updateObservers(){
        List<Building> buildingEntities = world.getBuildings();
        for(Building building : buildingEntities) {
            if (building instanceof Subject) {
                Subject subject = (Subject) building;
                // register all observers
                registerObservers(subject);
                // notify observers
                subject.notifyObservers();
            }
        }
    }


    public void registerObservers(Subject subject){
        List<Enemy> enemies = world.getEnemies();
        for (Enemy enemy : enemies) {
            if (enemy instanceof Observer) {
                Observer observer = (Observer) enemy;
                subject.registerObserver(observer);
            }
        }
    }


}
