package hr.algebra.dal.sql;

import hr.algebra.dal.Repository;
import hr.algebra.model.AppUser;
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

    //APP_USER CONSTANTS
    private static final String ID_APP_USER = "IDAppUser";
    private static final String USERNAME = "Username";
    private static final String PSWRD = "Pswrd";
    private static final String FULLNAME = "FullName";
    private static final String ID_CONTACT = "IDContact";
    private static final String PROFILE_IMAGE = "ProfileImage";

    //APP_USER PROCEDURE CONSTANTS
    private static final String CREATE_APP_USER = " { call spCreateAppUser(?, ?) } ";
    private static final String SELECT_APP_USER = " { call spSelectAppUser(?, ?) } ";
    private static final String UPDATE_APP_USER = " { call spUpdateAppUser(?, ?) } ";
    private static final String SELECT_USER_CONTACTS = "{ call spSelectUserContacts(?) }";

    @Override
    public void createAppUser(AppUser appUser) throws SQLException {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try(Connection connection = dataSource.getConnection();
            CallableStatement statement = connection.prepareCall(CREATE_APP_USER)){
            statement.setString(1, appUser.getUsername());
            statement.setString(2, appUser.getPassword());
            statement.executeUpdate();
        }
    }

    @Override
    public Optional<AppUser> selectAppUser(String username, String password) throws SQLException {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try(Connection connection = dataSource.getConnection();
            CallableStatement statement = connection.prepareCall(SELECT_APP_USER)){
            statement.setString(1, username);
            statement.setString(2, password);
            try(ResultSet resultSet = statement.executeQuery()){
                if(resultSet.next()){
                    return Optional.of(new AppUser(
                       resultSet.getInt(ID_APP_USER),
                       resultSet.getString(USERNAME),
                       resultSet.getString(PSWRD),
                       resultSet.getString(FULLNAME)
                    ));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public void updateAppUser(int idAppUser, String fullname) throws SQLException {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try(Connection connection = dataSource.getConnection();
            CallableStatement statement = connection.prepareCall(UPDATE_APP_USER)){
            statement.setInt(1, idAppUser);
            statement.setString(2, fullname);

            statement.executeUpdate();
        }
    }

    /*TODO: MODIFY PROFILE_IMAGE */
    @Override
    public List<Contact> selectUserContacts(int idAppUser) throws SQLException {
        List<Contact> contacts = new ArrayList<>();
        DataSource dataSource = DataSourceSingleton.getInstance();
        try(Connection connection = dataSource.getConnection();
            CallableStatement statement = connection.prepareCall(SELECT_USER_CONTACTS)){
            statement.setInt(1, idAppUser);
            try(ResultSet resultSet = statement.executeQuery()){
                while(resultSet.next()){
                    Image profileImage;
                    InputStream inputStream = resultSet.getBinaryStream("ProfileImage");
                    if(inputStream != null)
                        profileImage = new Image(inputStream);
                    else
                        profileImage = new Image(getClass().getResourceAsStream("/images/default-profile.png"));
                    contacts.add(new Contact(
                            resultSet.getInt(ID_CONTACT),
                            resultSet.getString(FULLNAME),
                            profileImage
                    ));
                }
            }
        }
        return contacts;
    }
}
