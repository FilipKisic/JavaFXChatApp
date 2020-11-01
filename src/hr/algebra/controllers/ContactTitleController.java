package hr.algebra.controllers;

import hr.algebra.model.Contact;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class ContactTitleController implements Initializable {
    @FXML
    public ImageView ivContactImage;
    @FXML
    public Label tfContactName;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
    public void setCurrentContact(Contact contact){
        ivContactImage.setImage(contact.getProfileImage());
        tfContactName.setText(contact.getFullName());
    }

}
