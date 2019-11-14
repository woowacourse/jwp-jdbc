package slipp.controller;

import nextstep.mvc.JspView;
import nextstep.mvc.ModelAndView;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.annotation.RequestMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import slipp.domain.User;
import slipp.support.db.DataBase;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @RequestMapping(value = "/users/create", method = RequestMethod.POST)
    public ModelAndView create(HttpServletRequest request, HttpServletResponse resp) throws Exception {
        User user = new User(
            request.getParameter("userId"),
            request.getParameter("name"),
            request.getParameter("password"),
            request.getParameter("email"));
        logger.debug("User : {}", user);
        DataBase.addUser(user);
        resp.setStatus(HttpStatus.CREATED.value());
        return redirect("/");
    }

    private ModelAndView redirect(String path) {
        return new ModelAndView(new JspView(
            JspView.DEFAULT_REDIRECT_PREFIX + path));
    }

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public ModelAndView list(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        if (!UserSessionUtils.isLogined(req.getSession())) {
            return redirect("/users/loginForm");
        }

        ModelAndView mav = new ModelAndView(new JspView("/user/list.jsp"));
        mav.addObject("users", DataBase.findAll());
        return mav;
    }
}
