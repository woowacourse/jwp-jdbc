package slipp.support;

import java.io.IOException;
import java.util.Map;

public interface PropertyReader {
    Map<String, String> read(String filePath) throws IOException;
}
