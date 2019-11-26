package sql;

import nextstep.jdbc.JdbcTemplate;

public class DataBaseInitialSetter {
    private static final String path = "/Users/mac/level3/jwp-jdbc/slipp/src/main/resources/db.properties";
    private JdbcTemplate jdbcTemplate;

    public DataBaseInitialSetter() {
        jdbcTemplate = new JdbcTemplate(path);
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }
}
