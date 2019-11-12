package sql;

import nextstep.jdbc.ConnectionManager;
import nextstep.jdbc.JdbcTemplate;
import org.apache.commons.lang3.tuple.Pair;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTimeout;

public class QueryTest {
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        ConnectionManager.initialize();
        this.jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
    }

    @Test
    void initializeTest() {
        // do nothing
    }

    @Test
    void percentOfCodingAsHobbyTest() {
        String sql = "SELECT COUNT(*) * 100 / (SELECT COUNT(*) FROM survey_results_public) AS Percentage\n" +
                "FROM survey_results_public\n" +
                "WHERE Hobby = ?";
        double codingNoHobby = jdbcTemplate.selectObjectTemplate(sql,
                (rs) -> rs.getDouble("Percentage"), "No");
        assertThat(codingNoHobby).isCloseTo(19.2, Offset.offset(0.1));
        assertThat(100 - codingNoHobby).isCloseTo(80.8, Offset.offset(0.1));
    }

    @Test
    void percentageOfCodingAsHobbyTimeout() {
        assertTimeout(Duration.ofMillis(100), () -> {
            String sql = "SELECT COUNT(*) * 100 / (SELECT COUNT(*) FROM survey_results_public) AS Percentage\n" +
                    "FROM survey_results_public\n" +
                    "WHERE Hobby = ?";
            double codingNoHobby = jdbcTemplate.selectObjectTemplate(sql,
                    (rs) -> rs.getDouble("Percentage"), "No");
        });
    }

    @Test
    void yearsOfProfessionalCoding() {
        String sql = "SELECT d.DevType AS DevType, ROUND(AVG(s.YearsCodingProf), 1) AS YearsCodingProf\n" +
                "FROM survey_results_public as s, TMP_DEV_TYPE as d \n" +
                "WHERE s.YearsCodingProf != 'NA' AND s.DevType LIKE CONCAT('%', d.DevType, '%')\n" +
                "GROUP BY d.DevType\n" +
                "ORDER BY YearsCodingProf DESC;";
        List<Pair<String, Double>> yearsOfCodingProf = jdbcTemplate.selectTemplate(sql,
                (rs) -> Pair.of(rs.getString("DevType"), rs.getDouble("YearsCodingProf")));
        List<Pair<String, Double>> answer = Arrays.asList(
                Pair.of("Engineering manager", 10.2),
                Pair.of("DevOps specialist", 8.0),
                Pair.of("Desktop or enterprise applications developer", 7.7),
                Pair.of("Embedded applications or devices developer", 7.5),
                Pair.of("Data or business analyst", 7.2),
                Pair.of("System administrator", 7.0),
                Pair.of("Database administrator", 6.9),
                Pair.of("Full-stack developer", 6.3),
                Pair.of("Back-end developer", 6.2),
                Pair.of("Educator or academic researcher", 6.2),
                Pair.of("Designer", 6.0),
                Pair.of("QA or test developer", 5.8),
                Pair.of("Front-end developer", 5.5),
                Pair.of("Data scientist or machine learning specialist", 5.5),
                Pair.of("Mobile developer", 5.2),
                Pair.of("Game or graphics developer", 4.6)
        );

        assertThat(yearsOfCodingProf).isEqualTo(answer);
    }
}
