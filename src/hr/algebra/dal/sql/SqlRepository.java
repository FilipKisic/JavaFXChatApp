package hr.algebra.dal.sql;

import hr.algebra.dal.Repository;
import hr.algebra.model.Contact;
import javafx.scene.image.Image;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SqlRepository implements Repository {

    //CONTACT CONSTANTS
    private static final String USERNAME = "Username";
    private static final String PSWRD = "Pswrd";
    private static final String FULLNAME = "FullName";
    private static final String ID_CONTACT = "IDContact";
    private static final String PROFILE_IMAGE = "ProfileImage";

    //CONTACT PROCEDURE CONSTANTS
    private static final String CREATE_CONTACT = " { call spCreateContact(?, ?, ?, ?) } ";
    private static final String AUTHENTICATE_CONTACT = " { call spAuthenticateContact(?, ?) } ";
    private static final String UPDATE_CONTACT = " { call spUpdateContact(?, ?) } ";
    private static final String SELECT_USER_CONTACTS = "{ call spSelectUserContacts(?) }";

    @Override
    public void createContact(Contact contact) throws SQLException {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection connection = dataSource.getConnection();
         CallableStatement statement = connection.prepareCall(CREATE_CONTACT)) {
            statement.setString(1, contact.getUsername());
            statement.setString(2, contact.getPassword());
            statement.setString(3, contact.getFullName());
            statement.setBytes(4, null);
            statement.executeUpdate();
        }
    }

    @Override
    public Optional<Contact> authenticateContact(String username, String password) throws SQLException {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection connection = dataSource.getConnection();
             CallableStatement statement = connection.prepareCall(AUTHENTICATE_CONTACT)) {
            statement.setString(1, username);
            statement.setString(2, password);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(new Contact(
                            resultSet.getInt(ID_CONTACT),
                            resultSet.getString(USERNAME),
                            resultSet.getString(PSWRD),
                            resultSet.getString(FULLNAME),
                            getImage(resultSet)
                    ));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public void updateContact(int idContact, String fullname) throws SQLException {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection connection = dataSource.getConnection();
             CallableStatement statement = connection.prepareCall(UPDATE_CONTACT)) {
            statement.setInt(1, idContact);
            statement.setString(2, fullname);

            statement.executeUpdate();
        }
    }

    @Override
    public List<Contact> selectUserContacts(int contactUserID) throws SQLException {
        List<Contact> contacts = new ArrayList<>();
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection connection = dataSource.getConnection();
             CallableStatement statement = connection.prepareCall(SELECT_USER_CONTACTS)) {
            statement.setInt(1, contactUserID);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    contacts.add(new Contact(
                            resultSet.getInt(ID_CONTACT),
                            resultSet.getString(FULLNAME),
                            getImage(resultSet)
                    ));
                }
            }
        }
        return contacts;
    }

    private Image getImage(ResultSet resultSet) throws SQLException {
        Image profileImage;
        InputStream inputStream = resultSet.getBinaryStream(PROFILE_IMAGE);
        if (inputStream != null)
            profileImage = new Image(inputStream);
        else
            profileImage = new Image(getClass().getResourceAsStream("/images/default-profile.png"));
        return profileImage;
    }
}
