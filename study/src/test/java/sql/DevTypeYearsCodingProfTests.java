package sql;

import nextstep.jdbc.JdbcTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class DevTypeYearsCodingProfTests {
    private final static Logger logger = LoggerFactory.getLogger(DevTypeYearsCodingProfTests.class);
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        jdbcTemplate = new JdbcTemplate(DataSourceFactory.getDataSource());
    }

    @Test
    void dev_type_years_coding_prof() {
        List<DevType> devTypes = jdbcTemplate.queryForObjects(getSql(), rs -> new DevType(rs.getString("devType"), rs.getDouble("percent")));
        DevType enginType = new DevType("Engineering manager", 10.2);
        DevType databaseType = new DevType("Database administrator", 6.9);
        DevType mobileType = new DevType("Mobile developer", 5.2);
        assertTrue(devTypes.contains(enginType));
        assertTrue(devTypes.contains(databaseType));
        assertTrue(devTypes.contains(mobileType));
    }

    String getSql() {
        return "select devType, round(avg(years), 1) as percent\n" +
            "from (\n" +
            "select\n" +
            "\tcast(YearsCodingProf as UNSIGNED) as years,\n" +
            "\tSUBSTRING_INDEX (SUBSTRING_INDEX(DevType,';',numbers.n),';',-1) devType\n" +
            "from \n" +
            "\t(select 1 n union all\n" +
            "\tselect 2 union all select 3 union all\n" +
            "\tselect 4 union all select 5 union all\n" +
            "\tselect 6 union all select 7 union all\n" +
            "\tselect 8 union all select 9 union all\n" +
            "\tselect 10 union all select 11 union all\n" +
            "\tselect 12 union all select 13 union all\n" +
            "\tselect 14 union all select 15 union all\n" +
            "\tselect 16 union all select 17 union all\n" +
            "\tselect 18 union all select 19) numbers INNER  JOIN (\n" +
            "\t\tselect DevType, YearsCodingProf \n" +
            "\t\tfrom survey_results_public \n" +
            "\t\twhere YearsCodingProf != 'NA' \n" +
            "\t\tand DevType != 'NA'\n" +
            "\t) as table1\n" +
            "on CHAR_LENGTH ( DevType ) \n" +
            "- CHAR_LENGTH ( REPLACE ( DevType ,  ';' ,  '' ))>= numbers.n-1 \n" +
            ") as table2\n" +
            "group by devType\n" +
            "order by avg(years) desc;";
    }

    private class DevType {
        private String devType;
        private double percent;

        public DevType(String devType, double percent) {
            this.devType = devType;
            this.percent = percent;
        }

        public String getDevType() {
            return devType;
        }

        public double getPercent() {
            return percent;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            DevType devType1 = (DevType) o;

            if (Double.compare(devType1.percent, percent) != 0) return false;
            return devType != null ? devType.equals(devType1.devType) : devType1.devType == null;
        }

        @Override
        public int hashCode() {
            int result;
            long temp;
            result = devType != null ? devType.hashCode() : 0;
            temp = Double.doubleToLongBits(percent);
            result = 31 * result + (int) (temp ^ (temp >>> 32));
            return result;
        }
    }
}
