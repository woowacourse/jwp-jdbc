package sql;

import nextstep.jdbc.DataSourceFactory;
import nextstep.jdbc.JdbcTemplate;
import nextstep.jdbc.PropertyRowMapper;
import nextstep.jdbc.RowMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class SqlTest {
    private JdbcTemplate jdbcTemplate;
    private RowMapper<Hobby> hobbyRowMapper = PropertyRowMapper.from(Hobby.class);
    private RowMapper<YearsOfProfessional> yearsRowMapper = PropertyRowMapper.from(YearsOfProfessional.class);

    @BeforeEach
    void setUp() throws SQLException {
        DataSourceFactory dataSourceFactory = new DataSourceFactory("src/test/java/sql/sample/db.properties");
        this.jdbcTemplate = new JdbcTemplate(dataSourceFactory.getDataSource());
    }

    @Test
    void CodingAsaHobby() {
        // given
        String sql = "SELECT a.Hobby, ROUND(count(a.Hobby)/t.total*100,1) AS PERCENTAGE\n" +
                "FROM survey_results_public as a\n" +
                "CROSS JOIN \n" +
                "(SELECT COUNT(1) AS total FROM survey_results_public) t\n" +
                "GROUP BY a.Hobby, t.total";

        List<Hobby> expected = List.of(new Hobby("Yes", 80.8), new Hobby("No", 19.2));

        // when
        List<Hobby> actual = jdbcTemplate.executeForList(sql, hobbyRowMapper);

        // then
        assertThat(actual).containsAll(expected);
    }

    @Test
    void YearsOfProfessionalCoding() {
        // given
        String sql = "SELECT t.devtype, ROUND(AVG(SUBSTRING_INDEX(t.YearsCodingProf, '-', 1)), 1) AS average\n" +
                "FROM (SELECT SUBSTRING_INDEX(SUBSTRING_INDEX(devtype, ';', n.digit + 1), ';', -1) devtype, YearsCodingProf\n" +
                "FROM survey_results_public\n" +
                "INNER JOIN\n" +
                "(SELECT 0 digit UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5 \n" +
                "UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9 UNION ALL SELECT 10) n\n" +
                "ON LENGTH(REPLACE(devtype, ';' , '')) <= LENGTH(devtype)-n.digit WHERE devtype != 'NA') as t\n" +
                "WHERE t.YearsCodingProf != 'NA'\n" +
                "GROUP BY t.devtype\n" +
                "ORDER BY average desc;";

        List<YearsOfProfessional> expected = List.of(
                new YearsOfProfessional("Engineering manager", 10.2),
                new YearsOfProfessional("DevOps specialist", 8.0),
                new YearsOfProfessional("Desktop or enterprise applications developer", 7.7),
                new YearsOfProfessional("Embedded applications or devices developer", 7.5));

        // when
        List<YearsOfProfessional> actual = jdbcTemplate.executeForList(sql, yearsRowMapper);

        // then
        assertThat(actual).containsAll(expected);
    }
}
