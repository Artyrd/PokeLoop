package unsw.loopmania.battle;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import unsw.loopmania.ImageManager;

public class BattleLoader {

    Stage currentBattle;
    BattleController currController;
    ImageManager otherImageManager;

    public BattleLoader() {
    }

    /**
     * Show the battle screen 
     * @param battle
     */
    public void showBattle(BattleSimulator battle) {
        System.out.println("Showing Battle");
        Stage newStage = new Stage();
        newStage.setTitle("PokeBattle");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("BattleView.fxml"));
        
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        BattleController controller = loader.<BattleController>getController();
        controller.transferStageBossBattleImages(otherImageManager);
        controller.loadBattle(battle);
        controller.battleActive.addListener((obs, oldValue, newValue) -> {
            if (newValue == false) {
                closeBattle();
            }
        });

        Scene scene = new Scene(root);

        newStage.setScene(scene);
        currentBattle = newStage;
        currController = controller;
        newStage.show();
    }

    public void closeBattle() {
        currentBattle.close();
    }

    public BattleController getController() {
        return currController;
    }

    public void transferStageBossBattleImages(ImageManager imageManager) {
        this.otherImageManager = imageManager;
    }
    
}
