package slipp.service;

import slipp.dao.UserDao;
import slipp.domain.User;
import slipp.dto.UserCreatedDto;
import slipp.dto.UserUpdatedDto;

import java.sql.SQLException;
import java.util.List;

public class UserService {

    private UserDao userDao = new UserDao();

    public void save(final UserCreatedDto userCreatedDto) throws SQLException {
        userDao.insert(userCreatedDto.toEntity());
    }

    public List<User> findAll() throws SQLException {
        return userDao.findAll();
    }

    public User findById(String userId) throws SQLException {
        return userDao.findByUserId(userId);
    }

    public void update(final String userId, final UserUpdatedDto updateUser) throws SQLException {
        User user = findById(userId);
        user.update(updateUser);
        userDao.update(user);
    }
}
