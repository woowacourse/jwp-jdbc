package slipp.dao;

import slipp.domain.User;

import java.util.List;

public interface UserDao {

    void insert(final User user);

    void update(User user);

    List<User> findAll();

    User findByUserId(final String userId);

    void deleteByUserId(final String userId);
}
