package hr.algebra.controllers;

import hr.algebra.dal.ContactHolderSingleton;
import hr.algebra.model.Contact;
import hr.algebra.model.Transferable;
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
    private final ContactHolderSingleton contactHolder = ContactHolderSingleton.getInstance();
    private Transferable contactTransfer;

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

    /* !!! PROBLEM Need to call initializeContactTitle in Controller to draw new pane !!!*/
    private void loadContact() {
        /*try{
            FXMLLoader loader =  new FXMLLoader(getClass().getClassLoader().getResource("view/contactTitle.fxml"));
            Parent root = loader.load();
            ContactTitleController controllerTitle = loader.getController();
            controllerTitle.setCurrentContact(contact);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        }catch(Exception ex){
            ex.printStackTrace();
        }*/
        contactTransfer.transfer(contact);
    }

}
