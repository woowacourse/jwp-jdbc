package slipp.support.db;

import nextstep.jdbc.DataSourceFactory;

public class DataSource {
    public static javax.sql.DataSource getDataSource() {
        final DataSourceFactory factory = new DataSourceFactory("src/main/resources/db.properties");
        return factory.getDataSource();
    }
}
