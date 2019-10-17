package nextstep.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public class QueryUtil {
    private static final String NAMED_PARAMETER_REGEX = ":\\w*";
    private static final Pattern NAMED_PARAMETER_PATTERN = Pattern.compile(NAMED_PARAMETER_REGEX);

    public static PreparedStatement mapQueryParams(Connection conn, String query, Map<String, Object> params) throws SQLException {
        Matcher matcher = NAMED_PARAMETER_PATTERN.matcher(query);
        List<String> parameterNameOrder = getOrderedParameterNames(matcher);
        PreparedStatement pstmt = conn.prepareStatement(query.replaceAll(NAMED_PARAMETER_REGEX, "?"));
        IntStream.range(1, parameterNameOrder.size() + 1)
                .forEach(i -> setParam(pstmt, i, params.get(parameterNameOrder.get(i - 1))));
        return pstmt;
    }

    private static List<String> getOrderedParameterNames(Matcher matcher) {
        List<String> parameterNameOrder = new ArrayList<>();
        while (matcher.find()) {
            parameterNameOrder.add(matcher.group().substring(1));
        }
        return parameterNameOrder;
    }

    private static void setParam(PreparedStatement pstmt, int i, Object param) {
        try {
            pstmt.setObject(i, param);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
