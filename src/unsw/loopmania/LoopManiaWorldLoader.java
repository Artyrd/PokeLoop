package unsw.loopmania;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

import org.javatuples.Pair;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.buildings.HeroCastleBuilding;
import unsw.loopmania.game_mode.BerserkerWorld;
import unsw.loopmania.game_mode.SurvivalWorld;
import unsw.loopmania.game_mode.ConfusingWorld;
import unsw.loopmania.movement.PathPosition;

import java.util.List;

import unsw.loopmania.items.Anduril;
import unsw.loopmania.items.TheOneRing;
import unsw.loopmania.items.TreeStump;

/**
 * Loads a world from a .json file.
 * 
 * By extending this class, a subclass can hook into entity creation.
 * This is useful for creating UI elements with corresponding entities.
 * 
 * this class is used to load the world.
 * it loads non-spawning entities from the configuration files.
 * spawning of enemies/cards must be handled by the controller.
 */
public abstract class LoopManiaWorldLoader {
    private JSONObject json;
    private GoalLoader goalLoader;

    public LoopManiaWorldLoader(String filename) throws FileNotFoundException {
        json = new JSONObject(new JSONTokener(new FileReader("worlds/" + filename)));
        goalLoader = new GoalLoader();
        //json = new JSONObject(new JSONTokener(new FileReader(filename)));
    }

    /**
     * Parses the JSON to create a world.
     */
    public LoopManiaWorld load(String gameMode) {
        int width = json.getInt("width");
        int height = json.getInt("height");

        // path variable is collection of coordinates with directions of path taken...
        List<Pair<Integer, Integer>> orderedPath = loadPathTiles(json.getJSONObject("path"), width, height);

        LoopManiaWorld world;
        // Set the gamemode of the world according to the selection
        switch (gameMode) {
            case ("Standard"):
                System.out.println("Loader is loading Standard Mode!!!!");
                world = new LoopManiaWorld(width, height, orderedPath);
                break;
            case ("Survival"):
                System.out.println("Loader is loading Survival Mode!!!!");
                world = new SurvivalWorld(width, height, orderedPath);
                break;
            case ("Berserker"):
                System.out.println("Loader is loading Berserker Mode!!!!");
                world = new BerserkerWorld(width, height, orderedPath);
                break;
            case ("Confusing"):
                System.out.println("Loader is loading Confusing Mode!!!!");
                world = new ConfusingWorld(width, height, orderedPath);
                break;
            default:
                System.out.println("Loader is loading default!!!!");
                world = new LoopManiaWorld(width, height, orderedPath);
        }


        // load the rare items
        JSONArray jsonRareItems = json.getJSONArray("rare_items");
        // load non-path entities later so that they're shown on-top
        for (int i = 0; i < jsonRareItems.length(); i++) {
            loadRareItem(world, jsonRareItems.getString(i));
        }

        int stageNumber = json.getInt("stage");
        world.setStageNumber(stageNumber);

        loadStageBossImages(world, json);
        loadStageBossManager(world, json);


        JSONArray jsonEntities = json.getJSONArray("entities");
        // load non-path entities later so that they're shown on-top
        for (int i = 0; i < jsonEntities.length(); i++) {
            loadEntity(world, jsonEntities.getJSONObject(i), orderedPath);
        }

        JSONObject jsonGoal = json.getJSONObject("goal-condition");
        world.setWorldGoal(goalLoader.loadGoals(jsonGoal));

        return world;
    }

    //private void loadRareItem(LoopManiaWorld world, JSONObject currentJson) {
    private void loadRareItem(LoopManiaWorld world, String type) {
        //String type = currentJson.getString("type");
        switch (type) {
            case "the_one_ring":
                world.addPossibleRareItem(new TheOneRing());
                break;
            case "anduril_flame_of_the_west":
                world.addPossibleRareItem(new Anduril(null, null));
                break;
            case "tree_stump":
                world.addPossibleRareItem(new TreeStump(null, null));
                break;
        }
        
    }

