package sql;

import com.google.common.collect.Maps;
import nextstep.jdbc.ConnectionManager;
import nextstep.jdbc.DataSourcePropertiesBinder;
import nextstep.jdbc.JdbcTemplate;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class DataBaseTest {
    private JdbcTemplate jdbcTemplate = new JdbcTemplate(new ConnectionManagerImpl());

    @Test
    void codingAsAHobby() {
        String sql = "select hobby, round(count(hobby) / (select count(hobby) from survey_results_public) * 100, 1) as proportion\n" +
                "from survey_results_public \n" +
                "group by hobby";

        Map<String, Double> codingAsHobby = jdbcTemplate.executeQuery(sql, rs -> {
            Map<String, Double> result = Maps.newHashMap();
            while(rs.next()) {
                result.put(rs.getString("hobby"), rs.getDouble("proportion"));
            }
            return result;
        });

        assertThat(codingAsHobby.get("Yes")).isEqualTo(80.8);
        assertThat(codingAsHobby.get("No")).isEqualTo(19.2);
    }

    @Test
    void yearsOfProfessionalCodingExperienceByDeveloperType() {
        String sql = "SELECT d.DevType AS DevType, ROUND(AVG(s.YearsCodingProf), 1) AS YearsCodingProf\n" +
                "FROM survey_results_public as s, TMP_DEV_TYPE as d\n" +
                "WHERE s.YearsCodingProf != 'NA' AND s.DevType LIKE CONCAT('%', d.DevType, '%')\n" +
                "GROUP BY d.DevType\n" +
                "ORDER BY YearsCodingProf DESC;";

        Map<String, Double> yearsOfProf = jdbcTemplate.executeQuery(sql, rs -> {
            Map<String, Double> result = Maps.newHashMap();
            while(rs.next()) {
                result.put(rs.getString("DevType"), rs.getDouble("YearsCodingProf"));
            }
            return result;
        });

        assertThat(yearsOfProf.get("Engineering manager")).isEqualTo(10.2);
        assertThat(yearsOfProf.get("DevOps specialist")).isEqualTo(8.0);
        assertThat(yearsOfProf.get("Desktop or enterprise applications developer")).isEqualTo(7.7);
        assertThat(yearsOfProf.get("Embedded applications or devices developer")).isEqualTo(7.5);
        assertThat(yearsOfProf.get("Data or business analyst")).isEqualTo(7.2);
        assertThat(yearsOfProf.get("System administrator")).isEqualTo(7.0);
        assertThat(yearsOfProf.get("Database administrator")).isEqualTo(6.9);
        assertThat(yearsOfProf.get("Full-stack developer")).isEqualTo(6.3);
        assertThat(yearsOfProf.get("Back-end developer")).isEqualTo(6.2);
        assertThat(yearsOfProf.get("Educator or academic researcher")).isEqualTo(6.2);
        assertThat(yearsOfProf.get("Designer")).isEqualTo(6.0);
        assertThat(yearsOfProf.get("QA or test developer")).isEqualTo(5.8);
        assertThat(yearsOfProf.get("Front-end developer")).isEqualTo(5.5);
        assertThat(yearsOfProf.get("Data scientist or machine learning specialist")).isEqualTo(5.5);
        assertThat(yearsOfProf.get("Mobile developer")).isEqualTo(5.2);
        assertThat(yearsOfProf.get("Game or graphics developer")).isEqualTo(4.6);
    }

    private static class ConnectionManagerImpl implements ConnectionManager {
        private static final String DB_PROPERTIES = "db.properties";

        public static DataSource getDataSource() {
            return DataSourcePropertiesBinder.bind(DB_PROPERTIES);
        }

        @Override
        public Connection getConnection() {
            try {
                return getDataSource().getConnection();
            } catch (SQLException e) {
                throw new IllegalStateException(e);
            }
        }
    }
}
