package slipp;

import nextstep.jdbc.ConnectionManager;
import org.apache.catalina.startup.Tomcat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class WebServerLauncher {
    private static final Logger logger = LoggerFactory.getLogger(WebServerLauncher.class);

    private static final String DB_DRIVER = "org.h2.Driver";
    private static final String DB_URL = "jdbc:h2:mem:jwp-framework";
    private static final String DB_USERNAME = "sa";
    private static final String DB_PW = "";

    public static void main(String[] args) throws Exception {
        String webappDirLocation = "./slipp/webapp/";
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8080);

        tomcat.addWebapp("/", new File(webappDirLocation).getAbsolutePath());
        logger.info("configuring app with basedir: {}", new File("./" + webappDirLocation).getAbsolutePath());

        ConnectionManager.initialize(DB_DRIVER, DB_URL, DB_USERNAME, DB_PW);

        tomcat.start();
        tomcat.getServer().await();
    }
}
