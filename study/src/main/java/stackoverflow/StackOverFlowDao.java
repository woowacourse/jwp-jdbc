package stackoverflow;

import nextstep.jdbc.JdbcTemplate;
import nextstep.jdbc.RowMapper;
import slipp.support.db.ConnectionManager;

import java.util.HashMap;
import java.util.Map;

public class StackOverFlowDao {
    private static final RowMapper<Request1> request1Mapper = resultSet -> {
        String no = resultSet.getString(2);
        resultSet.next();
        String yes = resultSet.getString(2);
        return new Request1(yes, no);
    };
    private static final RowMapper<Map<String, String>> request2Mapper = resultSet -> {
        Map<String, String> results = new HashMap<>();
        results.put(resultSet.getString(1), resultSet.getString(2));
        while (resultSet.next()) {
            results.put(resultSet.getString(1), resultSet.getString(2));
        }
        return results;
    };
    private JdbcTemplate jdbcTemplate;

    public StackOverFlowDao() {
        jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
    }

    public Request1 request1() {
        String sql = "SELECT Hobby, " +
                "(CONCAT(ROUND((COUNT(1) * 100.0 / (SELECT COUNT(1) FROM survey_results_public)), 1), '%')) " +
                "AS Percentage " +
                "FROM survey_results_public " +
                "GROUP BY Hobby;";
        return jdbcTemplate.execute(sql, request1Mapper);
    }

    public Map<String, String> request2() {
        String sql = "select b.DevType, round(avg(cast(substring_index(a.YearsCodingProf, '-', 1) as unsigned)), 1) as AVERAGE \n" +
                "from survey_results_public a \n" +
                "join DevType b \n" +
                "on a.DevType like concat('%', b.DevType, '%') where not a.YearsCodingProf = 'NA' group by b.DevType;";
        return jdbcTemplate.execute(sql, request2Mapper);
    }

    public Map<String, String> request2_Illegal() {
        String sql = "select b.DevType, round(avg(cast(substring_index(a.YearsCodingProf, '-', 1) as unsigned)), 1) as AVERAGE \n" +
                "from survey_results_public a \n" +
                "join (select distinct DevType from survey_results_public where not DevType = 'NA' and not DevType like '%;%') b \n" +
                "on a.DevType like concat('%', b.DevType, '%') where not a.YearsCodingProf = 'NA' group by b.DevType;";
        return jdbcTemplate.execute(sql, request2Mapper);
    }
}
