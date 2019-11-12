package sql;

import nextstep.jdbc.DBTemplate;
import nextstep.jdbc.JdbcTemplate;
import nextstep.jdbc.QueryFailedException;
import nextstep.jdbc.RowMapper;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import slipp.support.db.ConnectionManager;

import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTimeout;

public class QueryTest {
    private static final Logger logger = LoggerFactory.getLogger(QueryTest.class);

    private static final int MAX_DURATION = 100;
    private static final double ANSWER = 80.8;


    private final DBTemplate template = new JdbcTemplate(ConnectionManager.getDataSource());

    @Test
    void codingAsAHobbyTest() {
        final RowMapper<Integer> f = rs -> rs.getInt(1);
        final String resultQuery =
                "SELECT COUNT(*) * 100.0 / (SELECT COUNT(*) FROM survey_results_public) " +
                "FROM survey_results_public WHERE Hobby = 'Yes';";
        final String buildIndexQuery = "CREATE INDEX for_fun ON survey_results_public (Hobby);";
        try {
            this.template.execute(buildIndexQuery);
        } catch (QueryFailedException e) {
            logger.error(e.getMessage());
        }
        this.template.read(f, resultQuery).ifPresent(r -> assertEquals(ANSWER, r, 1.0));
        assertTimeout(Duration.ofMillis(MAX_DURATION), () -> {
            this.template.read(f, resultQuery);
        });
    }


    @Test
    void yearsOfProfessionalCodingExperienceByDeveloperTypeTest() {
        final RowMapper<StringRecord> f = rs -> new StringRecord(rs.getString(1), rs.getString(2));
        final String resultQuery =
                "SELECT DevType, YearsCodingProf FROM survey_results_public " +
                "WHERE DevType != 'NA' AND YearsCodingProf != 'NA';";
        final String buildIndexQuery =
                "CREATE INDEX exp_per_type ON survey_results_public (DevType, YearsCodingProf);";
        try {
            this.template.execute(buildIndexQuery);
        } catch (QueryFailedException e) {
            logger.error(e.getMessage());
        }
        this.template.readAll(f, resultQuery).forEach(r -> (new DevTypeCodingExperience(r)).print());
        assertTimeout(Duration.ofMillis(MAX_DURATION), () -> {
            this.template.readAll(f, resultQuery);
        });
    }
}

class StringRecord {
    public final String a;
    public final String b;

    public StringRecord(String a, String b) {
        this.a = a;
        this.b = b;
    }
}

class DevTypeCodingExperience {
    static final Pattern numbers = Pattern.compile("\\d+");

    final String[] devTypes;
    final int minExp;
    final int maxExp;

    DevTypeCodingExperience(String devTypes, String yearsOfExperience) {
        this.devTypes = devTypes.split(";");
        final Matcher numberExtractor = numbers.matcher(yearsOfExperience);
        numberExtractor.find();
        this.minExp = Integer.parseInt(numberExtractor.group());
        if (numberExtractor.find()) {
            this.maxExp = Integer.parseInt(numberExtractor.group());
        } else {
            this.maxExp = this.minExp;
        }
    }

    DevTypeCodingExperience(StringRecord r) {
        this(r.a, r.b);
    }

    void print() {
        if (this.minExp == this.maxExp) {
            System.out.println(
                    String.join(", ", this.devTypes) + ": " + this.minExp + " years"
            );
        } else {
            System.out.println(
                    String.join(", ", this.devTypes) + ": " + this.minExp + "-" + this.maxExp + " years"
            );
        }
    }
}