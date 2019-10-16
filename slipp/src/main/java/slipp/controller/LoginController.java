package slipp.controller;

import nextstep.mvc.asis.Controller;
import slipp.dao.UserDao;
import slipp.domain.User;
import slipp.exception.UserNotFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginController implements Controller {

    private UserDao userDao = UserDao.getInstance();

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String userId = req.getParameter("userId");
        String password = req.getParameter("password");

        try {
            User user = userDao.findUserById(userId);
            return (user.matchPassword(password)) ? responseWithLoginSuccess(req, user) : responseWithLoginFailed(req);
        } catch (UserNotFoundException e) {
            return responseWithLoginFailed(req);
        }
    }

    private String responseWithLoginSuccess(final HttpServletRequest req, final User user) {
        HttpSession session = req.getSession();
        session.setAttribute(UserSessionUtils.USER_SESSION_KEY, user);
        return "redirect:/";
    }

    private String responseWithLoginFailed(final HttpServletRequest req) {
        req.setAttribute("loginFailed", true);
        return "/user/login.jsp";
    }
}
