package slipp.controller;

import slipp.controller.exception.UserNotFoundException;
import slipp.dao.UserDao;
import slipp.domain.User;
import slipp.support.db.DataBase;
import nextstep.mvc.asis.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginController implements Controller {
    private UserDao userDao = UserDao.getInstance();

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String userId = req.getParameter("userId");
        String password = req.getParameter("password");
        User user;
        try {
            user = userDao.findByUserId(userId)
                    .orElseThrow(UserNotFoundException::new);
        } catch (UserNotFoundException e) {
            req.setAttribute("loginFailed", true);
            return "/user/login.jsp";
        }
        if (user.matchPassword(password)) {
            HttpSession session = req.getSession();
            session.setAttribute(UserSessionUtils.USER_SESSION_KEY, user);
            return "redirect:/";
        }
        req.setAttribute("loginFailed", true);
        return "/user/login.jsp";
    }
}
