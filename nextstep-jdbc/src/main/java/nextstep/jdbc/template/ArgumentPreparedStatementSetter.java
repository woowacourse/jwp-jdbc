package nextstep.jdbc.template;

import nextstep.jdbc.exception.DatabaseAccessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ArgumentPreparedStatementSetter implements PreparedStatementSetter {
    private static final int START_SET_VALUE_INDEX = 1;
    private static final Logger logger = LoggerFactory.getLogger(ArgumentPreparedStatementSetter.class);

    private final Object[] objects;

    public ArgumentPreparedStatementSetter(Object... objects) {
        this.objects = objects;
    }

    @Override
    public void setValues(PreparedStatement pstmt) {
        for (int index = START_SET_VALUE_INDEX; index <= objects.length; index++) {
            setString(pstmt, objects[index - START_SET_VALUE_INDEX], index);
        }
    }

    private void setString(PreparedStatement pstmt, Object object, int index) {
        try {
            pstmt.setString(index, String.valueOf(object));
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DatabaseAccessException(e);
        }
    }
}
