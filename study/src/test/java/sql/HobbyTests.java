package sql;

import nextstep.jdbc.JdbcTemplate;
import nextstep.jdbc.exception.DatabaseAccessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTimeout;

public class HobbyTests {
    private final static Logger logger = LoggerFactory.getLogger(HobbyTests.class);
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        jdbcTemplate = new JdbcTemplate(DataSourceFactory.getDataSource());
        addHobbyIndex();
    }

    private void addHobbyIndex() {
        String sql = "create index idx_hobby on survey_results_public(Hobby);";
        try {
            jdbcTemplate.update(sql);
        } catch (DatabaseAccessException e) {
            logger.debug(e.getMessage());
        }
    }

    private void removeHobbyIndex() {
        String sql = "alter table survey_results_public drop index idx_hobby";
        try {
            jdbcTemplate.update(sql);
        } catch (DatabaseAccessException e) {
            logger.debug(e.getMessage());
        }
    }

    @Test
    void hobby_timeout() {
        assertTimeout(Duration.ofMillis(1000), () -> {
            findHobbies();
        });
    }

    @Test
    void hobby_equals() {
        List<Hobby> hobbies = findHobbies();
        Hobby noHobby = new Hobby("No", 19.18);
        Hobby yesHobby = new Hobby("Yes", 80.82);

        int noIndex = 0;
        int yesIndex = 1;

        assertThat(hobbies.get(noIndex).getHobby()).isEqualTo(noHobby.getHobby());
        assertThat(hobbies.get(noIndex).getPercentage()).isEqualTo(noHobby.getPercentage());
        assertThat(hobbies.get(yesIndex).getHobby()).isEqualTo(yesHobby.getHobby());
        assertThat(hobbies.get(yesIndex).getPercentage()).isEqualTo(yesHobby.getPercentage());
    }

    private List<Hobby> findHobbies() {
        String sql = "select Hobby as hobby, ANY_VALUE(sum(100)/total) as percentage \n" +
            "from survey_results_public \n" +
            "cross join (select count(Hobby) as total from survey_results_public) x \n" +
            "group by Hobby;";
        return jdbcTemplate.queryForObjects(sql, rs -> new Hobby(rs.getString("hobby"), rs.getFloat("percentage")));
    }

    private class Hobby {
        private String hobby;
        private double percentage;

        public Hobby(String hobby, double percentage) {
            this.hobby = hobby;
            this.percentage = round(percentage);
        }

        public String getHobby() {
            return hobby;
        }

        public double getPercentage() {
            return percentage;
        }

        private double round(double percentage) {
            return Math.round(percentage * 100) / 100;
        }
    }
}
