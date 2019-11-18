package properties;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;

public class DBConnectionTest {
    private final String resource = "db.properties";
    private InputStream inputStream;
    private Properties properties;
    private String actual = null;

    @BeforeEach
    void setUp() {
        inputStream = getClass().getClassLoader().getResourceAsStream(resource);
        properties = new Properties();
    }

    @Test
    void getDriverClass() throws IOException {
        if (inputStream != null) {
            properties.load(inputStream);
            actual = properties.getProperty("jdbc.driverClass");
        }

        assertThat(actual).isEqualTo("com.mysql.cj.jdbc.Driver");
    }

    @Test
    void getUrl() throws IOException {
        if (inputStream != null) {
            properties.load(inputStream);
            actual = properties.getProperty("jdbc.url");
        }

        assertThat(actual).isEqualTo("jdbc:mysql://localhost:13306/jwp_jdbc?useUnicode=true&characterEncoding=utf8");
    }

    @Test
    void getUsername() throws IOException {
        if (inputStream != null) {
            properties.load(inputStream);
            actual = properties.getProperty("jdbc.username");
        }

        assertThat(actual).isEqualTo("techcourse");
    }

    @Test
    void getPassword() throws IOException {
        if (inputStream != null) {
            properties.load(inputStream);
            actual = properties.getProperty("jdbc.password");
        }

        assertThat(actual).isEqualTo("password");
    }
}
