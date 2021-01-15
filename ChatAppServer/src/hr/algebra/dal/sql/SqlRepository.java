package hr.algebra.dal.sql;

import hr.algebra.dal.Repository;
import hr.algebra.model.Message;

import javax.sql.DataSource;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

public class SqlRepository implements Repository {

    private static final String CREATE_MESSAGE = " { call spCreateMessage(?, ?, ?, ?, ?) } ";
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
}
