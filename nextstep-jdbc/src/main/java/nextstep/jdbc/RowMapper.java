package nextstep.jdbc;

import java.sql.ResultSet;

public interface RowMapper<A> extends FunctionThrowingSQLException<ResultSet, A> {}