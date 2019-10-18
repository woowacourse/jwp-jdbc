package nextstep.jdbc.query;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SqlMapper {
    private String query;
    private List<String> attributes;

    public SqlMapper(String query) {
        this.query = query;
        attributes = new ArrayList<>();
    }

    public SqlMapper addAttribute(String attribute) {
        attributes.add(attribute);

        return this;
    }

    public PreparedStatement create(Connection con) throws SQLException {
        PreparedStatement pstmt = con.prepareStatement(query);
        for (int i = 0; i < attributes.size(); i++) {
            pstmt.setString(i + 1, attributes.get(i));
        }

        return pstmt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SqlMapper sqlMapper = (SqlMapper) o;
        return Objects.equals(query, sqlMapper.query);
    }

    @Override
    public int hashCode() {
        return Objects.hash(query);
    }
}
