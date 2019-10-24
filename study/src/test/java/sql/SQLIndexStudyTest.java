package sql;

import nextstep.jdbc.DBConnection;
import nextstep.jdbc.JdbcProxyTemplate;
import nextstep.jdbc.RowMapper;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

public class SQLIndexStudyTest {

    @Test
    void coding_as_a_hobby() {
        JdbcProxyTemplate jdbcTemplate = new JdbcProxyTemplate(DBConnection.getMysqlConnection());
        String sql = "Select hobby,Round((Count(Hobby)* 100 / (Select Count(*) From survey_results_public)),1) as Score From survey_results_public Group By Hobby";
        List<HobbyDto> hobbyDtos = Arrays.asList(new HobbyDto("No", 19.2), new HobbyDto("Yes", 80.8));
        RowMapper<HobbyDto> rowMapper = rs -> new HobbyDto(rs.getString(1), rs.getDouble(2));
        assertThat(jdbcTemplate.query(sql, rowMapper)).isEqualTo(hobbyDtos);
    }

    public class HobbyDto {
        private String interest;
        private Double percent;

        public HobbyDto(String interest, Double percent) {
            this.interest = interest;
            this.percent = percent;
        }

        public String getInterest() {
            return interest;
        }

        public void setInterest(String interest) {
            this.interest = interest;
        }

        public Double getPercent() {
            return percent;
        }

        public void setPercent(Double percent) {
            this.percent = percent;
        }

        @Override
        public String toString() {
            return interest + ":" + percent;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof HobbyDto)) return false;
            HobbyDto hobbyDto = (HobbyDto) o;
            return Objects.equals(getInterest(), hobbyDto.getInterest()) &&
                    Objects.equals(getPercent(), hobbyDto.getPercent());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getInterest(), getPercent());
        }
    }

}
