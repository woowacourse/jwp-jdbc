package nextstep.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ObjectSetter implements PreparedStatementSetter {
    private Object[] objects;

    public ObjectSetter(Object[] objects) {
        this.objects = objects;
    }

    @Override
    public void values(PreparedStatement pstmt) throws SQLException {
        for (int i = 0; i < objects.length; ++i) {
            pstmt.setObject(i + 1, objects[i]);
        }
    }
}
