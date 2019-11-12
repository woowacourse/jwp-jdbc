package sql;

import nextstep.jdbc.DBTemplate;
import nextstep.jdbc.JdbcTemplate;
import nextstep.jdbc.QueryFailedException;
import nextstep.jdbc.RowMapper;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import slipp.support.db.ConnectionManager;

import java.time.Duration;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTimeout;

public class QueryTest {
    private static final Logger logger = LoggerFactory.getLogger(QueryTest.class);

    private static final int MAX_DURATION = 100;
    private static final double HOBBYIST_ANSWER = 80.8;
    private static final Map<String, Double> CODING_EXP_PER_DEV_TYPE_ANSWER = new HashMap<>() {{
        put("Engineering manager", 10.2);
        put("DevOps specialist", 8.0);
        put("Desktop or enterprise applications developer", 7.7);
        put("Embedded applications or devices developer", 7.5);
        put("Data or business analyst", 7.2);
        put("System administrator", 7.0);
        put("Database administrator", 6.9);
        put("Full-stack developer", 6.3);
        put("Back-end developer", 6.2);
        put("Educator or academic researcher", 6.2);
        put("Designer", 6.0);
        put("QA or test developer", 5.8);
        put("Front-end developer", 5.5);
        put("Data scientist or machine learning specialist", 5.5);
        put("Mobile developer", 5.2);
        put("Game or graphics developer", 4.6);
    }};

    private final DBTemplate template = new JdbcTemplate(ConnectionManager.getDataSource());

    @Test
    void codingAsAHobbyTest() {
        final RowMapper<Double> f = rs -> rs.getDouble(1);
        final String resultQuery =
                "SELECT COUNT(*) * 100.0 / (SELECT COUNT(*) FROM survey_results_public) " +
                "FROM survey_results_public WHERE Hobby = 'Yes';";
        final String buildIndexQuery = "CREATE INDEX for_fun ON survey_results_public (Hobby);";
        try {
            this.template.execute(buildIndexQuery);
        } catch (QueryFailedException e) {
            logger.error(e.getMessage());
        }
        this.template.read(f, resultQuery).ifPresent(r ->
            assertThat(r).isCloseTo(HOBBYIST_ANSWER, Offset.offset(1.0))
        );
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
        final DevTypeCodingExpResultProcessor resultProcessor = new DevTypeCodingExpResultProcessor();
        this.template.readAll(f, resultQuery).forEach(r -> resultProcessor.add(new DevTypeCodingExp(r)));
        resultProcessor.process();
        resultProcessor.match(CODING_EXP_PER_DEV_TYPE_ANSWER);
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

class DevTypeCodingExp {
    static final Pattern numbers = Pattern.compile("\\d+");

    private final List<String> devTypes;
    private final int minExp;
    private final int maxExp;

    DevTypeCodingExp(String devTypes, String yearsOfExperience) {
        this.devTypes = Arrays.asList(devTypes.split(";"));
        final Matcher numberExtractor = numbers.matcher(yearsOfExperience);
        numberExtractor.find();
        this.minExp = Integer.parseInt(numberExtractor.group());
        if (numberExtractor.find()) {
            this.maxExp = Integer.parseInt(numberExtractor.group());
        } else {
            this.maxExp = this.minExp;
        }
    }

    DevTypeCodingExp(StringRecord r) {
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

    public List<String> devTypes() {
        return this.devTypes;
    }

    public int minExp() {
        return this.minExp;
    }

    public int maxExp() {
        return this.maxExp;
    }
}

class DevTypeCodingExpResultProcessor {
    final List<DevTypeCodingExp> resultSet = new ArrayList<>();
    final Map<String, Double> processedResult = new HashMap<>();

    DevTypeCodingExpResultProcessor add(DevTypeCodingExp x) {
        this.resultSet.add(x);
        return this;
    }

    void process() {
        final HashMap<String, Integer[]> temp = new HashMap<>();
        this.resultSet.forEach(x ->
            x.devTypes().forEach(y -> {
                final Object[] acc = temp.getOrDefault(y, new Integer[] { 0, 0 });
                final int accYears = (int) acc[0] + x.minExp();
                final int number = (int) acc[1] + 1;
                temp.put(y, new Integer[] { accYears, number });
             })
        );
        temp.forEach((k, v) -> this.processedResult.put(k, (double) v[0] / v[1]));
    }

    void match(Map<String, Double> answerSheet) {
        answerSheet.forEach((k, v) -> assertThat(this.processedResult.get(k)).isCloseTo(v, Offset.offset(0.1)));
    }
}