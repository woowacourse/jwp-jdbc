package sql;

import nextstep.jdbc.JdbcTemplate;
import org.junit.jupiter.api.Test;
import slipp.support.db.ConnectionManager;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class QueryTest {
    private JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());

    @Test
    void codingAsAHobby() throws SQLException {
        String query = "SELECT Hobby, ROUND(COUNT(*) / (SELECT COUNT(*) FROM survey_results_public) * 100, 1) as 'responses'\n" +
                "FROM survey_results_public\n" +
                "GROUP BY Hobby";

        List<CodingAsHobbyDto> codingAsHobbyDtos = jdbcTemplate.readForList(
                resultSet -> {
                    String hobby = resultSet.getString("hobby");
                    String percent = resultSet.getString("responses");
                    return new CodingAsHobbyDto(hobby, percent);
                },
                query,
                PreparedStatement::executeQuery
        );

        assertThat(codingAsHobbyDtos.get(0).getHobby()).isEqualTo("No");
        assertThat(codingAsHobbyDtos.get(0).getPercent()).isEqualTo(19.2);
        assertThat(codingAsHobbyDtos.get(1).getHobby()).isEqualTo("Yes");
        assertThat(codingAsHobbyDtos.get(1).getPercent()).isEqualTo(80.8);
    }

    @Test
    void yearsOfProfessionalCodingExperienceByDeveloperType() throws SQLException {
        String query = "SELECT SUBSTRING_INDEX(SUBSTRING_INDEX(DevType, ';', numbers.n), ';', -1) as dev_type,\n" +
                "       ROUND(AVG((substring_index(YearsCodingProf, '-', 1))), 1)          as years_of_prof\n" +
                "FROM (\n" +
                "         select 1 n union all\n" +
                "         select 2 union all\n" +
                "         select 3 union all\n" +
                "         select 4 union all\n" +
                "         select 5 union all\n" +
                "         select 6 union all\n" +
                "         select 7 union all\n" +
                "         select 8 union all\n" +
                "         select 9 union all\n" +
                "         select 10 union all\n" +
                "         select 11 union all\n" +
                "         select 12 union all\n" +
                "         select 13 union all\n" +
                "         select 14 union all\n" +
                "         select 15 union all\n" +
                "         select 16 union all\n" +
                "         select 17 union all\n" +
                "         select 18 union all\n" +
                "         select 19 union all\n" +
                "         select 20) numbers\n" +
                "         INNER JOIN survey_results_public\n" +
                "                    on CHAR_LENGTH (DevType)\n" +
                "                           - CHAR_LENGTH ( REPLACE ( DevType ,  ';' ,  '' ))>= numbers . n-1\n" +
                "WHERE DevType NOT IN ('NA') AND YearsCodingProf not in ('NA')\n" +
                "GROUP BY dev_type;";

        List<CodingExperienceByDeveloperTypeDto> codingExperienceByDeveloperTypeDtos = jdbcTemplate.readForList(
                resultSet -> {
                    String devType = resultSet.getString("dev_type");
                    String yearsOfProf = resultSet.getString("years_of_prof");

                    return new CodingExperienceByDeveloperTypeDto(devType, yearsOfProf);
                },
                query,
                PreparedStatement::executeQuery
        );

        assertThat(codingExperienceByDeveloperTypeDtos.get(0).getDeveloperType()).isEqualTo("Back-end developer");
        assertThat(codingExperienceByDeveloperTypeDtos.get(0).getYearsOfCodingExperience()).isEqualTo(6.2);
    }
}
