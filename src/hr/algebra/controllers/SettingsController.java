package hr.algebra.controllers;

import hr.algebra.dal.Repository;
import hr.algebra.dal.RepositoryFactory;
import hr.algebra.dal.UserHolderSingleton;
import hr.algebra.model.AppUser;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class SettingsController implements Initializable {

    private static AppUser user;
    @FXML
    public TextField tfFirstName;
    @FXML
    public TextField tfLastName;
    private static String[] name;
    private static Repository repository;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        UserHolderSingleton userHolder = UserHolderSingleton.getInstance();
        user = userHolder.getUser();
        initializeTextFields();
        repository = RepositoryFactory.getRepository();
    }

    private void initializeTextFields() {
        tfFirstName.setFocusTraversable(false);
        tfLastName.setFocusTraversable(false);
        if(!user.getFullname().isEmpty()) {
            name = user.getFullname().split(" ");
            tfFirstName.setText(name[0]);
            tfLastName.setText(name[1]);
        }
    }

    public void saveData(MouseEvent mouseEvent) {
        /*update AppUser firstName and lastName*/
        if(!tfFirstName.getText().trim().isEmpty() && !tfLastName.getText().trim().isEmpty()) {
            name[0] = tfFirstName.getText().trim();
            name[1] = tfLastName.getText().trim();
        }
        try {
            repository.updateAppUser(user.getIdAppUser(), name[0] + " " + name[1]);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
