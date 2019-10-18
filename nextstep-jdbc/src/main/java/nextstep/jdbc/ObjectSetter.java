package nextstep.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ObjectSetter implements PreparedStatementSetter {
    public static final int PARAMETER_INDEX_START_INDEX_NUMBER = 1;
    private Object[] objects;

    public ObjectSetter(Object[] objects) {
        this.objects = objects;
    }

    @Override
    public void values(PreparedStatement pstmt) throws SQLException {
        for (int i = 0; i < objects.length; ++i) {
            pstmt.setObject(i + PARAMETER_INDEX_START_INDEX_NUMBER, objects[i]);
        }
    }
}
