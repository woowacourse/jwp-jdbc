package slipp.controller;

import nextstep.mvc.asis.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import slipp.dao.UserDao;
import slipp.domain.User;
import slipp.exception.NoSuchUserException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginController implements Controller {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    private final UserDao userDao;

    public LoginController() {
        this.userDao = new UserDao();
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String userId = req.getParameter("userId");
        String password = req.getParameter("password");
        try {
            User user = userDao.findByUserId(userId).orElseThrow(NoSuchUserException::new);
            if (user.matchPassword(password)) {
                HttpSession session = req.getSession();
                session.setAttribute(UserSessionUtils.USER_SESSION_KEY, user);
                return "redirect:/";
            }
            req.setAttribute("loginFailed", true);
            return "/user/login.jsp";
        } catch (Throwable t) {
            logger.error("Login Failed : {}", t.getMessage());
            req.setAttribute("loginFailed", true);
            return "/user/login.jsp";
        }
    }
}
