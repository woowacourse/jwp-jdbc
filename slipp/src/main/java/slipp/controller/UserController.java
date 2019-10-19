package slipp.controller;

import nextstep.mvc.JspView;
import nextstep.mvc.ModelAndView;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.annotation.RequestMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import slipp.dao.UserDao;
import slipp.domain.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserDao userDao;

    public UserController(UserDao userDao) {
        this.userDao = userDao;
    }

    @RequestMapping(value = "/users/create", method = RequestMethod.POST)
    public ModelAndView create(HttpServletRequest req, HttpServletResponse res) {
        final User user = new User(
                req.getParameter("userId"),
                req.getParameter("password"),
                req.getParameter("name"),
                req.getParameter("email"));
        this.userDao.create(user);
        logger.debug("User : {}", user);
        return redirect("/");
    }

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public ModelAndView list(HttpServletRequest req, HttpServletResponse res) {
        return UserSessionUtils.isLogined(req.getSession())
                ? new ModelAndView(new JspView("/user/list.jsp")).addObject("users", this.userDao.findAll())
                : redirect("/users/loginForm");
    }

    private ModelAndView redirect(String path) {
        return new ModelAndView(new JspView(JspView.DEFAULT_REDIRECT_PREFIX + path));
    }
}