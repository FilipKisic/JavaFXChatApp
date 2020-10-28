package hr.algebra.controllers;

import hr.algebra.dal.ContactHolderSingleton;
import hr.algebra.dal.Repository;
import hr.algebra.dal.RepositoryFactory;
import hr.algebra.dal.UserHolderSingleton;
import hr.algebra.model.AppUser;
import hr.algebra.model.Contact;
import hr.algebra.utils.DialogUtils;
import hr.algebra.utils.FxmlLoader;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class ContactListController implements Initializable {

    @FXML
    public VBox vbContacts;
    private AppUser user;
    private Repository repository;
    private List<Contact> contacts;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        UserHolderSingleton userHolder = UserHolderSingleton.getInstance();
        user = userHolder.getUser();
        repository = RepositoryFactory.getRepository();
        loadContacts();
    }

    private void loadContacts() {
        try {
            contacts = repository.selectUserContacts(user.getIdAppUser());
        } catch (SQLException ex) {
            DialogUtils.showErrorDialog("Error", "SqlException", ex.getMessage());
            ex.printStackTrace();
        }
        ContactHolderSingleton contactHolder = ContactHolderSingleton.getInstance();
        /* fill vbContacts with contactCards */
        for(Contact contact : contacts){
            contactHolder.setContact(contact);
            Pane pane = new FxmlLoader().getScene("contactCard");
            vbContacts.getChildren().add(pane);
        }
    }

}
