package hr.algebra.main;

import hr.algebra.utils.ReflectionUtils;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main extends Application {

    FXMLLoader loader;
    private static final String CLASSES_PATH = "src/hr/algebra/model";
    private static final String CLASSES_PACKAGE = "hr.algebra.model.";

    @Override
    public void start(Stage primaryStage) throws Exception{
        loader = new FXMLLoader(getClass().getClassLoader().getResource("view/login.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("Login page");
        primaryStage.setScene(new Scene(root, 900, 600));
        primaryStage.setResizable(false);
        primaryStage.sizeToScene();
        primaryStage.show();

        createDocumentation();
        //ReflectionUtils.readClassAndMembersInfo(Contact.class);
    }

    private void createDocumentation() {
        try(FileWriter docGenerator = new FileWriter("documentation.html");
                DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(CLASSES_PATH))){
            stream.forEach(file ->{
                String fileName = file.getFileName().toString();
                String className = fileName.substring(0, fileName.indexOf("."));
                try{
                    Class<?> clazz = Class.forName(CLASSES_PACKAGE.concat(className));
                    ReflectionUtils.readClassAndMembersInfo(clazz, docGenerator);
                } catch (ClassNotFoundException | IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
