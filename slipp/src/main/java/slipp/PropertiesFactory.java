package slipp;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class PropertiesFactory {
    public static Properties generate(String filePath) {
        Properties properties = new Properties();

        try {
            properties.load(new FileReader(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return properties;
    }
}


