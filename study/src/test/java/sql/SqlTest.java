package sql;

import nextstep.jdbc.JdbcTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class SqlTest {
    private JdbcTemplate jdbcTemplate;
    private static final String PROPERTY_FILE_PATH = "../slipp/src/main/resources/db.properties";

    @BeforeEach
    void setUp() {
        jdbcTemplate = new JdbcTemplate(DataSourceManager.getDataSource(PROPERTY_FILE_PATH));
    }

    @Test
    void Hobby_비율_확인() {
        String sql = "SELECT\n" +
                "Hobby,\n" +
                "ROUND(COUNT(Hobby)/(SELECT COUNT(*) FROM survey_results_public) * 100, 1) AS percentage\n" +
                "FROM survey_results_public\n" +
                "GROUP BY\n" +
                "Hobby";

        List<HobbyStatistic> hobbyStatistics = jdbcTemplate.query(sql,
                rs -> new HobbyStatistic(rs.getString(1),
                        Float.parseFloat(rs.getString(2))),
                pstmt -> {});

        for (HobbyStatistic hobbyStatistic : hobbyStatistics) {
            if (hobbyStatistic.getYesOrNo().equals("Yes")) {
                assertThat(hobbyStatistic.getPercentage()).isEqualTo(80.8f);
            } else {
                assertThat(hobbyStatistic.getPercentage()).isEqualTo(19.2f);
            }
        }
    }

    @Test
    void 평균_코딩_경험_데이터_가져오기() {
        String sql = "SELECT\n" +
                "DevType, ROUND(AVG(AvgYear), 1) AS AverageCodingExperience\n" +
                "FROM(\n" +
                "SELECT \n" +
                "SUBSTRING_INDEX(SUBSTRING_INDEX(survey_results_public.DevType, ';', numbers.n), ';', -1) DevType,\n" +
                "YearsCodingProf AS AvgYear\n" +
                "FROM(\n" +
                "SELECT 1 n UNION ALL\n" +
                "SELECT 2 UNION ALL\n" +
                "SELECT 3 UNION ALL\n" +
                "SELECT 4 UNION ALL\n" +
                "SELECT 5 UNION ALL\n" +
                "SELECT 6 UNION ALL\n" +
                "SELECT 7 UNION ALL\n" +
                "SELECT 8 UNION ALL\n" +
                "SELECT 9 UNION ALL\n" +
                "SELECT 10 UNION ALL\n" +
                "SELECT 11 UNION ALL\n" +
                "SELECT 12 UNION ALL\n" +
                "SELECT 13 UNION ALL\n" +
                "SELECT 14 UNION ALL\n" +
                "SELECT 15 UNION ALL\n" +
                "SELECT 16 UNION ALL\n" +
                "SELECT 17 UNION ALL\n" +
                "SELECT 18 UNION ALL\n" +
                "SELECT 19 UNION ALL\n" +
                "SELECT 20) numbers\n" +
                "INNER JOIN survey_results_public\n" +
                "ON CHAR_LENGTH(survey_results_public.DevType) - CHAR_LENGTH(REPLACE(survey_results_public.DevType, ';', '')) >= numbers.n - 1\n" +
                "WHERE survey_results_public.YearsCodingProf != 'NA') sub\n" +
                "WHERE NOT DevType IN ('NA', 'Student' ,'Product manager' ,'C-suite executive (CEO, CTO, etc.)')\n" +
                "GROUP BY\n" +
                "DevType\n" +
                "ORDER BY\n" +
                "AverageCodingExperience DESC";

        List<CodingExperience> codingExperiences = jdbcTemplate.query(sql,
                rs -> new CodingExperience(rs.getString(1),
                        Float.parseFloat(rs.getString(2))),
                pstmt -> {});

        Map<String, Float> codingExMapper = new HashMap<>();

        for (CodingExperience codingExperience : codingExperiences) {
            codingExMapper.put(codingExperience.getDevType(),
                    codingExperience.getAverageCodingExperience());
        }

        assertThat(codingExMapper.get("Engineering manager")).isEqualTo(10.2f);
        assertThat(codingExMapper.get("DevOps specialist")).isEqualTo(8.0f);
        assertThat(codingExMapper.get("Desktop or enterprise applications developer")).isEqualTo(7.7f);
        assertThat(codingExMapper.get("Embedded applications or devices developer")).isEqualTo(7.5f);
        assertThat(codingExMapper.get("Data or business analyst")).isEqualTo(7.2f);
        assertThat(codingExMapper.get("System administrator")).isEqualTo(7.0f);
        assertThat(codingExMapper.get("Database administrator")).isEqualTo(6.9f);
        assertThat(codingExMapper.get("Full-stack developer")).isEqualTo(6.3f);
        assertThat(codingExMapper.get("Back-end developer")).isEqualTo(6.2f);
        assertThat(codingExMapper.get("Educator or academic researcher")).isEqualTo(6.2f);
        assertThat(codingExMapper.get("Designer")).isEqualTo(6.0f);
        assertThat(codingExMapper.get("QA or test developer")).isEqualTo(5.8f);
        assertThat(codingExMapper.get("Front-end developer")).isEqualTo(5.5f);
        assertThat(codingExMapper.get("Data scientist or machine learning specialist")).isEqualTo(5.5f);
        assertThat(codingExMapper.get("Mobile developer")).isEqualTo(5.2f);
        assertThat(codingExMapper.get("Game or graphics developer")).isEqualTo(4.6f);
    }
}
