package sql;

import nextstep.jdbc.JdbcTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTimeout;

public class SqlTest {
    private JdbcTemplate jdbcTemplate;
    private static final String PROPERTY_FILE_PATH = "../slipp/src/main/resources/db.properties";

    @BeforeEach
    void setUp() {
        jdbcTemplate = new JdbcTemplate(DataSourceManager.getDataSource(PROPERTY_FILE_PATH));
    }

    @Test
    void Hobby_비율_확인() {
        String sql =
                "select " +
                        "Hobby, " +
                        "ROUND(count(Hobby)/(select count(*) from survey_results_public)*100, 1) as percentage " +
                        "from survey_results_public " +
                        "group by Hobby";

        List<HobbyStatistic> hobbyStatistics = jdbcTemplate.query(sql,
                rs -> new HobbyStatistic(rs.getString(1),
                        Float.parseFloat(rs.getString(2))),
                pstmt -> {
                });

        for (HobbyStatistic hobbyStatistic : hobbyStatistics) {
            if (hobbyStatistic.getYesOrNo().equals("Yes")) {
                assertThat(hobbyStatistic.getPercentage()).isEqualTo(80.8f);
            } else {
                assertThat(hobbyStatistic.getPercentage()).isEqualTo(19.2f);
            }
        }
    }

    @Test
    void Hobby_시간_테스트() {
        String sql =
                "select " +
                        "Hobby, " +
                        "ROUND(count(Hobby)/(select count(*) from survey_results_public)*100, 1) as percentage " +
                        "from survey_results_public " +
                        "group by Hobby";

        assertTimeout(Duration.ofMillis(100), () -> jdbcTemplate.query(sql,
                rs -> new HobbyStatistic(rs.getString(1),
                        Float.parseFloat(rs.getString(2))),
                pstmt -> {
                }));
    }


}
