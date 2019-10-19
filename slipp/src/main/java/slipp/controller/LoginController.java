package slipp.controller;

import nextstep.mvc.asis.Controller;
import slipp.dao.UserDao;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginController implements Controller {
    private final UserDao userDao;

    public LoginController(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse res) {
        return this.userDao.findByUserId(req.getParameter("userId")).filter(user ->
            user.matchPassword(req.getParameter("password"))
        ).map(user -> {
            req.getSession().setAttribute(UserSessionUtils.USER_SESSION_KEY, user);
            return "redirect:/";
        }).orElseGet(() -> {
            req.setAttribute("loginFailed", true);
            return "/user/login.jsp";
        });
    }
}