package sql;

import nextstep.jdbc.JdbcTemplate;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import slipp.support.db.ConnectionManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class DataBasePracticeTest {
    private static final Logger logger = LoggerFactory.getLogger(DataBasePracticeTest.class);
    JdbcTemplate jdbcTemplate = JdbcTemplate.getInstance(ConnectionManager.getDataSource());

    @Test
    void codingAsAHobby() {
        String sql = "SELECT Hobby, round((count(*) * 100)/(SELECT count(*) FROM survey_results_public), 1) AS average " +
                "FROM survey_results_public " +
                "GROUP BY Hobby;";
        List<CodingAsAHobbyDto> query = jdbcTemplate.query(sql, (rs) ->
                new CodingAsAHobbyDto(
                        rs.getString("Hobby"),
                        rs.getDouble("average")));

        Map<String, Double> codingAsHobby = new HashMap<>();
        query.forEach(codingAsAHobbyDto ->
                codingAsHobby.put(codingAsAHobbyDto.getHobby(), codingAsAHobbyDto.getAverage()));

        assertThat(codingAsHobby.get("Yes")).isEqualTo(80.8);
        assertThat(codingAsHobby.get("No")).isEqualTo(19.2);
    }

    @Test
    void years() {
        String sql = "SELECT\n" +
                "dt.name AS DevType,\n" +
                "round(avg(srp.YearsCodingProf), 1) AS average\n" +
                "FROM\n" +
                "DEV_TYPE dt\n" +
                "INNER JOIN survey_results_public AS srp\n" +
                "ON srp.DevType like concat('%', dt.name, '%')\n" +
                "WHERE srp.YearsCodingProf != 'NA'\n" +
                "GROUP BY dt.name;";
        List<YearsCodingProfDevTypeDto> query = jdbcTemplate.query(sql, (rs) ->
                new YearsCodingProfDevTypeDto(
                        rs.getString("DevType"),
                        rs.getDouble("average")));

        Map<String, Double> yearsCodingProf = new HashMap<>();
        query.forEach(yearsCodingProfDevTypeDto ->
                yearsCodingProf.put(yearsCodingProfDevTypeDto.getDevType(), yearsCodingProfDevTypeDto.getAverage()));

        assertThat(yearsCodingProf.get("Engineering manager")).isEqualTo(10.2);
    }
}
