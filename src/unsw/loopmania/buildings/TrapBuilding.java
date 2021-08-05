package unsw.loopmania.buildings;

import java.util.List;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.enemies.Enemy;

public class TrapBuilding extends Building{
    
    double trapDamage;
    
    public TrapBuilding(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
        setID("TrapBuilding");
        trapDamage = 10;
    }

    /**
     * Trap an enemy and deal damage when stepped on
     */
    @Override
    public void update(LoopManiaWorld world) {
        List<Enemy> enemies = world.getEnemies();

        for (Enemy e : enemies) {
            if (e.getX() == this.getX() && e.getY() == this.getY()) {
                e.receiveAttack(null, trapDamage);
                // destroy the trap
                this.destroy();
            }
        }

    }
    
}
