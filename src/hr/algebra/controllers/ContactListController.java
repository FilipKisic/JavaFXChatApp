package hr.algebra.controllers;

import hr.algebra.dal.Repository;
import hr.algebra.dal.RepositoryFactory;
import hr.algebra.model.Contact;
import hr.algebra.model.ContactProvider;
import hr.algebra.model.UserProvider;
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
    private Contact user;
    private Repository repository;
    private List<Contact> contacts;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        UserProvider userHolder = UserProvider.getInstance();
        user = userHolder.getUser();
        repository = RepositoryFactory.getRepository();
        loadContacts();
    }

    private void loadContacts() {
        try {
            contacts = repository.selectUserContacts(user.getIdContact());
        } catch (SQLException ex) {
            DialogUtils.showErrorDialog("Error", "SqlException", ex.getMessage());
            ex.printStackTrace();
        }
        ContactProvider contactHolder = ContactProvider.getInstance();
        /* fill vbContacts with contactCards */
        for(Contact contact : contacts){
            contactHolder.setContact(contact);
            Pane pane = new FxmlLoader().getScene("contactCard");
            vbContacts.getChildren().add(pane);
        }
    }

}
