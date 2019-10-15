package slipp.support.db;

import nextstep.jdbc.ConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import slipp.domain.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class JdbcTemplate {
    private static final Logger log = LoggerFactory.getLogger(JdbcTemplate.class);

    public void update(String query) {
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {
            setValues(pstmt);
            pstmt.execute();
        } catch (SQLException e) {
            log.error("SQLException : {}", e.getMessage());
        }
    }

    public List<Object> query(String query) {
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            List<Object> users = new ArrayList<>();

            while (rs.next()) {
                users.add(new User(rs.getString("userId"),
                        rs.getString("password"),
                        rs.getString("name"),
                        rs.getString("email"))
                );
            }

            return users;
        } catch (SQLException e) {
            log.error("SQLException : {}", e.getMessage());
            throw new IllegalArgumentException("유저가 없습니다.");
        }
    }

    public Object queryForObject(String query) {
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {
            setValues(pstmt);

            ResultSet rs = pstmt.executeQuery();
            User user = null;
            if (rs.next()) {
                user = new User(rs.getString("userId"), rs.getString("password"), rs.getString("name"),
                        rs.getString("email"));
            }
            rs.close();
            return user;
        } catch (SQLException e) {
            log.error("SQLException : {}", e.getMessage());
            throw new IllegalArgumentException("찾는 유저가 없습니다.");
        }
    }

    protected abstract void setValues(PreparedStatement pstmt) throws SQLException;

    protected abstract Object mapRow(ResultSet rs);
}
