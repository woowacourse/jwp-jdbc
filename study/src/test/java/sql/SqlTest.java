package sql;

import nextstep.jdbc.ConnectionManager;
import nextstep.jdbc.JdbcTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class SqlTest {
    @BeforeEach
    void setUp() {
        ConnectionManager.initialize("../slipp/src/main/resources/db.properties");
    }

    @DisplayName("hobby 비율 확인")
    @Test
    void selectRatio() {
        String query = "SELECT hobby, ROUND(100 * COUNT(*) / (SELECT COUNT(*) FROM survey_results_public),1) AS ratio " +
                "FROM survey_results_public " +
                "GROUP BY hobby";

        JdbcTemplate<HobbyResponse> jdbcTemplate = new JdbcTemplate<>();
        List<HobbyResponse> hobbyResponses = jdbcTemplate.select(query, resultSet ->
                new HobbyResponse(
                        resultSet.getString("hobby"),
                        resultSet.getString("ratio")));

        assertThat(hobbyResponses)
                .isEqualTo(Arrays.asList(
                        new HobbyResponse("No", "19.2"),
                        new HobbyResponse("Yes", "80.8")));
    }

    @DisplayName("코딩경험 평균기간 확인")
    @Test
    void selectYearsOfProfessionalCodingExperience() {
        String query = "SELECT\n" +
                "   SUBSTRING_INDEX(SUBSTRING_INDEX(survey_results_public.DevType,\n" +
                "                   ';',\n" +
                "                   numbers.n),\n" +
                "           ';',\n" +
                "           - 1) AS DeveloperType,\n" +
                "   ROUND(AVG(IF(survey_results_public.YearsCodingProf LIKE '30 or more years',\n" +
                "               30,\n" +
                "               SUBSTRING_INDEX(survey_results_public.YearsCodingProf,\n" +
                "                       '-',\n" +
                "                       1))),\n" +
                "           1) AS AvgOfYears\n" +
                "FROM\n" +
                "   (SELECT 1 n UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9 UNION ALL SELECT 10 UNION ALL SELECT 11 UNION ALL SELECT 12 UNION ALL SELECT 13 UNION ALL SELECT 14 UNION ALL SELECT 15 UNION ALL SELECT 16 UNION ALL SELECT 17) numbers\n" +
                "       INNER JOIN\n" +
                "   survey_results_public ON CHAR_LENGTH(survey_results_public.DevType) - CHAR_LENGTH(REPLACE(survey_results_public.DevType,\n" +
                "               ';',\n" +
                "               '')) >= numbers.n - 1\n" +
                "WHERE\n" +
                "   survey_results_public.YearsCodingProf NOT LIKE 'NA'\n" +
                "       AND survey_results_public.DevType NOT LIKE 'NA'\n" +
                "GROUP BY DeveloperType\n" +
                "ORDER BY AvgOfYears DESC;";

        JdbcTemplate<ExperienceResponse> jdbcTemplate = new JdbcTemplate<>();
        List<ExperienceResponse> experienceResponses = jdbcTemplate.select(query, resultSet ->
                new ExperienceResponse(
                        resultSet.getString("DeveloperType"),
                        resultSet.getString("AvgOfYears")));

        assertThat(experienceResponses).contains(
                new ExperienceResponse("Engineering manager", "10.2"),
                new ExperienceResponse("DevOps specialist", "8.0"),
                new ExperienceResponse("Desktop or enterprise applications developer", "7.7"),
                new ExperienceResponse("Embedded applications or devices developer", "7.5"),
                new ExperienceResponse("Data or business analyst", "7.2"),
                new ExperienceResponse("System administrator", "6.9"),
                new ExperienceResponse("Database administrator", "6.9"),
                new ExperienceResponse("Full-stack developer", "6.3"),
                new ExperienceResponse("Back-end developer", "6.2"),
                new ExperienceResponse("Educator or academic researcher", "6.2"),
                new ExperienceResponse("Designer", "6.0"),
                new ExperienceResponse("QA or test developer", "5.7"),
                new ExperienceResponse("Front-end developer", "5.5"),
                new ExperienceResponse("Data scientist or machine learning specialist", "5.5"),
                new ExperienceResponse("Mobile developer", "5.2"),
                new ExperienceResponse("Game or graphics developer", "4.6"));
    }
}
