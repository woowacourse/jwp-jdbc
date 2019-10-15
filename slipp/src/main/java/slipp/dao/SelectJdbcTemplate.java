package slipp.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import slipp.support.db.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class SelectJdbcTemplate {
    private static final Logger logger = LoggerFactory.getLogger(SelectJdbcTemplate.class);

    List query(String sql) {
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            setValues(pstmt);

            ResultSet resultSet = pstmt.executeQuery();
            List<Object> objects = new ArrayList<>();
            while (resultSet.next()) {
                objects.add(mapRow(resultSet));
            }
            return objects;
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException();
        }
    }

    Object queryForObject(String sql) {
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            setValues(pstmt);

            ResultSet resultSet = pstmt.executeQuery();
            List<Object> objects = new ArrayList<>();
            while (resultSet.next()) {
                objects.add(mapRow(resultSet));
            }
            logger.debug("objects size : {} ", objects.size());
            return objects.get(0);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException();
        }
    }

    abstract Object mapRow(ResultSet resultSet) throws SQLException;

    abstract void setValues(PreparedStatement pstmt) throws SQLException;
}
