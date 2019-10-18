package slipp.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import slipp.domain.TestEntity;
import slipp.domain.User;
import slipp.support.db.ConnectionManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class TestEntityDaoTest {
    private static final Logger log = LoggerFactory.getLogger(TestEntityDaoTest.class);

    @BeforeEach
    public void setup() {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("jwp.sql"));
        DatabasePopulatorUtils.execute(populator, ConnectionManager.getDataSource());
    }


    @Test
    public void crudTestEntityWithoutRowMapper() throws Exception {
        TestEntity expected = new TestEntity("kjm",10, 100);
        TestEntityDao dao = new TestEntityDao();
        dao.insert(expected);
        TestEntity actual = dao.findByUserIdWithoutRowMapper(expected.getUserId());
        assertThat(actual).isEqualTo(expected);
        log.debug(actual.toString());


        expected.update(11, 101);
        dao.update(expected);
        actual = dao.findByUserIdWithoutRowMapper(expected.getUserId());
        assertThat(actual).isEqualTo(expected);
        log.debug(actual.toString());
    }


    @Test
    public void findAllWithoutRowMapper() throws Exception {
        TestEntityDao dao = new TestEntityDao();
        List<TestEntity> entities = dao.findAllWithoutRowMapper();
        assertThat(entities).hasSize(1);
        assertThat(entities.get(0).getAge()).isEqualTo(40);
    }
}
