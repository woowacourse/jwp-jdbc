package sql;

import nextstep.jdbc.JdbcTemplate;
import nextstep.jdbc.RowMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import slipp.support.db.ConnectionManager;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTimeout;

public class ExampleTest {
    private static final String FILE_PATH = "src/test/java/sql/db.properties";
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void setup() {
        jdbcTemplate = JdbcTemplate.getInstance(ConnectionManager.getDataSource(FILE_PATH));
    }

    @Test
    void Coding_as_a_hobby_테스트() {
        String sql = "select hobby, round(count(*) / (select count(*) from survey_results_public) * 100, 2) as ratio\n" +
                "from survey_results_public\n" +
                "group by hobby;";

        RowMapper results = (resultSet) -> {
            String hobby = resultSet.getString("hobby");
            String ratio = resultSet.getString("ratio");

            return new HobbyDto(hobby, ratio);
        };

        List<HobbyDto> hobbyDtos = jdbcTemplate.selectAll(sql, results);

        Map<String, Double> hobbyResults = new HashMap<>();
        for (HobbyDto hobbyDto : hobbyDtos) {
            hobbyResults.put(hobbyDto.getHobby(), hobbyDto.getRatio());
        }

        assertThat(hobbyResults.get("No")).isEqualTo(19.18);
        assertThat(hobbyResults.get("Yes")).isEqualTo(80.82);

        assertTimeout(Duration.ofMillis(1500), () -> jdbcTemplate.selectAll(sql, results));
    }

    @Test
    void Years_of_Professional_Coding_Experience_by_devType_테스트() {
        String sql = "select round(avg(YearsCodingProf),2) as average, devType\n" +
                "from (select SUBSTRING_INDEX (SUBSTRING_INDEX(survey_results_public.devType,';',numbers.n),';',-1) devType, Respondent, YearsCodingProf\n" +
                "from \n" +
                "   (select  1 n union  all  select 2  \n" +
                "    union  all  select  3  union  all select 4 \n" +
                "    union  all  select  5  union  all  select  6\n" +
                "    union  all  select  7  union  all  select  8 \n" +
                "    union  all  select  9 union  all  select  10) numbers INNER  JOIN survey_results_public\n" +
                "    on CHAR_LENGTH ( survey_results_public.devType) \n" +
                "      - CHAR_LENGTH ( REPLACE ( survey_results_public.devType,  ';' ,  '' ))>= numbers.n-1) as sub\n" +
                "      group by devType;\n";

        RowMapper result = (resultSet) -> {
            String devType = resultSet.getString("devType");
            double average = resultSet.getDouble("average");

            return new YpceOfDevTypeDto(devType, average);
        };

        List<YpceOfDevTypeDto> results = jdbcTemplate.selectAll(sql, result);

        Map<String, Double> ypceOfDevTypeMap = new HashMap<>();
        for (YpceOfDevTypeDto ypceOfDevTypeDto : results) {
            ypceOfDevTypeMap.put(ypceOfDevTypeDto.getDevType(), ypceOfDevTypeDto.getAverageOfYears());
        }

        assertThat(ypceOfDevTypeMap.get("Back-end developer")).isEqualTo(5.46);
        assertTimeout(Duration.ofMillis(6000), () -> jdbcTemplate.selectAll(sql, result));
    }
}
