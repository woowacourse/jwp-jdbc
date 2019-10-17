package slipp.controller;

import slipp.dao.UserDao;
import slipp.domain.User;
import nextstep.mvc.asis.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginController implements Controller {
    private final UserDao userDao;

    public LoginController() {
        this.userDao = new UserDao();
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        String userId = req.getParameter("userId");
        String password = req.getParameter("password");
        try {
            User user = userDao.findByUserId(userId);
            if (user.matchPassword(password)) {
                HttpSession session = req.getSession();
                session.setAttribute(UserSessionUtils.USER_SESSION_KEY, user);
                return "redirect:/";
            }
            req.setAttribute("loginFailed", true);
            return "/user/login.jsp";
        } catch (Throwable e){
            req.setAttribute("loginFailed", true);
            return "/user/login.jsp";
        }
    }
}
