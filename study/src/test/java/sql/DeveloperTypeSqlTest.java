package sql;

import nextstep.jdbc.JdbcTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import slipp.support.db.ConnectionManager;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class DeveloperTypeSqlTest {
    private static final String TEMPORARY_NUMBERS_TABLE =
            "(SELECT 1 n\n" + "  UNION ALL SELECT 2 \n" + "  UNION ALL SELECT 3 \n" + "  UNION ALL SELECT 4 \n"
            + "  UNION ALL SELECT 5 \n" + "  UNION ALL SELECT 6 \n" + "  UNION ALL SELECT 7 \n" + "  UNION ALL SELECT 8 \n"
            + "  UNION ALL SELECT 9 \n" + "  UNION ALL SELECT 10 \n" + "  UNION ALL SELECT 11 \n" + "  UNION ALL SELECT 12 \n"
            + "  UNION ALL SELECT 13 \n" + "  UNION ALL SELECT 14 \n" + "  UNION ALL SELECT 15 \n" + "  UNION ALL SELECT 16 \n"
            + "  UNION ALL SELECT 17 \n" + "  UNION ALL SELECT 18 \n" + "  UNION ALL SELECT 19 \n" + "  UNION ALL SELECT 20\n"
            + ") numbers\n";
    private static final String SELECT_YEARS_OF_PROFESSIONAL_CODING_EXPERIENCE_SQL =
            "SELECT SUBSTRING_INDEX(SUBSTRING_INDEX(devtype, ';', n), ';', -1) dev_type,"
                    + " ROUND(AVG((SUBSTRING_INDEX(YearsCodingProf, '-', 1))), 1) years_of_prof\n"
                    + "FROM survey_results_public \n"
                    + "JOIN" + TEMPORARY_NUMBERS_TABLE
                    + "ON CHAR_LENGTH(devtype) - CHAR_LENGTH(REPLACE(devtype, ';', '')) >= n - 1\n"
                    + "WHERE devtype <> 'NA' AND YearsCodingProf <> 'NA'\n" + "GROUP BY dev_type\n"
                    + ";";

    private List<String> devTypes = Arrays.asList(
            "Engineering manager", "DevOps specialist", "Desktop or enterprise applications developer",
            "Embedded applications or devices developer", "Data or business analyst", "System administrator",
            "Database administrator", "Full-stack developer", "Back-end developer", "Educator or academic researcher",
            "Designer", "QA or test developer", "Front-end developer", "Data scientist or machine learning specialist",
            "Mobile developer", "Game or graphics developer"
    );
    private List<String> codingExperienceYears = Arrays.asList(
            "10.2", "8.0", "7.7", "7.5", "7.2", "7.0",
            "6.9", "6.3", "6.2", "6.2", "6.0",
            "5.8", "5.5", "5.5", "5.2", "4.6"
    );
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
    }


    @Test
    @DisplayName("코딩 경력 평균")
    void SELECTYearsOfProfessionalCodingExperienceSql() {
        List<YearsOfProfessionalCodingExperience> yearsOfProfessionalCodingExperience = jdbcTemplate.queryForList(
                SELECT_YEARS_OF_PROFESSIONAL_CODING_EXPERIENCE_SQL, resultSet ->
                new YearsOfProfessionalCodingExperience(
                        resultSet.getString("dev_type"),
                        resultSet.getString("years_of_prof")
                )
        );

        for (int i = 0; i < devTypes.size(); i++) {
            assertTrue(yearsOfProfessionalCodingExperience.contains(
                    new YearsOfProfessionalCodingExperience(devTypes.get(i), codingExperienceYears.get(i)))
            );
        }
    }
}
