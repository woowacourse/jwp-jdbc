package support.test;

import org.springframework.mock.web.MockServletContext;
import slipp.support.context.ContextLoaderListener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class TestInitializer {
    public static void initialize() {
        MockServletContext servletContext = new MockServletContext();
        ServletContextListener listener = new ContextLoaderListener();
        ServletContextEvent event = new ServletContextEvent(servletContext);

        listener.contextInitialized(event);
    }
}
