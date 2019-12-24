package nextstep.jdbc;

import org.junit.jupiter.api.Test;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class ConnectionManagerTest {

    @Test
    void create() {
        assertDoesNotThrow(() -> {
            DataSource ds = ConnectionManager.getDataSource();
            assertThat(ds).isNotNull();
            ds.getConnection();
        });
    }
}
