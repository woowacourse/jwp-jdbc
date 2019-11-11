package sql;

import nextstep.jdbc.JdbcTemplate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import slipp.support.db.ConnectionManager;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class DataSetTest {
    public static final String TABLE_NAME = "survey_results_public";
    private JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());

    @Test
    @DisplayName("Coding as a Hobby")
    void hobbyTest() {
        String sql = String.format("SELECT hobby, ROUND(COUNT(*) * 100 / (SELECT COUNT(*) FROM %s), 1) as percentage FROM %s GROUP BY hobby",
                TABLE_NAME, TABLE_NAME);

        List<Data> result = jdbcTemplate.executeQuery(sql, new DataCreator("hobby", "percentage"));

        Map<String, Object> dataMap = dataListToMap(result);
        assertThat(dataMap.get("No")).isEqualTo(19.2);
        assertThat(dataMap.get("Yes")).isEqualTo(80.8);
    }

    @Test
    @DisplayName("Years of Professional Coding Experience by Developer Type")
    void yearTest() {
        String sql = "SELECT devtype, ROUND(AVG(YearsCodingProf), 1) YearsCodingProf " +
                "FROM (SELECT SUBSTRING_INDEX(SUBSTRING_INDEX(devtype, ';', n.digit+1), ';', -1) devtype, " +
                "       CONVERT(SUBSTRING_INDEX(YearsCodingProf, \" \", 1), UNSIGNED INTEGER) YearsCodingProf " +
                "     FROM survey_results_public " +
                "     INNER JOIN (SELECT 0 digit UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9 UNION ALL SELECT 10) n " +
                "     ON LENGTH(REPLACE(devtype, ';' , '')) <= LENGTH(devtype) - n.digit " +
                "     WHERE YearsCodingProf!=\"NA\") tmp " +
                "WHERE devtype!=\"NA\" " +
                "GROUP BY devtype;";

        List<Data> result = jdbcTemplate.executeQuery(sql, new DataCreator("devtype", "YearsCodingProf"));

        Map<String, Object> dataMap = dataListToMap(result);
        assertThat(dataMap.get("Engineering manager")).isEqualTo(10.2);
    }

    private Map<String, Object> dataListToMap(List<Data> result) {
        return result.stream()
                .collect(Collectors.toMap(Data::getName, Data::getValue));
    }
}