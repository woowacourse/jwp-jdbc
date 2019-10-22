package slipp.support.properties;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class PropertiesFileReaderTest {

    private static final String TEST_RESOURCE = "src/test/resources";

    private PropertiesFileReader propertiesReader;

    @BeforeEach
    void setUp() {
        propertiesReader = new PropertiesFileReader();
    }

    @Test
    @DisplayName("db.properties에서 작성된 값을 읽어올 수 있는지 테스트")
    void properties_read() throws IOException {
        Map<String, String> propertiesMap = propertiesReader.read(TEST_RESOURCE + "/db.properties");

        assertThat(propertiesMap.get("jdbc.driverClass")).isEqualTo("com.mysql.cj.jdbc.Driver");
        assertThat(propertiesMap.get("jdbc.url")).isEqualTo("jdbc:mysql://localhost:13306/jwp_jdbc?useUnicode=true&characterEncoding=utf8");
        assertThat(propertiesMap.get("jdbc.username")).isEqualTo("techcourse");
        assertThat(propertiesMap.get("jdbc.password")).isEqualTo("password");
    }
}
