package sql;

import nextstep.jdbc.JdbcTemplate;
import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;

public class HobbyTest {
    private static final Logger log = LoggerFactory.getLogger(HobbyTest.class);

    DataSource dataSource;

    @BeforeEach
    void setUp() {
        Properties properties = new Properties();
        try {
            FileInputStream fileInputStream = new FileInputStream("src/test/resources/db.properties");

            properties.load(new BufferedInputStream(fileInputStream));
            BasicDataSource ds = new BasicDataSource();
            ds.setDriverClassName(properties.getProperty("jdbc.driverClass"));
            ds.setUrl(properties.getProperty("jdbc.url"));
            ds.setUsername(properties.getProperty("jdbc.username"));
            ds.setPassword(properties.getProperty("jdbc.password"));
            dataSource = ds;
        } catch (IOException e) {
            log.error("DB 설정파일을 찾을 수 없습니다.");
        }
    }

    @Test
    void codingAsHobby() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        String query = "select * from survey_results_public where Respondent = ?";

        Result result = jdbcTemplate.select(this::resultMapper, query, 1);

        assertThat(result.getCountry()).isEqualTo("Kenya");
        assertThat(result.getOpenSource()).isEqualTo("No");
        assertThat(result.getHobby()).isEqualTo("Yes");
        assertThat(result.getRespondent()).isEqualTo(1);
    }

    private Result resultMapper(ResultSet rs) throws SQLException {
        return new Result(rs.getInt("Respondent"),
                rs.getString("hobby"),
                rs.getString("openSource"),
                rs.getString("country"));
    }
}
