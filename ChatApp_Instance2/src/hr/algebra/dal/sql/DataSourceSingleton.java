package hr.algebra.dal.sql;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;

import javax.sql.DataSource;

public class DataSourceSingleton {
    public static final String SERVER_NAME = "localhost";
    public static final String DATABASE_NAME = "ChatApp";
    public static final String UID = "sa";
    public static final String PWD = "SQL";

    private static DataSource instance;

    private DataSourceSingleton(){ }

    public static DataSource getInstance(){
        if(instance == null){
            synchronized (DataSource.class){
                if(instance == null)
                    instance = createInstance();
            }
        }
        return instance;
    }

    private static DataSource createInstance() {
        SQLServerDataSource dataSource = new SQLServerDataSource();
        dataSource.setServerName(SERVER_NAME);
        dataSource.setDatabaseName(DATABASE_NAME);
        dataSource.setUser(UID);
        dataSource.setPassword(PWD);
        return dataSource;
    }
}
