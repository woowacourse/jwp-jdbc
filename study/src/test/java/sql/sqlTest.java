package sql;

import nextstep.jdbc.JdbcTemplate;
import nextstep.jdbc.RowMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import slipp.support.db.ConnectionManager;

import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTimeout;

class sqlTest {
    private static final String HOBBY_RATIO_SQL = "SELECT hobby as name,ROUND((COUNT(hobby)*100)/(SELECT COUNT(hobby) FROM survey_results_public),2) as ratio\n" +
            "FROM  survey_results_public\n" +
            "GROUP BY name";

    private static final String DEV_TYPE_YEARS_CODING_SQL = "SELECT dt.DevType, ROUND(AVG(YearsCodingProf), 1) AS Average\n" +
            "FROM (SELECT\n" +
            "SUBSTRING_INDEX(SUBSTRING_INDEX(DevType, ‘;’, numbers.digit), ‘;’, -1) DevType, YearsCodingProf\n" +
            "FROM survey_results_public\n" +
            "INNER JOIN\n" +
            "(SELECT 1 digit UNION ALL\n" +
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
            "ON CHAR_LENGTH ( DevType ) - CHAR_LENGTH ( REPLACE ( DevType ,  ‘;’ ,  ‘’ ))>= numbers.digit-1) as dt\n" +
            "WHERE dt.YearsCodingProf != ‘NA’\n" +
            "GROUP BY DevType\n" +
            "ORDER BY Average desc";

    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setup() {
        jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
    }

    @Test
    void hobby_duration() {
        assertTimeout(Duration.ofMillis(2000), (Executable) this::getHobbyRatio);
    }

    @Test
    void hobby_data_correct() {
        HobbyDto expectDto1 = new HobbyDto("No", 19.18);
        HobbyDto expectDto2 = new HobbyDto("Yes", 80.82);
        List<HobbyDto> hobbyDtos = getHobbyRatio();
        assertThat(hobbyDtos.contains(expectDto1)).isTrue();
        assertThat(hobbyDtos.contains(expectDto2)).isTrue();
    }

    List<HobbyDto> getHobbyRatio() {
        RowMapper results = (rs) ->
                new HobbyDto(rs.getString("name"), rs.getDouble("ratio"));
        return jdbcTemplate.query(HOBBY_RATIO_SQL, results);
    }

    @Test
    void devTypeYearsCoding_duration() {
        assertTimeout(Duration.ofMillis(10000), this::getDevTypeYearsCoding);
    }

    @Test
    void devTypeYearsCoding_data_correct() {
        List<DevTypeYearsCodingDto> devTypeYearsCodingDtos = getDevTypeYearsCoding();
        DevTypeYearsCodingDto expectDto1 = new DevTypeYearsCodingDto("Engineering manager", 10.2);
        assertThat(devTypeYearsCodingDtos.contains(expectDto1)).isTrue();
    }

    List<DevTypeYearsCodingDto> getDevTypeYearsCoding() {
        return jdbcTemplate.query(DEV_TYPE_YEARS_CODING_SQL, (rs) ->
                new DevTypeYearsCodingDto(
                        rs.getString("DevType"),
                        rs.getDouble("Average")));
    }
}
