package hr.algebra.dal.sql;

import hr.algebra.dal.Repository;
import hr.algebra.model.Contact;
import hr.algebra.model.Message;
import javafx.scene.image.Image;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.*;
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

    //MESSAGE CONSTANTS
    private static final String ID_MESSAGE = "IDMessage";
    private static final String MESSAGE_CONTENT = "MessageContent";
    private static final String FROM_ID = "FromID";
    private static final String TO_ID = "ToID";
    private static final String TIME_OF = "TimeOf";
    private static final String IS_IMAGE = "IsImage";

    //CONTACT PROCEDURE CONSTANTS
    private static final String CREATE_CONTACT = " { call spCreateContact(?, ?, ?, ?) } ";
    private static final String AUTHENTICATE_CONTACT = " { call spAuthenticateContact(?, ?) } ";
    private static final String UPDATE_CONTACT = " { call spUpdateContact(?, ?) } ";
    private static final String SELECT_USER_CONTACTS = " { call spSelectUserContacts(?) } ";

    //MESSAGE PROCEDURE CONSTANTS
    private static final String CREATE_MESSAGE = " { call spCreateMessage(?, ?, ?, ?, ?) } ";
    private static final String SELECT_MESSAGES = "{ call spSelectConversationMessages(?, ?) }";

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

    @Override
    public void createMessage(Message message) throws SQLException {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection connection = dataSource.getConnection();
             CallableStatement statement = connection.prepareCall(CREATE_MESSAGE)) {
            statement.setBytes(1, message.getMessageContent());
            statement.setInt(2, message.getFromId());
            statement.setInt(3, message.getToId());
            statement.setTimestamp(4, message.getTime());
            statement.setBoolean(5, message.isImage());
            statement.executeUpdate();
        }
    }

    @Override
    public List<Message> selectMessages(int userId, int contactId) {
        List<Message> messages = new ArrayList<>();
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection connection = dataSource.getConnection();
             CallableStatement statement = connection.prepareCall(SELECT_MESSAGES)) {
            statement.setInt(1, userId);
            statement.setInt(2, contactId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    messages.add(new Message(
                            resultSet.getInt(ID_MESSAGE),
                            resultSet.getBytes(MESSAGE_CONTENT),
                            resultSet.getInt(FROM_ID),
                            resultSet.getInt(TO_ID),
                            resultSet.getTimestamp(TIME_OF),
                            resultSet.getBoolean(IS_IMAGE)
                    ));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return messages;
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
