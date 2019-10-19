package slipp.controller;

import nextstep.mvc.asis.Controller;
import slipp.dao.UserDao;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ProfileController implements Controller {
    private final UserDao userDao;

    public ProfileController(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse res) {
        return this.userDao.findByUserId(req.getParameter("userId")).map(user -> {
            req.setAttribute("user", user);
            return "/user/profile.jsp";
        }).orElseThrow(() -> new NullPointerException("사용자를 찾을 수 없습니다."));
    }
}