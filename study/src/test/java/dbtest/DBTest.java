package dbtest;

import nextstep.jdbc.ConnectionManager;
import nextstep.jdbc.JdbcTemplate;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

public class DBTest {
    private JdbcTemplate jdbcTemplate = new JdbcTemplate(new ConnectionManager("db.properties"));
    private static final Logger log = LoggerFactory.getLogger(DBTest.class);

    @Test
    void infoCodingAsHobby() {
        String sql = "SELECT hobby, count(hobby) / (SELECT count(hobby) FROM jwp_jdbc.survey_results_public) from jwp_jdbc.survey_results_public group by hobby;";

        for (String result : jdbcTemplate.query(sql, rs -> "hobby : " + rs.getString(1) + ", rate : " + rs.getDouble(2))) {
            log.info(result);
        }
    }

    @Test
    void yearsOfProfessionalCodingExperience() {
        initDevTypeTable();

        String sql = "SELECT d.name, ROUND(AVG(s.YearsCodingProf),1) as exp\n" +
                "FROM dev_type as d, survey_results_public as s\n" +
                "WHERE s.DevType LIKE CONCAT('%', d.name, '%') and s.YearsCodingProf != 'NA'\n" +
                "GROUP BY d.name\n" +
                "ORDER BY exp DESC;";

        for (String result : jdbcTemplate.query(sql, rs -> rs.getString(1) + ": " + rs.getDouble(2))) {
            log.info(result);
        }
    }

    void initDevTypeTable() {
        String dropDevType = "DROP TABLE dev_type;";
        jdbcTemplate.update(dropDevType);

        String createDevTypeTable = "create table dev_type (" +
                " name varchar(50));";
        jdbcTemplate.update(createDevTypeTable);

        List<String> insertDataSqls = Arrays.asList(
                "insert into dev_type values ('Engineering manager');",
                "insert into dev_type values ('DevOps specialist');",
                "insert into dev_type values ('Desktop or enterprise applications developer');",
                "insert into dev_type values ('Embedded applications or devices developer');",
                "insert into dev_type values ('Data or business analyst');",
                "insert into dev_type values ('System administrator');",
                "insert into dev_type values ('Database administrator');",
                "insert into dev_type values ('Full-stack developer');",
                "insert into dev_type values ('Back-end developer');",
                "insert into dev_type values ('Educator or academic researcher');",
                "insert into dev_type values ('Designer');",
                "insert into dev_type values ('QA or test developer');",
                "insert into dev_type values ('Front-end developer');",
                "insert into dev_type values ('Data scientist or machine learning specialist');",
                "insert into dev_type values ('Mobile developer');",
                "insert into dev_type values ('Game or graphics developer');"
        );
        for (String insertDataSql : insertDataSqls) {
            jdbcTemplate.update(insertDataSql);
        }
    }
}
