package sql;

import nextstep.jdbc.JdbcTemplate;
import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;

public class SqlTest {
    private static final String DB_PROPERTIES_PATH = "./src/test/resources/db.properties";
    private static final String DRIVER_CLASS_KEY = "jdbc.driverClass";
    private static final String URL_KEY = "jdbc.url";
    private static final String USERNAME_KEY = "jdbc.username";
    private static final String PASSWORD_KEY = "jdbc.password";

    private JdbcTemplate jdbcTemplate;
    private BasicDataSource dataSource;

    @BeforeEach
    void setUp() {
        Properties dbProperties = new Properties();
        try {
            dbProperties.load(new FileReader(DB_PROPERTIES_PATH));
        } catch (IOException e) {
            e.printStackTrace();
        }

        dataSource = new BasicDataSource();
        dataSource.setDriverClassName(dbProperties.getProperty(DRIVER_CLASS_KEY));
        dataSource.setUrl(dbProperties.getProperty(URL_KEY));
        dataSource.setUsername(dbProperties.getProperty(USERNAME_KEY));
        dataSource.setPassword(dbProperties.getProperty(PASSWORD_KEY));

        dataSource.setInitialSize(2);
        dataSource.setMinIdle(2);

        jdbcTemplate = new JdbcTemplate(dataSource);

        // Set default connection in connection pool
        try (Connection ignored = dataSource.getConnection()) {
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private class Result {
        private String name;
        private double result;

        Result(String name, double result) {
            this.name = name;
            this.result = result;
        }
    }

    private class Results {
        private Map<String, Double> results = new HashMap<>();

        Results(List<Result> results) {
            results.forEach(result -> this.results.put(result.name, result.result));
        }

        Double getResult(String name) {
            return results.get(name);
        }
    }

    @Test
    @DisplayName("Coding As a Hobby")
    void first() {
        // hobby를 인덱스로 활용
        // ALTER TABLE survey_results_public ADD INDEX idx_hobby ( hobby );
        String query = "SELECT hobby, ROUND((count(Hobby) * 100) / (select count(Hobby) from survey_results_public), 1) " +
                "FROM survey_results_public group by Hobby";

        List<Result> res = jdbcTemplate.select(query, resultSet ->
                new Result(resultSet.getString(1), resultSet.getDouble(2)));

        Results results = new Results(res);
        assertThat(results.getResult("Yes")).isEqualTo(80.8);
        assertThat(results.getResult("No")).isEqualTo(19.2);
    }


    @Test
    @DisplayName("Years of Professional Coding Experience by Developer Type")
    void second() {
        String query =
                "SELECT DevType, ROUND(avg(year), 1) " +
                        "FROM (" +
                        "SELECT\n" +
                        "  SUBSTRING_INDEX(SUBSTRING_INDEX(t1.DevType, ';', numbers.n), ';', -1) DevType,\n" +
                        "  CASE\n" +
                        "WHEN YearsCodingProf = '0-2 years' THEN 0\n" +
                        "WHEN YearsCodingProf = '3-5 years' THEN 3\n" +
                        "WHEN YearsCodingProf = '6-8 years' THEN 6\n" +
                        "WHEN YearsCodingProf = '9-11 years' THEN 9\n" +
                        "WHEN YearsCodingProf = '12-14 years' THEN 12\n" +
                        "WHEN YearsCodingProf = '15-17 years' THEN 15\n" +
                        "WHEN YearsCodingProf = '18-20 years' THEN 18\n" +
                        "WHEN YearsCodingProf = '21-23 years' THEN 21\n" +
                        "WHEN YearsCodingProf = '24-26 years' THEN 24\n" +
                        "WHEN YearsCodingProf = '27-29 years' THEN 27\n" +
                        "WHEN YearsCodingProf = '30 or more years' THEN 30\n" +
                        "ELSE 0\n" +
                        "END AS year\n" +
                        "FROM\n" +
                        "  (select 1 n union all\n" +
                        "   select 2 union all\n" +
                        "   select 3 union all\n" +
                        "   select 4 union all \n" +
                        "   select 5 union all \n" +
                        "   select 6 union all \n" +
                        "   select 7 union all \n" +
                        "   select 8 union all \n" +
                        "   select 9 union all \n" +
                        "   select 10 union all \n" +
                        "   select 11 union all \n" +
                        "   select 12 union all \n" +
                        "   select 13 union all \n" +
                        "   select 14 union all \n" +
                        "   select 15 union all \n" +
                        "   select 16 union all \n" +
                        "   select 17 union all \n" +
                        "   select 18 union all \n" +
                        "   select 19 union all \n" +
                        "   select 20 ) numbers\n" +
                        "   INNER JOIN survey_results_public t1\n" +
                        "  ON CHAR_LENGTH(t1.DevType) - CHAR_LENGTH(REPLACE(t1.DevType, ';', '')) >= numbers.n - 1\n" +
                        "  WHERE t1.DevType != 'NA' AND t1.YearsCodingProf != 'NA') sub\n" +
                        "  GROUP BY DevType;";

        List<Result> res = jdbcTemplate.select(query, resultSet ->
                new Result(resultSet.getString(1), resultSet.getDouble(2)));

        Results results = new Results(res);
        assertThat(results.getResult("Engineering manager")).isEqualTo(10.2);
        assertThat(results.getResult("DevOps specialist")).isEqualTo(8.0);
        assertThat(results.getResult("Desktop or enterprise applications developer")).isEqualTo(7.7);
        assertThat(results.getResult("Embedded applications or devices developer")).isEqualTo(7.5);
        assertThat(results.getResult("Data or business analyst")).isEqualTo(7.2);
        assertThat(results.getResult("System administrator")).isEqualTo(7.0);
        assertThat(results.getResult("Database administrator")).isEqualTo(6.9);
        assertThat(results.getResult("Full-stack developer")).isEqualTo(6.3);
        assertThat(results.getResult("Back-end developer")).isEqualTo(6.2);
        assertThat(results.getResult("Educator or academic researcher")).isEqualTo(6.2);
        assertThat(results.getResult("Designer")).isEqualTo(6.0);
        assertThat(results.getResult("QA or test developer")).isEqualTo(5.8);
        assertThat(results.getResult("Front-end developer")).isEqualTo(5.5);
        assertThat(results.getResult("Data scientist or machine learning specialist")).isEqualTo(5.5);
        assertThat(results.getResult("Mobile developer")).isEqualTo(5.2);
        assertThat(results.getResult("Game or graphics developer")).isEqualTo(4.6);
    }
}
