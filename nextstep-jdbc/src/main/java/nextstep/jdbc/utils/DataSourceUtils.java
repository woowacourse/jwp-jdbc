package nextstep.jdbc.utils;

import nextstep.jdbc.exception.DataSourceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class DataSourceUtils {
    private static final Logger logger = LoggerFactory.getLogger(DataSourceUtils.class);

    public static Properties createProperties(final String filePath) {
        try {
            final FileReader fileReader = new FileReader(filePath);
            final Properties properties = new Properties();
            properties.load(fileReader);
            return properties;
        } catch (IOException e) {
            logger.error(e.getMessage());
            throw new DataSourceException(e);
        }
    }
}
