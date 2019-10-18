package slipp;

import nextstep.mvc.DispatcherServlet;
import nextstep.mvc.SingletonRegistry;
import nextstep.mvc.asis.ControllerHandlerAdapter;
import nextstep.mvc.tobe.AnnotationHandlerMapping;
import nextstep.mvc.tobe.HandlerExecutionHandlerAdapter;
import nextstep.web.WebApplicationInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import slipp.dao.UserDao;
import slipp.support.db.ConnectionManager;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

public class SlippWebApplicationInitializer  implements WebApplicationInitializer {
    private static final Logger log = LoggerFactory.getLogger(SlippWebApplicationInitializer.class);

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        final SingletonRegistry singletonRegistry = new SingletonRegistry(
                new UserDao(ConnectionManager.getDataSource())
        );

        final DispatcherServlet dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.addHandlerMapping(new ManualHandlerMapping(singletonRegistry));
        dispatcherServlet.addHandlerMapping(new AnnotationHandlerMapping(singletonRegistry, "slipp.controller"));

        dispatcherServlet.addHandlerAdapter(new HandlerExecutionHandlerAdapter());
        dispatcherServlet.addHandlerAdapter(new ControllerHandlerAdapter());

        final ServletRegistration.Dynamic dispatcher = servletContext.addServlet("dispatcher", dispatcherServlet);
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");

        log.info("Start MyWebApplication Initializer");
    }
}