package sql;

import nextstep.jdbc.JdbcTemplate;
import nextstep.jdbc.RowMapper;
import nextstep.jdbc.SimpleRowMapper;
import org.junit.jupiter.api.Test;
import slipp.support.db.ConnectionManager;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class LargeDataProcessingTest {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());

    @Test
    void Coding_as_a_Hobby() {
        RowMapper<CodingAsAHobbyStatistics> rowMapper = new SimpleRowMapper<>(CodingAsAHobbyStatistics.class);


        String sql = "SELECT Hobby, COUNT(*) * 100 / (SELECT COUNT(*) FROM survey_results_public) as rate " +
                "FROM survey_results_public " +
                "GROUP BY Hobby " +
                "ORDER BY Hobby DESC";

        List<CodingAsAHobbyStatistics> statistics = jdbcTemplate.queryForList(sql, rowMapper);
        assertThat(statistics.get(0).getRate()).isCloseTo(BigDecimal.valueOf(80), within(BigDecimal.ONE));
        assertThat(statistics.get(1).getRate()).isCloseTo(BigDecimal.valueOf(19), byLessThan(BigDecimal.ONE));
    }

    @Test
    void Years_of_Professional_Coding_Experience_by_Developer_Type() {
        RowMapper<ProfessionalCodingExperienceStatistics> rowMapper = new SimpleRowMapper<>(ProfessionalCodingExperienceStatistics.class);

        String sql = "SELECT t.devtype, ROUND(AVG(IF(t.YearsCodingProf = '30 or more years', 30, SUBSTRING_INDEX(t.YearsCodingProf, '-', 1))), 1) AS average" +
                " FROM (SELECT" +
                " SUBSTRING_INDEX(SUBSTRING_INDEX(devtype, ';', n.digit + 1), ';', -1) devtype, YearsCodingProf" +
                " FROM" +
                " survey_results_public" +
                " INNER JOIN" +
                " (SELECT 0 digit UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9 UNION ALL SELECT 10) n" +
                " ON LENGTH(REPLACE(devtype, ';' , '')) <= LENGTH(devtype)-n.digit WHERE devtype != 'NA') AS t" +
                " WHERE t.YearsCodingProf != 'NA'" +
                " GROUP BY t.devtype" +
                " ORDER BY average desc" +
                " LIMIT 5";

        List<ProfessionalCodingExperienceStatistics> statistics = jdbcTemplate.queryForList(sql, rowMapper);

        assertThat(statistics.get(0).getAverage()).isEqualTo(10.2);
        assertThat(statistics.get(1).getAverage()).isEqualTo(10.1);
        assertThat(statistics.get(2).getAverage()).isEqualTo(8.6);
        assertThat(statistics.get(3).getAverage()).isEqualTo(8.0);
        assertThat(statistics.get(4).getAverage()).isEqualTo(7.7);
    }
}