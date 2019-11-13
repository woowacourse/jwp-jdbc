package sql;

import nextstep.jdbc.JdbcTemplate;
import nextstep.jdbc.RowMapper;
import org.junit.jupiter.api.Test;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static sql.ConnectionManager.getDataSource;

class BigDataProcessTest {
    private final JdbcTemplate jdbcTemplate = new JdbcTemplate(getDataSource());

    @Test
    void CodingAsAHobbyTest() {
        final String sql =
                "SELECT " +
                "Hobby as isHobby, " +
                "COUNT(Hobby) AS count " +
                "FROM survey_results_public " +
                "GROUP BY Hobby;";
        final RowMapper<Entry> mapper = resultSet -> new Entry(
                resultSet.getString("isHobby"),
                resultSet.getInt("count")
        );
        final List<Entry> hobbyStats = jdbcTemplate.findItems(sql, mapper);
        final int sumOfCounts = hobbyStats.stream().mapToInt(item -> item.getValue().intValue()).sum();
        final DecimalFormat percentage = new DecimalFormat("0.#%");
        final Map<String, String> result = new HashMap<>();
        for (final Entry entry : hobbyStats) {
            final double ratio = entry.getValue().doubleValue() / (double) sumOfCounts;
            result.put(entry.getKey(), percentage.format(ratio));
        }

        assertThat(result.get("Yes")).isEqualTo("80.8%");
        assertThat(result.get("No")).isEqualTo("19.2%");
    }

    @Test
    void YearsOfProfessionalCodingExperiencesTest() {
        final String sql = "SELECT " +
                "DevType AS DeveloperType, " +
                "AVG(YearsProfessional) AS AverageYears " +
                "FROM YEARS_PROFESSIONAL_BY_DEV_TYPE " +
                "INNER JOIN DEV_TYPES " +
                "ON YEARS_PROFESSIONAL_BY_DEV_TYPE.DevTypeId = DEV_TYPES.id " +
                "GROUP BY DevType;";
        final RowMapper<Entry> mapper = resultSet -> new Entry(
                resultSet.getString("DeveloperType"),
                resultSet.getFloat("AverageYears")
        );
        final List<Entry> YearsOfProByDevType = jdbcTemplate.findItems(sql, mapper);
        final DecimalFormat decimalPointOne = new DecimalFormat("0.0");
        final Map<String, String> result = new HashMap<>();
        for (final Entry entry : YearsOfProByDevType) {
            result.put(entry.getKey(), decimalPointOne.format(entry.getValue()));
        }

        assertThat(result.get("Engineering manager")).isEqualTo("10.2");
        assertThat(result.get("DevOps specialist")).isEqualTo("8.0");
        assertThat(result.get("Desktop or enterprise applications developer")).isEqualTo("7.7");
        assertThat(result.get("Embedded applications or devices developer")).isEqualTo("7.5");
        assertThat(result.get("Data or business analyst")).isEqualTo("7.2");
        assertThat(result.get("System administrator")).isEqualTo("7.0");
        assertThat(result.get("Database administrator")).isEqualTo("6.9");
        assertThat(result.get("Full-stack developer")).isEqualTo("6.3");
        assertThat(result.get("Back-end developer")).isEqualTo("6.2");
        assertThat(result.get("Educator or academic researcher")).isEqualTo("6.2");
        assertThat(result.get("Designer")).isEqualTo("6.0");
        assertThat(result.get("QA or test developer")).isEqualTo("5.8");
        assertThat(result.get("Front-end developer")).isEqualTo("5.5");
        assertThat(result.get("Data scientist or machine learning specialist")).isEqualTo("5.5");
        assertThat(result.get("Mobile developer")).isEqualTo("5.2");
        assertThat(result.get("Game or graphics developer")).isEqualTo("4.6");
    }
}
