package slipp.dao;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class PropertiesTest {
    @Test
    void 프로퍼티_읽기() throws IOException {
        InputStream input = getClass()
                .getClassLoader()
                .getResourceAsStream("db.properties");

        Properties prop = new Properties();
        prop.load(input);
        assertThat(prop.getProperty("jdbc.url")).isNotNull();
        assertThat(prop.getProperty("jdbc.username")).isEqualTo("techcourse");
    }
}
