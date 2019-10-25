package sql;

import nextstep.jdbc.DBConnection;
import nextstep.jdbc.JdbcProxyTemplate;
import nextstep.jdbc.RowMapper;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

public class SQLIndexStudyTest {
    private static final Logger log = LoggerFactory.getLogger(SQLIndexStudyTest.class);

    @Test
    void coding_as_a_hobby() {
        JdbcProxyTemplate jdbcTemplate = new JdbcProxyTemplate(DBConnection.getMysqlConnection());
        String indexSql = "ALTER TABLE `jwp_jdbc`.`survey_results_public` \n" +
                "ADD INDEX `ix_hobby` USING BTREE (`Hobby` ASC);";
        String sql = "Select hobby,Round((Count(Hobby)* 100 / (Select Count(*) From survey_results_public)),1) as Score From survey_results_public Group By Hobby";

        List<HobbyDto> hobbyDtos = Arrays.asList(new HobbyDto("No", 19.2), new HobbyDto("Yes", 80.8));
        RowMapper<HobbyDto> rowMapper = rs -> new HobbyDto(rs.getString(1), rs.getDouble(2));
        jdbcTemplate.execute(indexSql);
        assertThat(jdbcTemplate.query(sql, rowMapper)).isEqualTo(hobbyDtos);
    }

    @Test
    void devType_per_Professional_Coding_Year() {
        JdbcProxyTemplate jdbcTemplate = new JdbcProxyTemplate(DBConnection.getMysqlConnection());
        String createTableSql = "CREATE TABLE dev_per_year\n" +
                "SELECT dev_type,SUBSTRING_INDEX(splited_coding_year,' ',1) AS prof_coding_year\n" +
                "FROM(SELECT dev_type, SUBSTRING_INDEX(YearsCodingProf, '-', 1) AS splited_coding_year\n" +
                "FROM (SELECT\n" +
                "  SUBSTRING_INDEX(SUBSTRING_INDEX(DevType, ';', numbers.n), ';', -1) dev_type,\n" +
                "  YearsCodingProf\n" +
                "FROM\n" +
                "  (SELECT 1 n \n" +
                "  UNION ALL SELECT 2\n" +
                "   UNION ALL SELECT 3 \n" +
                "   UNION ALL SELECT 4 \n" +
                "   UNION ALL SELECT 5 \n" +
                "   UNION ALL SELECT 6 \n" +
                "   UNION ALL SELECT 7 \n" +
                "   UNION ALL SELECT 8 \n" +
                "   UNION ALL SELECT 9 \n" +
                "   UNION ALL SELECT 10\n" +
                "   UNION ALL SELECT 11\n" +
                "   UNION ALL SELECT 12\n" +
                "   UNION ALL SELECT 13\n" +
                "   UNION ALL SELECT 14\n" +
                "   UNION ALL SELECT 15\n" +
                "   UNION ALL SELECT 16\n" +
                "   UNION ALL SELECT 17\n" +
                "   UNION ALL SELECT 18\n" +
                "   UNION ALL SELECT 19\n" +
                "   UNION ALL SELECT 20) numbers INNER JOIN survey_results_public\n" +
                "  ON CHAR_LENGTH(DevType)\n" +
                "     -CHAR_LENGTH(REPLACE(DevType, ';', ''))>=numbers.n-1\n" +
                "     WHERE YearsCodingProf <> 'NA' AND DevType <> 'NA') AS temp_a ) AS temp_b;";

        String typeSql = "ALTER TABLE dev_per_year MODIFY dev_type varchar(64), MODIFY prof_coding_year double;";

        String indexSql = "ALTER TABLE `jwp_jdbc`.`dev_per_year` \n" +
                "ADD INDEX `ix_dev_type` USING BTREE (`dev_type` ASC);\n";
        String selectSql = "\n" +
                " SELECT \n" +
                "    dev_type, ROUND(AVG(prof_coding_year), 1) AS years\n" +
                "FROM\n" +
                "    dev_per_year\n" +
                "GROUP BY dev_type;";

        RowMapper<CodingYearDto> rowMapper = rs -> new CodingYearDto(rs.getString(1), rs.getDouble(2));
        jdbcTemplate.execute(createTableSql);
        jdbcTemplate.execute(typeSql);
        jdbcTemplate.execute(indexSql);
        jdbcTemplate.query(selectSql, rowMapper);
        List<CodingYearDto> codingYearDtos = jdbcTemplate.query(selectSql, rowMapper);
        assertThat(codingYearDtos).isNotNull();
        log.debug(codingYearDtos.toString());
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

    public class CodingYearDto {
        private String devType;
        private double codingYear;

        public CodingYearDto(String devType, double codingYear) {
            this.devType = devType;
            this.codingYear = codingYear;
        }

        public String getDevType() {
            return devType;
        }

        public void setDevType(String devType) {
            this.devType = devType;
        }

        public double getCodingYear() {
            return codingYear;
        }

        public void setCodingYear(double codingYear) {
            this.codingYear = codingYear;
        }

        @Override
        public String toString() {
            return devType + " : " + codingYear;
        }
    }

}
