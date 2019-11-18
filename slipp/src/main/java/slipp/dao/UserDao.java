package slipp.dao;

import slipp.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    void insert(User user);

    void update(User user);

    List<User> findAll();

    Optional<User> findByUserId(String id);
}
