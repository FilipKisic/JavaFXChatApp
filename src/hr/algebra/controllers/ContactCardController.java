package hr.algebra.controllers;

import hr.algebra.model.Contact;
import hr.algebra.model.ContactProvider;
import hr.algebra.model.PaneProvider;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
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
    private PaneProvider paneHolder;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.contact = contactHolder.getContact();
        lbFullName.setText(contact.getFullName());
        setImage();
        paneHolder = PaneProvider.getInstance();
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
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("view/contactTitle.fxml"));
            Pane contactTitle = loader.load();
            ContactTitleController controllerTitle = loader.getController();
            controllerTitle.setCurrentContact(contact);
            BorderPane pane = paneHolder.getPane();
            pane.setCenter(contactTitle);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
