package nextstep.jdbc;

import java.util.List;
import java.util.Optional;

public interface DBTemplate {
    void create(String query, Object... params);
    <A> List<A> readAll(RowMapper<A> rowMapper, String query);
    <A> Optional<A> read(RowMapper<A> rowMapper, String query, Object... params);
    void update(String query, Object... params);
    void delete(String query, Object... params);
    void deleteAll(String query);
    void execute(String query, Object... params);
}