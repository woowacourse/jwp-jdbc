package nextstep.jdbc;

import nextstep.jdbc.support.DataBaseInitializer;
import nextstep.jdbc.support.SampleDataSource;

import javax.sql.DataSource;

class JdbcTemplateTest {
    private DataSource dataSource = SampleDataSource.getDataSource();
    private JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

    JdbcTemplateTest() {
        DataBaseInitializer.init(dataSource);
    }

}