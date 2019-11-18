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
        jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource(FILE_PATH));
    }

    @Test
    void Coding_as_a_hobby_테스트() {
//        String index = "create index hobby_index on survey_results_public (hobby);";
//        jdbcTemplate.execute(index);

        String sql = "select hobby, round(count(*) / (select count(*) from survey_results_public) * 100, 2) as ratio\n" +
                "from survey_results_public\n" +
                "group by hobby;";

        RowMapper results = (resultSet) -> {
            String hobby = resultSet.getString("hobby");
            String ratio = resultSet.getString("ratio");

            return new HobbyDto(hobby, ratio);
        };

        List<HobbyDto> hobbyDtos = jdbcTemplate.query(sql, results);

        Map<String, Double> hobbyResults = new HashMap<>();
        for (HobbyDto hobbyDto : hobbyDtos) {
            hobbyResults.put(hobbyDto.getHobby(), hobbyDto.getRatio());
        }

        assertThat(hobbyResults.get("No")).isEqualTo(19.18);
        assertThat(hobbyResults.get("Yes")).isEqualTo(80.82);

        assertTimeout(Duration.ofMillis(100), () -> jdbcTemplate.query(sql, results));
    }

    @Test
    void Years_of_Professional_Coding_Experience_by_devType_테스트() {
        String sql = "\n" +
                "select devType, round(avg(YearsCodingProf),1) as avg\n" +
                "from (select SUBSTRING_INDEX (SUBSTRING_INDEX(survey_results_public.devType,';',numbers.n),';',-1) devType, Respondent, YearsCodingProf\n" +
                "from \n" +
                "   (select  1 n union  all  select 2  \n" +
                "    union  all  select  3  union  all select 4 \n" +
                "    union  all  select  5  union  all  select  6\n" +
                "    union  all  select  7  union  all  select  8 \n" +
                "    union  all  select  9 union  all  select  10\n" +
                "    union  all  select  11  union  all select 12 \n" +
                "    union  all  select  13  union  all  select  14\n" +
                "    union  all  select  15  union  all  select  16 \n" +
                "    union  all  select  17 union  all  select  18\n" +
                "    union  all  select  19) numbers INNER  JOIN survey_results_public\n" +
                "    on CHAR_LENGTH ( survey_results_public.devType) \n" +
                "      - CHAR_LENGTH ( REPLACE ( survey_results_public.devType,  ';' ,  '' ))>= numbers.n-1) as sub\n" +
                "where devType not like 'NA%' AND YearsCodingProf != 'NA'\n" +
                "group by devType;";

        RowMapper result = (resultSet) -> {
            String devType = resultSet.getString("devType");
            double average = resultSet.getDouble("avg");

            return new YpceOfDevTypeDto(devType, average);
        };

        List<YpceOfDevTypeDto> results = jdbcTemplate.query(sql, result);

        Map<String, Double> ypceOfDevTypeMap = new HashMap<>();
        for (YpceOfDevTypeDto ypceOfDevTypeDto : results) {
            ypceOfDevTypeMap.put(ypceOfDevTypeDto.getDevType(), ypceOfDevTypeDto.getAverageOfYears());
        }

        assertThat(ypceOfDevTypeMap.get("Back-end developer")).isEqualTo(6.2);
    }
}
