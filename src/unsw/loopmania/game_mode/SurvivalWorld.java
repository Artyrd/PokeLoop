package unsw.loopmania.game_mode;

import org.javatuples.Pair;
import java.util.List;

import unsw.loopmania.LoopManiaWorld;

public class SurvivalWorld extends LoopManiaWorld {

    public SurvivalWorld(int width, int height, List<Pair<Integer, Integer>> orderedPath) {
        super(width,  height, orderedPath);
        setGamemode("Survival");
    }
    
}
