package unsw.loopmania;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import java.util.List;

import org.json.JSONObject;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import unsw.loopmania.buildings.HeroCastleBuilding;
import javafx.geometry.Rectangle2D;

import java.io.File;

/**
 * A LoopManiaLoader that also creates the necessary ImageViews for the UI,
 * connects them via listeners to the model, and creates a controller.
 * 
 * this should NOT be used to load any entities which spawn, or might be removed (use controller for that)
 * since this doesnt add listeners or teardown functions (so it will be very hacky to remove event handlers)
 */
public class LoopManiaWorldControllerLoader extends LoopManiaWorldLoader {

    private List<ImageView> entities;

    //Images
    private Image heroCastleImage;
    private Image characterImage;
    private Image pathTilesImage;

    private ImageManager imageManager;

    public LoopManiaWorldControllerLoader(String filename, String gameMode)
            throws FileNotFoundException {
        super(filename);
        entities = new ArrayList<>();
        heroCastleImage = new Image((new File("src/images/pkmn_center.png")).toURI().toString());
        characterImage = new Image((new File("src/images/red.png")).toURI().toString());
        //pathTilesImage = new Image((new File("src/images/32x32GrassAndDirtPath.png")).toURI().toString());
        pathTilesImage = new Image((new File("src/images/32x32Path.png")).toURI().toString());

        imageManager = new ImageManager();
        
    }

    // TODO = load more entity types from the file
    @Override
    public void onLoad(Character character) {
        ImageView view = new ImageView(characterImage);
        addEntity(character, view);
    }
    @Override
    public void onLoad(HeroCastleBuilding heroCastle) {
        ImageView view = new ImageView(heroCastleImage);
        addEntity(heroCastle, view);
    }
    /**
     * Campaign! load the frontend images of the stage boss
     */
    @Override
    public void loadStageBossImages(LoopManiaWorld world, JSONObject json) {
        JSONObject jsonStageBoss = json.getJSONObject("stage_boss");
        imageManager.setStageBossImages(jsonStageBoss.getString("boss_image"), jsonStageBoss.getString("pet_image"),
                                        jsonStageBoss.getString("boss_battle_image"), jsonStageBoss.getString("pet_battle_image"),
                                        jsonStageBoss.getString("card_image"), jsonStageBoss.getString("building_image")
                                        );
    }
    /**
     * load path tile ImageView based on configuration in file.
     * Note how src/images/32x32GrassAndDirtPath.png has 8 images within it
     * x and y values we produce here are the coordinates of the top left of our sub-image, taken from the top-left of the pathTilesImage
     */
    @Override
    public void onLoad(PathTile pathTile, PathTile.Direction into, PathTile.Direction out) {
        // note https://stackoverflow.com/a/58041962
        // we need to find the offset within the rectangle, we can do this from adjacencies
        ImageView view = new ImageView(pathTilesImage);
        // add ids to view so highlighter can easily do its logic
        view.setId("pathView");
        int x;
        int y;

        if (into == out && (into == PathTile.Direction.DOWN || into == PathTile.Direction.UP)) {
            // vertical, no corners
            x = 32;
            y = 0;
        } else if ((into == PathTile.Direction.DOWN && out == PathTile.Direction.RIGHT) || (into == PathTile.Direction.LEFT && out == PathTile.Direction.UP)) {
            // corner piece, turning left up, or turning down right
            x = 0;
            y = 64;
        } else if ((into == PathTile.Direction.UP && out == PathTile.Direction.RIGHT) || (into == PathTile.Direction.LEFT && out == PathTile.Direction.DOWN)) {
            // corner piece, turning up right, or turning left down
            x = 32;
            y = 32;
        } else if ((into == PathTile.Direction.DOWN && out == PathTile.Direction.LEFT) || (into == PathTile.Direction.RIGHT && out == PathTile.Direction.UP)) {
            // corner piece, turning down left, or turning right up
            x = 32;
            y = 64;
        } else if ((into == PathTile.Direction.RIGHT && out == PathTile.Direction.DOWN) || (into == PathTile.Direction.UP && out == PathTile.Direction.LEFT)) {
            // corner piece, turning right down, or turning up left
            x = 0;
            y = 32;
        } else if (into == out && (into == PathTile.Direction.LEFT || into == PathTile.Direction.RIGHT)) {
            // horizontal, no corners
            x = 0;
            y = 96;
        } else {
            throw new RuntimeException("Not a valid adjacency mapping " + into + "->" + out);
        }
        Rectangle2D imagePart = new Rectangle2D(x, y, 32, 32);
        view.setViewport(imagePart);
        addEntity(pathTile, view);
    }


    /**
     * pair the  backendentity and view, so the view tracks the coordinates of the entity
     * @param entity backend entity
     * @param view frontend image to be paired with the backend entity
     */
    private void addEntity(Entity entity, ImageView view) {
        trackPositionOfNonSpawningEntities(entity, view);
        entities.add(view);
    }


    /**
     * Track the position of entities which don't spawn or require removal.
     * We only setup the node to follow the coordinates of the backend entity.<br>
     * Items which potentially need to be removed should be spawned by controller, and have listener handles and teardown functions added.
     * @param entity backend entity
     * @param node frontend image to track the coordinates of the backend entity
     */
    private static void trackPositionOfNonSpawningEntities(Entity entity, Node node) {
        GridPane.setColumnIndex(node, entity.getX());
        GridPane.setRowIndex(node, entity.getY());
        entity.x().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable,
                    Number oldValue, Number newValue) {
                GridPane.setColumnIndex(node, newValue.intValue());
            }
        });
        entity.y().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable,
                    Number oldValue, Number newValue) {
                GridPane.setRowIndex(node, newValue.intValue());
            }
        });

    }

    /**
     * Create a controller that can be attached to the LoopManiaView with all the
     * loaded entities.
     * @return
     * @throws FileNotFoundException
     */
    public LoopManiaWorldController loadController(String gameMode) throws FileNotFoundException {
        return new LoopManiaWorldController(load(gameMode), gameMode, imageManager, entities);
    }


}
