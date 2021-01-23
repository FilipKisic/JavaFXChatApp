package hr.algebra.controllers;

import hr.algebra.dal.Repository;
import hr.algebra.dal.RepositoryFactory;
import hr.algebra.model.Contact;
import hr.algebra.model.UserProvider;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class SettingsController implements Initializable {

    private static Contact user;
    @FXML
    public TextField tfFirstName;
    @FXML
    public TextField tfLastName;
    private static String[] name;
    private static Repository repository;
    public ImageView ivProfileImage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        UserProvider userHolder = UserProvider.getInstance();
        user = userHolder.getUser();
        initializeTextFields();
        ivProfileImage.setImage(user.getProfileImage());
        repository = RepositoryFactory.getRepository();
    }

    private void initializeTextFields() {
        tfFirstName.setFocusTraversable(false);
        tfLastName.setFocusTraversable(false);
        if(!user.getFullName().isEmpty()) {
            name = user.getFullName().split(" ");
            tfFirstName.setText(name[0]);
            tfLastName.setText(name[1]);
        }
    }

    public void saveData() {
        /*update ContactUser firstName and lastName*/
        if(!tfFirstName.getText().trim().isEmpty() && !tfLastName.getText().trim().isEmpty()) {
            name[0] = tfFirstName.getText().trim();
            name[1] = tfLastName.getText().trim();
        }
        try {
            repository.updateContact(user.getIdContact(), name[0] + " " + name[1]);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void btnExitPressed() {
        System.exit(0);
    }

    public void btnMinimizePressed(ActionEvent actionEvent) {
        ((Stage) ((Button) actionEvent.getSource()).getScene().getWindow()).setIconified(true);
    }
}
