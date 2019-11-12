package nextstep.jdbc.utils;

import org.junit.jupiter.api.Test;

import java.util.Properties;

import static nextstep.jdbc.DataSourceFactory.*;
import static org.assertj.core.api.Assertions.assertThat;

class DataSourceUtilsTest {
    @Test
    void createPropertiesTest() {
        // given
        final String filePath = "src/test/java/nextstep/jdbc/support/db.properties";

        // when
        final Properties properties = DataSourceUtils.createProperties(filePath);
        final String driverClass = properties.getProperty(DB_DRIVER);
        final String uri = properties.getProperty(DB_URL);
        final String username = properties.getProperty(DB_USERNAME);
        final String password = properties.getProperty(DB_PASSWORD);

        // then
        assertThat(driverClass).isEqualTo("org.h2.Driver");
        assertThat(uri).isEqualTo("jdbc:h2:mem:jwp-framework");
        assertThat(username).isEqualTo("techcourse");
        assertThat(password).isEqualTo("password");
    }
}