package db;

import nextstep.jdbc.JdbcTemplate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTimeout;

public class QueryTest {

    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() throws SQLException {
        jdbcTemplate = new JdbcTemplate(DataSourceFactory.getInstance().createDataSource().getConnection());
    }

    @AfterEach
    void tearDown() {
        jdbcTemplate.close();
    }

    @Test
    void coding_as_a_hobby() {
        List<PortionResultDto> portions = new ArrayList<>();
        assertTimeout(Duration.ofMillis(300), () -> jdbcTemplate.executeQuery(
                "select hobby, round((count(*) / T.total) * 100, 1) AS portion\n" +
                        "from survey_results_public,\n" +
                        "(select count(*) as total from survey_results_public) as T\n" +
                        "group by hobby;", Collections.emptyMap(),
                resultSet -> new PortionResultDto(resultSet.getString("hobby"), resultSet.getDouble("portion")))
                .forEach(portions::add));
        assertThat(portions).hasSize(2);
        portions.forEach(p -> System.out.println(String.format("%s: %.1f%%", p.getValue(), p.getPortion())));
    }

    @Test
    void years_of_coding_prof_by_dev_type() {
        String query = "select dt.title as dev_type, avg_years\n" +
                "from (\n" +
                "select rdt.dev_type_id as dt_id\n" +
                ", round(avg(yearsCodingProf_int), 1) as avg_years\n" +
                "from  respondent_dev_type as rdt\n" +
                "inner join respondent as r\n" +
                "on r.id = rdt.respondent_id\n" +
                "where rdt.dev_type_id != (select id from dev_type where title = 'NA') and r.yearsCodingProf_int != 0\n" +
                "group by rdt.dev_type_id) as joined\n" +
                "inner join dev_type as dt\n" +
                "on joined.dt_id = dt.id;";
        List<AvgYearsCodingProfDevTypeDto> portions = new ArrayList<>();
        assertTimeout(Duration.ofMillis(3000), () -> portions.addAll(jdbcTemplate.executeQuery(
                query,
                Collections.emptyMap(),
                resultSet -> new AvgYearsCodingProfDevTypeDto(resultSet.getString("dev_type"),
                        resultSet.getDouble("avg_years")))));
        System.out.println(String.format("%-48s: Average time of years of coding professionally", "Dev type"));
        portions.sort(AvgYearsCodingProfDevTypeDto::compareTo);
        portions.forEach(p -> System.out.println(String.format("%-48s: %.1f hours", p.getDevType(), p.getAvgYears())));
    }

    void relate_devtype() {
        String query = "SELECT respondent, devType from survey_results_public";
        String insertQuery = "INSERT INTO respondent_dev_type(respondent_id, dev_type_id) VALUES(:respondentId, " +
                "(SELECT id FROM dev_type WHERE title = :devType));";
        jdbcTemplate.executeQuery(query, Collections.emptyMap(), rs -> {
            Map<String, Object> results = new HashMap<>();
            results.put("id", rs.getLong("respondent"));
            results.put("devtypes", Arrays.asList(rs.getString("devType").split(";")));
            return results;
        }).forEach(map -> {
            ((List<String>) map.get("devtypes"))
                    .forEach(dt -> {
                        Map<String, Object> params = new HashMap<>();
                        params.put("respondentId", map.get("id"));
                        params.put("devType", dt);
                        jdbcTemplate.executeUpdate(insertQuery, params);
                    });
        });
    }

    @Test
    void years_of_dev_exp_by_developer_type() {
        double avg = jdbcTemplate.executeQuery("select YearsCodingProf from survey_results_public where devType like '%Engineering manager%';",
                Collections.emptyMap(),
                rs -> rs.getString("YearsCodingProf")).stream()
                .filter(str -> !str.equals("NA"))
                .mapToInt(this::convertYearsOfCoding)
                .average().getAsDouble();
        System.out.println(avg);
    }

    private int convertYearsOfCoding(String str) {
        String firstToken = str.split(" ")[0];
        String firstNumberStr = firstToken.split("-")[0];
        if (firstToken.contains("-")) {
            return Integer.parseInt(firstNumberStr) + 1;
        }
        return Integer.parseInt(firstNumberStr);
    }
}
