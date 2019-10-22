package slipp.support.properties;

import slipp.support.PropertyReader;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PropertiesFileReader implements PropertyReader {
    private static final String PROPERTY_DELIMITER = "=";

    public Map<String, String> read(String filePath) throws IOException {
        List<String> rawProperties = new ArrayList<>();

        try (BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)))) {
            String line;
            while ((line = bf.readLine()) != null) {
                rawProperties.add(line);
            }

            return convertListToMap(rawProperties);
        }
    }

    private Map<String, String> convertListToMap(List<String> rawProperties) {
        return rawProperties.stream()
                .collect(Collectors.toMap(rawProperty -> rawProperty.substring(0, rawProperty.indexOf(PROPERTY_DELIMITER)),
                        rawProperty -> rawProperty.substring(rawProperty.indexOf(PROPERTY_DELIMITER) + 1)));
    }
}
