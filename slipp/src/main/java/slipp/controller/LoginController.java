package slipp.controller;

import nextstep.mvc.asis.Controller;
import slipp.dao.UserDao;
import slipp.domain.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginController implements Controller {
    private final UserDao userDao = new UserDao();

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String userId = req.getParameter("userId");
        String password = req.getParameter("password");

        return userDao.findByUserId(userId)
                .filter(user -> user.matchPassword(password))
                .map(user -> login(req, user))
                .orElseGet(() -> failLogin(req));
    }

    private String login(HttpServletRequest req, User user) {
        HttpSession session = req.getSession();
        session.setAttribute(UserSessionUtils.USER_SESSION_KEY, user);
        return "redirect:/";
    }

    private String failLogin(HttpServletRequest req) {
        req.setAttribute("loginFailed", true);
        return "/user/login.jsp";
    }
}
