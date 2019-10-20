package slipp.dao;

import java.util.List;
import java.util.Optional;

public interface Dao<T> {

    void insert(T t);

    int update(T t);

    int delete(T t);

    List<T> findAll();

    Optional<T> findBy(String id);
}
