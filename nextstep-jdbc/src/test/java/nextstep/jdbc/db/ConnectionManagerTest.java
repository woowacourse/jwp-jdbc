package nextstep.jdbc.db;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;

class ConnectionManagerTest {
    private Properties properties;
    @BeforeEach
    void setup() throws Exception {
        properties = new Properties();
        properties.load(new BufferedInputStream(new FileInputStream("./src/main/resources/db.properties")));
        ConnectionManager.init(BasicDataSourceFactory.getDataSource(properties));
    }
    @Test
    void connectDb() {
        assertThat(properties.getProperty("jdbc.driverClass")).isEqualTo("com.mysql.cj.jdbc.Driver");
    }
}