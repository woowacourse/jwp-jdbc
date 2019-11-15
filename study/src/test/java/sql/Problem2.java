package sql;

import com.google.common.collect.Maps;
import nextstep.jdbc.JdbcTemplate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class Problem2 {
    private JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());

    @Test
    @DisplayName("Years of Professional Coding Experience by Developer Type")
    void essentialProblem() {
        String query =
                "SELECT DevType, ROUND(AVG(yearsCodingProf), 1) yearsCodingProf\n" +
                        "FROM (\n" +
                        "select SUBSTRING_INDEX(SUBSTRING_INDEX(survey_results_public.devType, ';', numbers.n), ';', -1) devType, yearsCodingProf\n" +
                        "from numbers INNER JOIN survey_results_public\n" +
                        "on CHAR_LENGTH(survey_results_public.devType)-CHAR_LENGTH(REPLACE(survey_results_public.devType, ';', '')) >= numbers.n-1\n" +
                        "where yearsCodingProf != 'NA') b\n" +
                        "GROUP BY b.DevType\n" +
                        "ORDER BY yearsCodingProf DESC";

        List<DevType> devTypes = jdbcTemplate.executeQuery(query, Maps.newHashMap(),
                rs -> new DevType(rs.getString("devType"), rs.getDouble("yearsCodingProf")));

        List<DevType> expected = Arrays.asList(
                new DevType("Engineering manager", 10.2),
                new DevType("C-suite executive (CEO, CTO, etc.)", 10.1),
                new DevType("Product manager", 8.8),
                new DevType("DevOps specialist", 8.0),
                new DevType("Desktop or enterprise applications developer", 7.7),
                new DevType("Embedded applications or devices developer", 7.5),
                new DevType("Marketing or sales professional", 7.2),
                new DevType("NA", 7.2),
                new DevType("Data or business analyst", 7.2),
                new DevType("System administrator", 7.0),
                new DevType("Database administrator", 6.9),
                new DevType("Full-stack developer", 6.3),
                new DevType("Back-end developer", 6.2),
                new DevType("Educator or academic researcher", 6.2),
                new DevType("Designer", 6.0),
                new DevType("QA or test developer", 5.8),
                new DevType("Data scientist or machine learning specialist", 5.5),
                new DevType("Front-end developer", 5.5),
                new DevType("Mobile developer", 5.2),
                new DevType("Game or graphics developer", 4.6),
                new DevType("Student", 1.2));

        assertThat(devTypes).isEqualTo(expected);

    }
}
