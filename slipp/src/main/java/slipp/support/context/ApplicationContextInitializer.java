package slipp.support.context;

import nextstep.jdbc.JdbcTemplate;
import org.apache.commons.dbcp2.BasicDataSource;

public class ApplicationContextInitializer {

    public static void registerBeans() {
        registerDataSource();
        registerJdbcTemplate();
    }

    private static void registerDataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setUrl("jdbc:h2:mem:jwp-framework");
        dataSource.setUsername("sa");
        dataSource.setPassword("");

        ApplicationContext.register(dataSource);
    }

    private static void registerJdbcTemplate() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(ApplicationContext.getBean(BasicDataSource.class));

        ApplicationContext.register(jdbcTemplate);
    }
}
