package slipp;

import nextstep.mvc.DispatcherServlet;
import nextstep.mvc.HandlerMapping;
import nextstep.mvc.SingletonRegistry;
import nextstep.mvc.asis.Controller;
import nextstep.mvc.asis.ForwardController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import slipp.controller.*;
import slipp.dao.UserDao;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class ManualHandlerMapping implements HandlerMapping {
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);

    private final SingletonRegistry singletonRegistry;
    private final Map<String, Controller> mappings = new HashMap<>();

    public ManualHandlerMapping(SingletonRegistry singletonRegistry) {
        this.singletonRegistry = singletonRegistry;
    }

    public void initialize() {
        final UserDao userDao = (UserDao) this.singletonRegistry.getInstance(UserDao.class);
        mappings.put("/", new HomeController(userDao));
        mappings.put("/users/form", new ForwardController("/user/form.jsp"));
        mappings.put("/users/loginForm", new ForwardController("/user/login.jsp"));
        mappings.put("/users/login", new LoginController(userDao));
        mappings.put("/users/profile", new ProfileController(userDao));
        mappings.put("/users/logout", new LogoutController());
        mappings.put("/users/updateForm", new UpdateFormUserController(userDao));
        mappings.put("/users/update", new UpdateUserController(userDao));

        logger.info("Initialized Request Mapping!");
        mappings.keySet().forEach(path -> {
            logger.info("Path : {}, Controller : {}", path, mappings.get(path).getClass());
        });
    }

    public Controller getHandler(HttpServletRequest request) {
        return mappings.get(request.getRequestURI());
    }

    void put(String url, Controller controller) {
        mappings.put(url, controller);
    }
}