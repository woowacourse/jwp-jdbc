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
    void query() {
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
        /*CREATE TABLE IF NOT EXISTS TMP_DEV_TYPE (
            DevType text
        );
        -- DROP TABLE IF EXISTS TMP_DEV_TYPE;
        INSERT INTO TMP_DEV_TYPE(DevType) VALUES("Engineering manager");
        INSERT INTO TMP_DEV_TYPE(DevType) VALUES("DevOps specialist");
        INSERT INTO TMP_DEV_TYPE(DevType) VALUES("Desktop or enterprise applications developer");
        INSERT INTO TMP_DEV_TYPE(DevType) VALUES("Embedded applications or devices developer");
        INSERT INTO TMP_DEV_TYPE(DevType) VALUES("Data or business analyst");
        INSERT INTO TMP_DEV_TYPE(DevType) VALUES("System administrator");
        INSERT INTO TMP_DEV_TYPE(DevType) VALUES("Database administrator");
        INSERT INTO TMP_DEV_TYPE(DevType) VALUES("Full-stack developer");
        INSERT INTO TMP_DEV_TYPE(DevType) VALUES("Back-end developer");
        INSERT INTO TMP_DEV_TYPE(DevType) VALUES("Educator or academic researcher");
        INSERT INTO TMP_DEV_TYPE(DevType) VALUES("Designer");
        INSERT INTO TMP_DEV_TYPE(DevType) VALUES("QA or test developer");
        INSERT INTO TMP_DEV_TYPE(DevType) VALUES("Front-end developer");
        INSERT INTO TMP_DEV_TYPE(DevType) VALUES("Data scientist or machine learning specialist");
        INSERT INTO TMP_DEV_TYPE(DevType) VALUES("Mobile developer");
        INSERT INTO TMP_DEV_TYPE(DevType) VALUES("Game or graphics developer");*/

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
