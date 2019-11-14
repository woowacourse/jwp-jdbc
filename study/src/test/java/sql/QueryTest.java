package sql;

import nextstep.jdbc.JdbcTemplate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import slipp.support.db.ConnectionManager;

import java.sql.SQLException;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTimeout;

public class QueryTest {
    public static final String TABLE_NAME = "survey_results_public";
    private JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());

    @Test
    @DisplayName("Coding as a Hobby")
    void hobbyTest() throws SQLException {
        String sql = "select hobby, ROUND(COUNT(*) / (SELECT COUNT(*) FROM survey_results_public) * 100, 1) as percentage  FROM survey_results_public  GROUP BY hobby;";

        Optional<List<Data>> result = jdbcTemplate.readForList(resultSet->new Data(resultSet.getString("hobby"), resultSet.getFloat("percentage")), sql);

        Map<String, Object> dataMap = dataListToMap(result.get());
        assertThat(dataMap.get("No")).isEqualTo(19.2f);
        assertThat(dataMap.get("Yes")).isEqualTo(80.8f);
    }

    @Test
    @DisplayName("Years of Professional Coding Experience by Developer Type")
    void yearTest() throws SQLException {
        String sql = "select DevType, round(avg(years),1) as avg\n" +
                "from DEV_TYPE\n" +
                "where DevType != 'NA' and years != 'NA'\n" +
                "group by DevType\n" +
                "order by avg desc;";

        assertTimeout(Duration.ofMillis(1800),()->{
            jdbcTemplate.readForList(resultSet -> new Data(resultSet.getString("DevType"), resultSet.getFloat("avg")), sql);
        });

        Optional<List<Data>> result = jdbcTemplate.readForList(resultSet -> new Data(resultSet.getString("DevType"), resultSet.getFloat("avg")), sql);
        Map<String, Object> dataMap = dataListToMap(result.get());

        assertThat(dataMap.get("Engineering manager")).isEqualTo(10.2f);
    }

    private Map<String, Object> dataListToMap(List<Data> result) {
        return result.stream()
                .collect(Collectors.toMap(Data::getName, Data::getValue));
    }
}
