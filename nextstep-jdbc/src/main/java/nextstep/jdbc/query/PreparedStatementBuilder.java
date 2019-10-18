package nextstep.jdbc.query;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PreparedStatementBuilder {
    private String query;
    private List<String> attributes;

    public PreparedStatementBuilder(String query) {
        this.query = query;
        attributes = new ArrayList<>();
    }

    public PreparedStatementBuilder addAttribute(String attribute) {
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
        PreparedStatementBuilder preparedStatementBuilder = (PreparedStatementBuilder) o;
        return Objects.equals(query, preparedStatementBuilder.query);
    }

    @Override
    public int hashCode() {
        return Objects.hash(query);
    }
}
