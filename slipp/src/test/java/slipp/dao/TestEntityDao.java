package slipp.dao;

import nextstep.jdbc.JdbcTemplate;
import nextstep.jdbc.PreparedStatementSetter;
import slipp.support.db.ConnectionManager;

import java.util.List;

public class TestEntityDao {
    private final JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());

    public void insert(TestEntity entity) {
        String sql = "INSERT INTO ENTITIES VALUES (?, ?, ?)";
        PreparedStatementSetter preparedStatementSetter = pstmt -> {
            pstmt.setString(1, entity.getUserId());
            pstmt.setInt(2, entity.getAge());
            pstmt.setLong(3, entity.getGrade());
        };
        jdbcTemplate.executeUpdate(sql, preparedStatementSetter);
    }

    public void update(TestEntity entity) {
        String sql = "UPDATE ENTITIES SET age=?,grade=? WHERE userId=?";
        PreparedStatementSetter preparedStatementSetter = pstmt -> {
            pstmt.setInt(1, entity.getAge());
            pstmt.setLong(2, entity.getGrade());
            pstmt.setString(3, entity.getUserId());
        };
        jdbcTemplate.executeUpdate(sql, preparedStatementSetter);
    }

    public List<TestEntity> findAllWithoutRowMapper() {
        String sql = "SELECT * FROM ENTITIES";
        PreparedStatementSetter preparedStatementSetter = pstmt -> {

        };
        return jdbcTemplate.queryForMultipleEntitiesWithoutRowMapper(sql, preparedStatementSetter, TestEntity.class);
    }

    public TestEntity findByUserIdWithoutRowMapper(String userId) {
        String sql = "SELECT userId, age, grade FROM ENTITIES WHERE userid=?";
        PreparedStatementSetter preparedStatementSetter = pstmt -> {
            pstmt.setString(1, userId);
        };
        return jdbcTemplate.queryForSingleEntityWithoutRowMapper(sql, preparedStatementSetter, TestEntity.class);
    }
}
