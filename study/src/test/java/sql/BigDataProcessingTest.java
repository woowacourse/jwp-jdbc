package sql;

import nextstep.jdbc.JdbcTemplate;
import nextstep.jdbc.resultsetmapper.SimpleResultSetMapper;
import org.junit.jupiter.api.Test;
import slipp.support.db.ConnectionManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BigDataProcessingTest {

    private static final JdbcTemplate JDBC_TEMPLATE = new JdbcTemplate(ConnectionManager.getDataSource());
    private static final String CODING_AS_A_HOBBY_SQL = "SELECT Hobby AS 'response', ROUND(ROUND(COUNT(Hobby) / (SELECT COUNT(*) FROM survey_results_public), 3) * 100, 1) AS 'ratio'" +
            "FROM survey_results_public GROUP BY Hobby ORDER BY response DESC;";
    private static final String YEARS_OF_PROFESSIONAL_SQL = "SELECT dtype AS 'type', ROUND(AVG(period), 1) AS 'mean' FROM dtype WHERE dtype = ?;";

    @Test
    void coding_as_a_hobby() {
        SimpleResultSetMapper<ResponseAndRatioDto> simpleResultSetMapper = new SimpleResultSetMapper<>(ResponseAndRatioDto.class);
        List<ResponseAndRatioDto> list = JDBC_TEMPLATE.executeQueryForList(
                CODING_AS_A_HOBBY_SQL, simpleResultSetMapper);
        assertEquals(list.get(0).getResponse(), "Yes");
        assertEquals(list.get(0).getRatio().toString(), "80.8");
        assertEquals(list.get(1).getResponse(), "No");
        assertEquals(list.get(1).getRatio().toString(), "19.2");
    }

    @Test
    void years_of_professional_coding_experience_by_developer_type() {
        SimpleResultSetMapper<TypeAndMeanDto> simpleResultSetMapper = new SimpleResultSetMapper<>(TypeAndMeanDto.class);
        TypeAndMeanDto typeAndMeanDto = JDBC_TEMPLATE.executeQuery(
                YEARS_OF_PROFESSIONAL_SQL, simpleResultSetMapper, "Engineering manager");
        assertEquals(typeAndMeanDto.getMean().toString(), "10.2");
    }
}
