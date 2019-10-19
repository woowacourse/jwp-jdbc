package slipp.support.db;

import org.apache.commons.dbcp2.BasicDataSource;

import javax.sql.DataSource;

public class ConnectionManager {
    private static final String DB_DRIVER = "org.h2.Driver";
    private static final String DB_URL = "jdbc:h2:mem:jwp-framework";
    private static final String DB_USERNAME = "sa";
    private static final String DB_PW = "";

    private static final BasicDataSource ds = new BasicDataSource();

    public static DataSource getDataSource() {

        ds.setDriverClassName(DB_DRIVER);
        ds.setUrl(DB_URL);
        ds.setUsername(DB_USERNAME);
        ds.setPassword(DB_PW);
        return ds;
    }
}
