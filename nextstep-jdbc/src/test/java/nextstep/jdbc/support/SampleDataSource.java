package nextstep.jdbc.support;


import org.apache.commons.dbcp2.BasicDataSource;

import javax.sql.DataSource;

public class SampleDataSource {
    private static final String DB_DRIVER = "org.h2.Driver";
    private static final String DB_URL = "jdbc:h2:mem:jdbc-framework";
    private static final String DB_USERNAME = "sa";
    private static final String DB_PW = "";

    private SampleDataSource() {
    }

    public static DataSource getDataSource() {
        final BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName(DB_DRIVER);
        ds.setUrl(DB_URL);
        ds.setUsername(DB_USERNAME);
        ds.setPassword(DB_PW);
        return ds;
    }
}
