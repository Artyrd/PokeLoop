package unsw.loopmania.battle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.javatuples.Pair;

import unsw.loopmania.MovingEntity;
import unsw.loopmania.enemies.Boss;
import unsw.loopmania.enemies.Enemy;
import unsw.loopmania.Character;
import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.Ally;

public class BattleManager {
    
    private LoopManiaWorld world;

    public BattleManager(LoopManiaWorld world) {
        this.world = world;
    }

    /**
     * kill an enemy
     * @param enemy enemy to be killed
     */
    public void killEnemy(Enemy enemy){
        List<Enemy> enemies = world.getEnemies();
        List<Boss> aliveBosses = world.getAliveBosses();
        List<Boss> defeatedBosses = world.getDefeatedBosses();

        enemy.destroy();
        if(enemy instanceof Boss){
            aliveBosses.remove(enemy);
            defeatedBosses.add((Boss) enemy);
            enemies.remove(enemy);
            System.out.println(aliveBosses.toString());
        } else {
            enemies.remove(enemy);
            System.out.println(enemies.toString());
        }
    }

    /**
     * Check if a battle can be started in the world
     * ie, if a character is within battle range of an enemy
     * Then check for support radii
     * @return
     */
    public BattleSimulator findBattle(){

        List<Enemy> enemies = world.getEnemies();
        Character character = world.getCharacter();
        List<MovingEntity> activeStructures = world.getActiveStructures();
        List<MovingEntity> friendlyEntities = world.getFriendlyEntities();
        List<Ally> allies = world.getAllies();


        // Create potentialFighting list which begins with all enemies in the world
        ArrayList<MovingEntity> potentialFighting = new ArrayList<MovingEntity>(enemies);
        ArrayList<MovingEntity> fighters = new ArrayList<MovingEntity>();
        List<Pair<MovingEntity, Double>> pairEnemyDistance = new ArrayList<Pair<MovingEntity, Double>>();
        ArrayList<MovingEntity> structures = new ArrayList<MovingEntity>();

        // For each enemy in potentialFighting, find the distance from enemy to character 
        // If character is in battle radius of enemy, add enemy to a pairEnemyDistance list, remove them from potentialFighting list
        for(MovingEntity enemy: enemies) {
            double distanceSquaredBR = inRangeOfCharacter(character, enemy, enemy.getBattleRadius());
            if(distanceSquaredBR >= 0){
                potentialFighting.remove(enemy);
                Pair<MovingEntity, Double> pairEnemy = new Pair<MovingEntity, Double>(enemy, distanceSquaredBR);
                pairEnemyDistance.add(pairEnemy);
            }
        }
        // if no enemies within battle radius, no battle happens
        if (pairEnemyDistance.size() == 0) {
            return null;
        
        // else a battle is about to begin!!!
        } else if(pairEnemyDistance.size() > 0) {
            // For remaining enemies in potentialFighting list, if fighters list size > 0 and if character is in support radius of that enemy
            // also add that enemy to pairEnemyDistance list
            for(MovingEntity enemy : potentialFighting){
                double distanceSquaredSR = inRangeOfCharacter(character, enemy, enemy.getSupportRadius());
                if(distanceSquaredSR >= 0){
                    Pair<MovingEntity, Double> pairEnemy = new Pair<MovingEntity, Double>(enemy, distanceSquaredSR);
                    pairEnemyDistance.add(pairEnemy);
                }
            }
        }

         // Sort pairEnemyDistance list by distance of enemy to character, so that the closest enemy is first, the furthest enemy is last
        Collections.sort(pairEnemyDistance, new Comparator<Pair<MovingEntity, Double>>() {
            @Override
            public int compare(final Pair<MovingEntity, Double> p1, final Pair<MovingEntity, Double> p2) {
                if(p1.getValue1() > p2.getValue1()) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });

        // Take all enemies from sorted pairEnemyDistance and add to fighters list
        for(Pair<MovingEntity, Double> pairEnemy : pairEnemyDistance) {
            fighters.add(pairEnemy.getValue0());
        }
        
        // For each tower, find the distance from tower to character
        // If character is in the support radius of tower, add tower to structure list
        if(activeStructures.size() > 0){
            for(MovingEntity structure : activeStructures) {
                if(structure.getID().equals("TowerBattler")) {
                    double distanceSquaredSR = inRangeOfCharacter(character, structure, structure.getSupportRadius());
                    if(distanceSquaredSR >= 0){
                        structures.add(structure);
                    }
                }
            }
        }
        // If character is in the support radius of friendly entities, add to fighters
        if(friendlyEntities.size() > 0){
            for(MovingEntity friendly : friendlyEntities) {
                double distanceSquaredSR = inRangeOfCharacter(character, friendly, friendly.getSupportRadius());
                if(distanceSquaredSR >= 0){
                    fighters.add(friendly);
                }
            }
        }
         
        // Add all allies to fighters list
        for(Ally ally : allies) {
            fighters.add(ally);
        }

        // Construct BattleSimulator class with character, fighters, structures
        BattleSimulator battle = new BattleSimulator(character, fighters, structures);

        // battle is about to begin!
        return battle;
    }

    /**
     * Run the battle 
     * @param battle
     * @return
     */
    public List<Enemy> runBattle(BattleSimulator battle){
        if (battle == null) {
            return null;
        }
        boolean result = battle.runBattle();

        if (result == false) {
            world.setGameStateLose();
        }
        updateAllies();
        ArrayList<Enemy> defeatedEnemies = battle.getDefeated();
        cleanupBattle(defeatedEnemies);
        return defeatedEnemies;
    }

    /**
     * Finalise state of entities after battle
     * @param defeatedEnemies to be removed from the world
     */
    public void cleanupBattle(List<Enemy> defeatedEnemies){
        Character character = world.getCharacter();
        updateAllies();

        ArrayList<MovingEntity> deadFriendlies = new ArrayList<MovingEntity>();
        for (MovingEntity friendly: world.getFriendlyEntities()) {
            if (friendly.getCurrHP() <= 0) {
                deadFriendlies.add(friendly);
                friendly.destroy();
            }
        }
        world.getFriendlyEntities().removeAll(deadFriendlies);

        
        //ArrayList<Enemy> defeatedEnemies = battle.getDefeated();
        for(Enemy defeatedEnemy : defeatedEnemies) {
            killEnemy(defeatedEnemy);
        }

        if (character.getCurrHP() <= 0) {
            world.setGameStateLose();
        }
    }

    /**
     * Helper function to determine if the character is in the radius of the entity by calculating
     * their distance
     * @param character
     * @param entity
     * @param radius
     * @return distanceSquared if the character is inside the radius of entity, otherwise -1
     */
    public double inRangeOfCharacter(Character character, MovingEntity entity, int radius){
        double characterX = character.getX();
        double characterY = character.getY();

        double entityX = entity.getX();
        double entityY = entity.getY();

        double deltaX = characterX - entityX;
        double deltaY = characterY - entityY;

        double distanceSquared = Math.pow(deltaX, 2) + Math.pow(deltaY, 2);
        double radiusSquared = Math.pow(radius, 2); 

        if(distanceSquared <= radiusSquared) {
            return distanceSquared;
        }

        return -1;

    }


    /**
     * Helper function to determine dead allies after a battle and update numAllies
     */
    public void updateAllies(){
        List<Ally> allies = world.getAllies();
        ArrayList<Ally> deadAllies = new ArrayList<Ally>();
        for(Ally ally : allies){
            if(ally.getCurrHP() <= 0){
                deadAllies.add(ally);
            }
        }

        allies.removeAll(deadAllies);
        world.setNumAllies(allies.size());
        //numAllies.set(allies.size());
    }

}
