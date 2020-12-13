package hr.algebra.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

import java.net.URL;

public class FxmlLoader {
    private Pane scene;

    public Pane getScene(String sceneName) {
        try{
            URL fileUrl = getClass().getClassLoader().getResource( "view/" + sceneName + ".fxml");
            if(fileUrl == null)
                throw new Exception("Couldn't load the selected scene...");
            scene = FXMLLoader.load(fileUrl);
        } catch (Exception ex){
            DialogUtils.showErrorDialog("Error", "FxmlLoader failed", ex.getMessage());
            ex.printStackTrace();
        }
        return scene;
    }
}
