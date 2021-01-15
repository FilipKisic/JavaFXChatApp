package hr.algebra.dal;

import hr.algebra.model.Message;

import java.sql.SQLException;

public interface Repository {
    void createMessage(Message message) throws SQLException;
}
