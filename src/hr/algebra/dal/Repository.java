package hr.algebra.dal;

import hr.algebra.model.Contact;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface Repository {

    /* CONTACT METHODS */
    void createContact(Contact contact) throws SQLException;
    Optional<Contact> authenticateContact(String username, String password) throws SQLException;
    void updateContact(int idContact, String fullname) throws SQLException;
    List<Contact> selectUserContacts(int contactUserID) throws SQLException;
}
