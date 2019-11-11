package database;

import nextstep.jdbc.JdbcTemplate;
import org.junit.jupiter.api.Test;
import slipp.support.db.ConnectionManager;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class DataBaseQueryTest {

    private final JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource("src/test/java/database/db.properties"));

    @Test
    void codingAsAHobbyTest() {
        String query = "SELECT round((count(*) * 100 / (SELECT count(*) FROM survey_results_public)), 1) AS ratio" +
                " FROM survey_results_public" +
                " WHERE hobby = 'Yes'";

        Double result = jdbcTemplate.querySingle(query,pstmt->{},rs -> rs.getDouble("ratio"));
        assertThat(result).isEqualTo(80.8);
    }

    @Test
    void yearsOfProfessionalCoding() {
        String query = "SELECT t.devtype, round(avg(if(t.YearsCodingProf = '30 or more years', 30, substring_index(t.YearsCodingProf, '-', 1))), 1) AS average" +
                " from (SELECT" +
                " SUBSTRING_INDEX(SUBSTRING_INDEX(devtype, ';', n.digit + 1), ';', -1) devtype, YearsCodingProf" +
                " FROM" +
                " survey_results_public" +
                " INNER JOIN" +
                " (SELECT 0 digit UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9 UNION ALL SELECT 10) n" +
                " ON LENGTH(REPLACE(devtype, ';' , '')) <= LENGTH(devtype)-n.digit WHERE devtype != 'NA') as t" +
                " WHERE t.YearsCodingProf != 'NA'" +
                " group by t.devtype" +
                " order by average desc";

        List<StatisticsDto> results = jdbcTemplate.queryMultiple(query, pstmt -> {}, rs -> new StatisticsDto(rs.getString("devType"), rs.getDouble("average")));
        Map<String, Double> adjustedResults = tomap(results);

        assertThat(adjustedResults.get("Engineering manager")).isEqualTo(10.2);
        assertThat(adjustedResults.get("Embedded applications or devices developer")).isEqualTo(7.5);
    }

    private Map<String, Double> tomap(List<StatisticsDto> results) {
        return results.stream()
                .collect(Collectors.toMap(StatisticsDto::getDevType, StatisticsDto::getAverage));
    }
}
