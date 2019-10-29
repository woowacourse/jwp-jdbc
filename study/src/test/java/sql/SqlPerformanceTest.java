package sql;

import nextstep.jdbc.JdbcTemplate;
import nextstep.jdbc.mapper.ListMapper;
import org.junit.jupiter.api.Test;
import slipp.support.db.ConnectionManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class SqlPerformanceTest {
    @Test
    void test1_all() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getConnection());

        String query = "SELECT \n" +
                "    hobby,\n" +
                "    round((COUNT(hobby) / (SELECT \n" +
                "            COUNT(*)\n" +
                "        FROM\n" +
                "            survey_results_public) * 100), 1) AS hobby_ratio\n" +
                "FROM\n" +
                "    survey_results_public\n" +
                "GROUP BY hobby";

        List<Hobby> hobbys = jdbcTemplate.executeQuery(query,
                pst -> {
                }, new ListMapper<>(rs ->
                        new Hobby(rs.getString("hobby"), rs.getDouble("hobby_ratio"))
                ));

        assertThat(getHobby(hobbys, "Yes").getRatio()).isEqualTo(80.8);
        assertThat(getHobby(hobbys, "No").getRatio()).isEqualTo(19.2);
    }

    @Test
    void test1_prof() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getConnection());

        String query = "SELECT \n" +
                "    hobby,\n" +
                "    round((COUNT(hobby) / (SELECT \n" +
                "            COUNT(*)\n" +
                "        FROM\n" +
                "            survey_results_public \n" +
                "\t\twhere Student != 'NA') * 100), 0) AS hobby_ratio\n" +
                "FROM\n" +
                "    survey_results_public\n" +
                "where Student != 'NA'\n" +
                "GROUP BY hobby";

        List<Hobby> hobbys = jdbcTemplate.executeQuery(query,
                pst -> {
                }, new ListMapper<>(rs ->
                        new Hobby(rs.getString("hobby"), rs.getDouble("hobby_ratio"))
                ));

        assertThat(getHobby(hobbys, "Yes").getRatio()).isEqualTo(81.0);
        assertThat(getHobby(hobbys, "No").getRatio()).isEqualTo(19.0);
    }

    private Hobby getHobby(List<Hobby> hobbys, String status) {
        for (Hobby hobby : hobbys) {
            if (hobby.getStatus().equals(status)) {
                return hobby;
            }
        }
        return null;
    }

    @Test
    void test2_all() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getConnection());

        String query = "SELECT \n" +
                "    devType,\n" +
                "    ROUND(AVG(YearsCodingProf_NUM), 1) AS avg_yearsConfigProf\n" +
                "FROM\n" +
                "    DEVTYPES\n" +
                "GROUP BY devtype\n" +
                "ORDER BY avg_yearsConfigProf DESC";

        List<Developer> developers = jdbcTemplate.executeQuery(query,
                pst -> {
                }, new ListMapper<>(rs ->
                        new Developer(rs.getString("devType"), rs.getDouble("avg_yearsConfigProf"))
                ));

        assertThat(getDeveloper(developers, "Engineering manager").getYears()).isEqualTo(10.2);
        assertThat(getDeveloper(developers, "System administrator").getYears()).isEqualTo(7.0);
        assertThat(getDeveloper(developers, "Back-end developer").getYears()).isEqualTo(6.2);
        assertThat(getDeveloper(developers, "Front-end developer").getYears()).isEqualTo(5.5);
        assertThat(getDeveloper(developers, "Game or graphics developer").getYears()).isEqualTo(4.6);
    }

    private Developer getDeveloper(List<Developer> developers, String type) {
        for (Developer developer : developers) {
            if (developer.getType().equals(type)) {
                return developer;
            }
        }
        return null;
    }
}
