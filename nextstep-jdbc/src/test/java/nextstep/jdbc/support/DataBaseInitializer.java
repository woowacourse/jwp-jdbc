package nextstep.jdbc.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class DataBaseInitializer {
    private static final Logger logger = LoggerFactory.getLogger(DataBaseInitializer.class);

    private static final String SCHEMA_SQL = "src/test/java/nextstep/jdbc/support/test.sql";

    private DataBaseInitializer() {
    }

    public static void init(final DataSource dataSource) {
        try {
            final File file = new File(SCHEMA_SQL);
            final FileInputStream fis = new FileInputStream(file);
            final String query = getFileContent(fis);

            try (final Connection connection = dataSource.getConnection();
                 final PreparedStatement ps = connection.prepareStatement(query)) {
                ps.executeUpdate();
                logger.debug(query);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new DataBaseInitializerException(e.getMessage(), e);
        }
    }

    private static String getFileContent(FileInputStream fis) throws IOException {
        final StringBuilder sb = new StringBuilder();
        final Reader r = new InputStreamReader(fis, StandardCharsets.UTF_8);
        final char[] buf = new char[1024];
        int amt = r.read(buf);
        while (amt > 0) {
            sb.append(buf, 0, amt);
            amt = r.read(buf);
        }
        return sb.toString();
    }
}