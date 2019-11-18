package sql;

import nextstep.jdbc.DBTemplate;
import nextstep.jdbc.JdbcTemplate;
import nextstep.jdbc.QueryFailedException;
import nextstep.jdbc.RowMapper;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import slipp.support.db.ConnectionManager;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTimeout;

public class QueryTest {
    private static final Logger logger = LoggerFactory.getLogger(QueryTest.class);

    private static final int MAX_DURATION = 100;
    private static final double HOBBYIST_ANSWER = 80.8;
    private static final Map<String, Double> CODING_EXP_PER_DEV_TYPE_ANSWER = new HashMap<>() {{
        put("Engineering manager", 10.2);
        put("DevOps specialist", 8.0);
        put("Desktop or enterprise applications developer", 7.7);
        put("Embedded applications or devices developer", 7.5);
        put("Data or business analyst", 7.2);
        put("System administrator", 7.0);
        put("Database administrator", 6.9);
        put("Full-stack developer", 6.3);
        put("Back-end developer", 6.2);
        put("Educator or academic researcher", 6.2);
        put("Designer", 6.0);
        put("QA or test developer", 5.8);
        put("Front-end developer", 5.5);
        put("Data scientist or machine learning specialist", 5.5);
        put("Mobile developer", 5.2);
        put("Game or graphics developer", 4.6);
        put("C-suite executive (CEO, CTO, etc.)", 10.1);
        put("Marketing or sales professional", 7.2);
        put("Product manager", 8.8);
        put("Student", 1.2);
    }};

    private final DBTemplate template = new JdbcTemplate(ConnectionManager.getDataSource());

    @Test
    void codingAsAHobbyTest() {
        final RowMapper<Double> f = rs -> rs.getDouble(1);
        final String resultQuery =
                "SELECT COUNT(*) * 100.0 / (SELECT COUNT(*) FROM survey_results_public) " +
                "FROM survey_results_public WHERE Hobby = 'Yes';";
        final String buildIndexQuery = "CREATE INDEX for_fun ON survey_results_public (Hobby);";
        try {
            this.template.execute(buildIndexQuery);
        } catch (QueryFailedException e) {
            logger.error(e.getMessage());
        }
        this.template.read(f, resultQuery).ifPresent(r ->
            assertThat(r).isCloseTo(HOBBYIST_ANSWER, Offset.offset(1.0))
        );
        assertTimeout(Duration.ofMillis(MAX_DURATION), () -> {
            this.template.read(f, resultQuery);
        });
    }

    @Test
    void yearsOfProfessionalCodingExperienceByDeveloperTypeTest() {
        final RowMapper<DevTypeCodingExp> f =
                rs -> new DevTypeCodingExp(rs.getString("devType"), rs.getDouble("avgExp"));
        final String resultQuery =
                "SELECT devType, ROUND(AVG(YearsCodingProf), 1) AS avgExp FROM (" +
                "  SELECT Respondent, SUBSTRING_INDEX(SUBSTRING_INDEX(survey_results_public.devType, ';', numbers.n), ';', -1) devType, YearsCodingProf FROM (" +
                "    SELECT 1 n UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9 UNION ALL SELECT 10 UNION ALL SELECT 11 UNION ALL SELECT 12 UNION ALL SELECT 13 UNION ALL SELECT 14 UNION ALL SELECT 15 UNION ALL SELECT 16 UNION ALL SELECT 17 UNION ALL SELECT 18 UNION ALL SELECT 19 UNION ALL SELECT 20" +
                "  ) numbers JOIN survey_results_public ON CHAR_LENGTH(survey_results_public.devType) - CHAR_LENGTH(REPLACE(survey_results_public.devType, ';', '')) >= numbers.n - 1) AS tmp " +
                "WHERE devType NOT LIKE 'NA%' AND YearsCodingProf != 'NA' GROUP BY devType;";
        this.template.readAll(f, resultQuery).forEach(x -> {
            System.out.println(x.devType + ": " + x.avgExp);
            assertThat(x.avgExp).isEqualTo(CODING_EXP_PER_DEV_TYPE_ANSWER.get(x.devType));
        });
        assertTimeout(Duration.ofMillis(MAX_DURATION), () -> {
            this.template.readAll(f, resultQuery);
        });
    }
}

class DevTypeCodingExp {
    final String devType;
    final double avgExp;

    DevTypeCodingExp(String devType, double avgExp) {
        this.devType = devType;
        this.avgExp = avgExp;
    }
}