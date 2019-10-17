package slipp.support.db;

import nextstep.jdbc.JdbcTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;

public class DataBaseManager {
    private static final Logger log = LoggerFactory.getLogger(DataBaseManager.class);

    private static DataSource DATA_SOURCE_INSTANCE = null;
    private static JdbcTemplate JDBC_TEMPLATE_INSTANCE = null;

    public static void initialize(DataSource dataSource) {
        if (dataSource == null) {
            throw new IllegalArgumentException("DataBaseManager Initialization fail : DataSource가 null입니다.");
        }
        DATA_SOURCE_INSTANCE = dataSource;
        JDBC_TEMPLATE_INSTANCE = new JdbcTemplate(DATA_SOURCE_INSTANCE);
        log.info("Completed initialize DataBaseManager!");
    }

    public static DataSource getDataSource() {
        if (DATA_SOURCE_INSTANCE == null) {
            throw new IllegalStateException("초기화되지 않았습니다.");
        }
        return DATA_SOURCE_INSTANCE;
    }

    public static JdbcTemplate getJdbcTemplate() {
        if (JDBC_TEMPLATE_INSTANCE == null) {
            throw new IllegalStateException("초기화되지 않았습니다.");
        }
        return JDBC_TEMPLATE_INSTANCE;
    }
}
