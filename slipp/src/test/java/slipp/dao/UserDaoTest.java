package slipp.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import slipp.dao.exception.UserNotFoundException;
import slipp.domain.User;
import slipp.dto.UserUpdatedDto;
import slipp.support.db.ConnectionManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class UserDaoTest {
    private static final String TEST_USER_ID = "admin";
    private final UserDao userDao = UserDao.getInstance();

    @BeforeEach
    void setup() {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("jwp.sql"));
        DatabasePopulatorUtils.execute(populator, ConnectionManager.getDataSource());
    }

    @Test
    @DisplayName("id로 사용자를 찾는다.")
    void find() {
        assertThat(userDao.findByUserId(TEST_USER_ID)).isNotNull();
    }

    @Test
    @DisplayName("사용자가 없으면 예외가 발생한다.")
    void find_fail() {
        assertThatThrownBy(() -> userDao.findByUserId("admin2"))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    @DisplayName("모든 사용자를 조회한다.")
    void findAll() {
        List<User> users = userDao.findAll();
        assertThat(users).hasSize(1);
    }

    @Test
    @DisplayName("새로운 사용자를 만든다.")
    void create() {
        User expected = new User("userId", "password", "name", "javajigi@email.com");
        userDao.insert(expected);

        assertThat(userDao.findByUserId(expected.getUserId())).isEqualTo(expected);
    }

    @Test
    @DisplayName("사용자 정보를 업데이트한다.")
    void update() {
        User expected = userDao.findByUserId(TEST_USER_ID);

        expected.update(new UserUpdatedDto("password2", "name2", "sanjigi@email.com"));
        userDao.update(expected);
        assertThat(userDao.findByUserId(expected.getUserId())).isEqualTo(expected);
    }
}