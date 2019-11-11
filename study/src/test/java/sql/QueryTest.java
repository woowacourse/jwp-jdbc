package sql;

import nextstep.jdbc.JdbcTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import slipp.support.db.ConnectionManager;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class QueryTest {

    private static final String TABLE_NAME = "survey_results_public";
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
    }

    @Test
    void coding_as_a_hobby() {
        String sql = String.format("SELECT hobby, (count(*) * 100) / (SELECT count(*) FROM %s) as ratio FROM %s GROUP BY hobby", TABLE_NAME, TABLE_NAME);

        List<QueryResult<Double>> queryResults = jdbcTemplate.query(sql, rs -> new QueryResult<>(
                rs.getString("hobby"), rs.getDouble("ratio")
        ));

        Map<String, Double> result = convertResultToMap(queryResults);
        assertThat(result.get("No")).isEqualTo(19.1776);
        assertThat(result.get("Yes")).isEqualTo(80.8224);
    }

    @Test
    void years_of_Professional_Coding_Experience_by_Developer_Type() {
        int typeSize = 16;
        List<String> developerTypes = Arrays.asList(
                "Engineering manager", "DevOps specialist", "Desktop or enterprise applications developer",
                "Embedded applications or devices developer", "Data or business analyst", "System administrator",
                "Database administrator", "Full-stack developer", "Back-end developer",
                "Educator or academic researcher", "Designer", "QA or test developer",
                "Front-end developer", "Data scientist or machine learning specialist",
                "Mobile developer", "Game or graphics developer"
        );

        List<Double> professionalYears = Arrays.asList(
                10.2, 8.0, 7.7, 7.5, 7.2, 7.0, 6.9, 6.3,
                6.2, 6.2, 6.0, 5.8, 5.5, 5.5, 5.2, 4.6
        );

        String normalizedTable = "DEV_DATA";
        String sql = String.format("SELECT Dev_type as devType, round(avg(year), 1) as result\n" +
                "FROM %s as d\n" +
                "WHERE Dev_type <> \"NA\"\n" +
                "group by Dev_type\n" +
                "order by result DESC;", normalizedTable);

        List<QueryResult<Double>> queryResults = jdbcTemplate.query(sql, rs -> new QueryResult<>(
                rs.getString("devType"), rs.getDouble("result")
        ));

        Map<String, Double> result = convertResultToMap(queryResults);

        for (int i = 0; i < typeSize; i++) {
            assertThat(result.get(developerTypes.get(i))).isEqualTo(professionalYears.get(i));
        }
    }

    private <T> Map<String, T> convertResultToMap(List<QueryResult<T>> queryResults) {
        return queryResults.stream()
                .collect(Collectors.toMap(QueryResult::getName, QueryResult::getResult));
    }
}
