package nextstep.util;

import java.util.Map;

public class QueryUtil {

    public static String parseQueryParams(String query, Map<String, Object> params) {
        StringBuilder parsedQuery = new StringBuilder(query);

        params.forEach((key, value) -> {
            String paramName = ":" + key;
            parsedQuery.replace(0, parsedQuery.length(),
                    parsedQuery.toString().replaceAll(paramName, "'" + value.toString() + "'"));
        });

        return parsedQuery.toString();
    }
}
