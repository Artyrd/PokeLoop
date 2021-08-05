package unsw.loopmania.game_mode;

import org.javatuples.Pair;
import java.util.List;

import unsw.loopmania.LoopManiaWorld;

public class BerserkerWorld extends LoopManiaWorld {

    public BerserkerWorld(int width, int height, List<Pair<Integer, Integer>> orderedPath) {
        super(width,  height, orderedPath);
        setGamemode("Berserker");
    }
    
}