    /**
     * load an entity into the world
     * @param world backend world object
     * @param json a JSON object to parse (different from the )
     * @param orderedPath list of pairs of x, y cell coordinates representing game path
     */
    private void loadEntity(LoopManiaWorld world, JSONObject currentJson, List<Pair<Integer, Integer>> orderedPath) {
        String type = currentJson.getString("type");
        int x = currentJson.getInt("x");
        int y = currentJson.getInt("y");
        int indexInPath = orderedPath.indexOf(new Pair<Integer, Integer>(x, y));
        assert indexInPath != -1;

        Entity entity = null;
        // TODO = load more entity types from the file
        switch (type) {
        case "hero_castle":
            // make the Hero's Castle
            HeroCastleBuilding heroCastle = new HeroCastleBuilding(new SimpleIntegerProperty(x), new SimpleIntegerProperty(y));
            world.addBuilding(heroCastle);
            onLoad(heroCastle);
            // add the Character
            Character character = new Character(new PathPosition(indexInPath, orderedPath), world.inventory);
            world.setCharacter(character);
            onLoad(character);
            entity = character;
            break;
        case "path_tile":
            throw new RuntimeException("path_tile's aren't valid entities, define the path externally.");
        // TODO Handle other possible entities
        }
        world.addEntity(entity);
    }

    /**
     * load path tiles
     * @param path json data loaded from file containing path information
     * @param width width in number of cells
     * @param height height in number of cells
     * @return list of x, y cell coordinate pairs representing game path
     */
    private List<Pair<Integer, Integer>> loadPathTiles(JSONObject path, int width, int height) {
        if (!path.getString("type").equals("path_tile")) {
            // ... possible extension
            throw new RuntimeException(
                    "Path object requires path_tile type.  No other path types supported at this moment.");
        }
        PathTile starting = new PathTile(new SimpleIntegerProperty(path.getInt("x")), new SimpleIntegerProperty(path.getInt("y")));
        if (starting.getY() >= height || starting.getY() < 0 || starting.getX() >= width || starting.getX() < 0) {
            throw new IllegalArgumentException("Starting point of path is out of bounds");
        }
        // load connected path tiles
        List<PathTile.Direction> connections = new ArrayList<>();
        for (Object dir: path.getJSONArray("path").toList()){
            connections.add(Enum.valueOf(PathTile.Direction.class, dir.toString()));
        }

        if (connections.size() == 0) {
            throw new IllegalArgumentException(
                "This path just consists of a single tile, it needs to consist of multiple to form a loop.");
        }

        // load the first position into the orderedPath
        PathTile.Direction first = connections.get(0);
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        orderedPath.add(Pair.with(starting.getX(), starting.getY()));

        int x = starting.getX() + first.getXOffset();
        int y = starting.getY() + first.getYOffset();

        // add all coordinates of the path into the orderedPath
        for (int i = 1; i < connections.size(); i++) {
            orderedPath.add(Pair.with(x, y));
            
            if (y >= height || y < 0 || x >= width || x < 0) {
                throw new IllegalArgumentException("Path goes out of bounds at direction index " + (i - 1) + " (" + connections.get(i - 1) + ")");
            }
            
            PathTile.Direction dir = connections.get(i);
            PathTile tile = new PathTile(new SimpleIntegerProperty(x), new SimpleIntegerProperty(y));
            x += dir.getXOffset();
            y += dir.getYOffset();
            if (orderedPath.contains(Pair.with(x, y)) && !(x == starting.getX() && y == starting.getY())) {
                throw new IllegalArgumentException("Path crosses itself at direction index " + i + " (" + dir + ")");
            }
            onLoad(tile, connections.get(i - 1), dir);
        }
        // we should connect back to the starting point
        if (x != starting.getX() || y != starting.getY()) {
            throw new IllegalArgumentException(String.format(
                    "Path must loop back around on itself, this path doesn't finish where it began, it finishes at %d, %d.",
                    x, y));
        }
        onLoad(starting, connections.get(connections.size() - 1), connections.get(0));
        return orderedPath;
    }
    
    public void loadStageBossManager(LoopManiaWorld world, JSONObject json) {
        // load the StageBossManager
        JSONObject jsonStageBoss = json.getJSONObject("stage_boss");
        world.setStageBossManager(new StageBossManager(jsonStageBoss));
    }


    public abstract void loadStageBossImages(LoopManiaWorld world, JSONObject json);
    public abstract void onLoad(HeroCastleBuilding heroCastle); // <- added
    public abstract void onLoad(Character character);
    public abstract void onLoad(PathTile pathTile, PathTile.Direction into, PathTile.Direction out);


}
