package nextstep.jdbc;

import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

class DataSourceFactoryTest {

    @Test
    void DataSource_생성_테스트() throws SQLException {
        // given
        final DataSourceFactory factory = new DataSourceFactory("src/test/java/nextstep/jdbc/support/db.properties");
        final DataSource dataSource = factory.getDataSource();

        // when
        assertThat(dataSource.getConnection()).isNotNull();
    }
}