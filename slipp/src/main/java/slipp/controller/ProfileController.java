package slipp.controller;

import slipp.controller.exception.UserNotFoundException;
import slipp.dao.UserDao;
import slipp.domain.User;
import slipp.support.db.DataBase;
import nextstep.mvc.asis.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ProfileController implements Controller {
    private UserDao userDao = UserDao.getInstance();

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String userId = req.getParameter("userId");
        User user = userDao.findByUserId(userId)
                .orElseThrow(UserNotFoundException::new);
        req.setAttribute("user", user);
        return "/user/profile.jsp";
    }
}
