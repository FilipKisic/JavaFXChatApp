package hr.algebra.main;

import hr.algebra.utils.ReflectionUtils;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    FXMLLoader loader;

    @Override
    public void start(Stage primaryStage) throws Exception{
        loader = new FXMLLoader(getClass().getClassLoader().getResource("view/login.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("Login page");
        primaryStage.setScene(new Scene(root, 900, 600));
        primaryStage.setResizable(false);
        primaryStage.sizeToScene();
        primaryStage.show();

        ReflectionUtils.createDocumentation();
    }

    public static void main(String[] args) {
        launch(args);
    }
}