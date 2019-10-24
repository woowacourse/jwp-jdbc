package sql;

import nextstep.jdbc.JdbcTemplate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import slipp.support.db.ConnectionManager;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class DatasetTest {

    private final JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource("src/test/java/sql/db.properties"));

    @Test
    @DisplayName("Coding As A Hobby")
    void findCodingAsHobby() {
        String sql = "SELECT round((count(*) * 100 / (SELECT count(*) FROM survey_results_public)), 1) AS percent" +
                " FROM survey_results_public" +
                " WHERE hobby = 'Yes'";

        Object o = jdbcTemplate.executeQueryWithUniqueResult(sql, rs -> rs.getObject("percent"))
                .orElseThrow(RuntimeException::new);
        assertThat((BigDecimal) o).isEqualTo("80.8");
    }

    @Test
    @DisplayName("Years of Professional Coding Experience by Developer Type")
    void findYearsOfProfessionalCoding() {
        String sqlByEngineeringManager = createYearsOfProfQuery("Engineering manager");
        Object o1 = jdbcTemplate.executeQueryWithUniqueResult(sqlByEngineeringManager, rs -> rs.getObject("avg"))
                .orElseThrow(RuntimeException::new);
        
        assertThat((Double) o1).isEqualTo(10.2);

        String sqlByDevOpsSpecialist = createYearsOfProfQuery("DevOps specialist");
        Object o2 = jdbcTemplate.executeQueryWithUniqueResult(sqlByDevOpsSpecialist, rs -> rs.getObject("avg"))
                .orElseThrow(RuntimeException::new);
        
        assertThat((Double) o2).isEqualTo(8.0);
    }

    private String createYearsOfProfQuery(final String devType) {
        return "SELECT round(avg(if(YearsCodingProf = '30 or more years', 30, substring_index(YearsCodingProf, '-', 1))), 1) AS avg" +
                    " FROM survey_results_public" +
                    " WHERE DevType like '%" + devType + "%' AND YearsCodingProf != 'NA'";
    }
}
