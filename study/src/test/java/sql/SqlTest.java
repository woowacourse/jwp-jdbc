package sql;

import nextstep.jdbc.JdbcTemplate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import slipp.support.db.ConnectionManager;
import sql.dto.DevTypeDto;
import sql.dto.HobbyDto;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class SqlTest {
    private static final String TEST_DATABASE_PATH = "src/main/resources/testdb.properties";
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource(TEST_DATABASE_PATH));
    }

    @Test
    @DisplayName("첫번째 sql Hobby 테스트")
    void codingAsAHobbyTest() {
        List<HobbyDto> hobbyDtos = jdbcTemplate.query(queryForHobby(), this::getHobbyValue);
        assertThat(hobbyDtos.get(0).toString()).isEqualTo("No: 19.1776");
        assertThat(hobbyDtos.get(1).toString()).isEqualTo("Yes: 80.8224");
    }

    private HobbyDto getHobbyValue(ResultSet rs) throws SQLException {
        return new HobbyDto(rs.getString("hobby"), rs.getString("percentage"));
    }

    private String queryForHobby() {
        return "select Hobby, (count(Hobby) * 100) / " +
                "(select count(*) " +
                "from survey_results_public) " +
                "as percentage " +
                "from survey_results_public " +
                "group by Hobby;";
    }

    @Test
    @DisplayName("두번째 sql years Of Prof 테스트")
    void yearsOfProfByDevTypeTest() {
        List<DevTypeDto> devTypeDtos = jdbcTemplate.query(queryForYearsOfProf(), this::getDevTypeValue);
        assertThat(devTypeDtos.get(0).toString()).isEqualTo("Engineering manager: 10.2");
        assertThat(devTypeDtos.get(2).toString()).isEqualTo("Product manager: 8.8");
        assertThat(devTypeDtos.get(3).toString()).isEqualTo("DevOps specialist: 8.0");
        assertThat(devTypeDtos.get(13).toString()).isEqualTo("Designer: 6.0");
    }

    private DevTypeDto getDevTypeValue(ResultSet rs) throws SQLException {
        return new DevTypeDto(rs.getString("DevType"), rs.getString("AverageOfYears"));
    }

    private String queryForYearsOfProf() {
        return "select s.DevType, " +
                "round(avg(if(s.YearsCodingProf = '30 or more years', 30, " +
                "substring_index(s.YearsCodingProf, '-', 1))), 1) as AverageOfYears " +
                "from " +
                "(select substring_index(substring_index(DevType, ';', n), ';', -1) DevType, YearsCodingProf " +
                "from " +
                "(select 1 n union select 2 union select 3 union select 4 " +
                "union select 5 union select 6 union select 7 " +
                "union select 8 union select 9 union select 10 " +
                "union select 11 union select 12 union select 13 " +
                "union select 14 union select 15 union select 16 " +
                "union select 17 union select 18 union select 19) as numbers " +
                "inner join survey_results_public " +
                "on char_length(DevType) - char_length(replace(DevType, ';', '')) >= numbers.n - 1 " +
                "where DevType != 'NA') as s " +
                "where YearsCodingProf != 'NA' " +
                "group by s.DevType " +
                "order by AverageOfYears desc ";
    }

    @AfterEach
    void tearDown() {
        jdbcTemplate = null;
    }
}
