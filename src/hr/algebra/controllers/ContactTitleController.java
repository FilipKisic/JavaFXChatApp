package hr.algebra.controllers;

import hr.algebra.model.Contact;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

public class ContactTitleController {
    @FXML
    public ImageView ivContactImage;
    @FXML
    public Label tfContactName;

    public void setCurrentContact(Contact contact){
        ivContactImage.setImage(contact.getProfileImage());
        tfContactName.setText(contact.getFullName());
    }

}
