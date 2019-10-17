package slipp;

import nextstep.mvc.DispatcherServlet;
import nextstep.mvc.asis.ControllerHandlerAdapter;
import nextstep.mvc.tobe.AnnotationHandlerMapping;
import nextstep.mvc.tobe.HandlerExecutionHandlerAdapter;
import nextstep.web.WebApplicationInitializer;
import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import slipp.support.db.DataBaseManager;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

public class SlippWebApplicationInitializer implements WebApplicationInitializer {
    private static final Logger log = LoggerFactory.getLogger(SlippWebApplicationInitializer.class);

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        initializeDatabase();

        DispatcherServlet dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.addHandlerMpping(new ManualHandlerMapping());
        dispatcherServlet.addHandlerMpping(new AnnotationHandlerMapping("slipp.controller"));

        dispatcherServlet.addHandlerAdapter(new HandlerExecutionHandlerAdapter());
        dispatcherServlet.addHandlerAdapter(new ControllerHandlerAdapter());

        ServletRegistration.Dynamic dispatcher = servletContext.addServlet("dispatcher", dispatcherServlet);
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");

        log.info("Start MyWebApplication Initializer");
    }

    private void initializeDatabase() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setUrl("jdbc:h2:mem:jwp-framework");
        dataSource.setUsername("sa");
        dataSource.setPassword("");

        DataBaseManager.initialize(dataSource);
    }
}