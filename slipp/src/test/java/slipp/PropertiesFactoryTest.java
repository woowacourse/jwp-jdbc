package slipp;

import org.junit.jupiter.api.Test;

import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;

class PropertiesFactoryTest {
    @Test
    void properties() {
        String filePath = "./src/test/resources/test.properties";
        Properties properties = PropertiesFactory.generate(filePath);

        assertThat(properties.getProperty("test.key1")).isEqualTo("value1");
        assertThat(properties.getProperty("test.key2")).isEqualTo("value2");
    }
}