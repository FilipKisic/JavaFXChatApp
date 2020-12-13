package hr.algebra.dal;

import hr.algebra.dal.sql.SqlRepository;

public class RepositoryFactory {
    public static Repository getRepository(){
        return new SqlRepository();
    }
}
