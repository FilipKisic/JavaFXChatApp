package hr.algebra.dal;

import hr.algebra.model.AppUser;
import hr.algebra.model.Contact;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface Repository {

    /* APP_USER METHODS */
    void createAppUser(AppUser appUser) throws SQLException;
    Optional<AppUser> selectAppUser(String username, String password) throws SQLException;
    void updateAppUser(int idAppUser, String fullname) throws SQLException;
    List<Contact> selectUserContacts(int idAppUser) throws SQLException;
}
