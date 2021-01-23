package hr.algebra.controllers;

import hr.algebra.model.Contact;
import hr.algebra.model.ContactProvider;
import hr.algebra.model.ControllerProvider;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;

public class ContactCardController implements Initializable {
    @FXML
    public ImageView ivProfileImage;
    @FXML
    public Pane pContactCard;
    @FXML
    private Label lbFullName;
    private Contact contact;
    private final ContactProvider contactHolder = ContactProvider.getInstance();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.contact = contactHolder.getContact();
        lbFullName.setText(contact.getFullName());
        setImage();
    }

    private void setImage() {
        try {
            ivProfileImage.setImage(contact.getProfileImage());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void paneOnMouseClicked() {
        /* get contactID and send it to Controller using ContactHolderSingleton */
        contactHolder.setContact(this.contact);
        loadContact();
    }

    private void loadContact() {
        ControllerProvider controllerProvider = ControllerProvider.getInstance();
        Controller controller = (Controller)controllerProvider.getController();
        controller.setCurrentContact();
        controller.loadMessages();
    }
}
