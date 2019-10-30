package nextstep.web;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.io.IOException;

public interface WebApplicationInitializer {
    void onStartup(ServletContext servletContext) throws ServletException, IOException;
}
