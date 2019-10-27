package slipp.controller;

import nextstep.mvc.asis.Controller;
import slipp.controller.exception.UserNotFoundException;
import slipp.dao.UserDao;
import slipp.domain.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ProfileController implements Controller {
    private final UserDao userDao = new UserDao();

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String userId = req.getParameter("userId");
        User user = userDao.findByUserId(userId)
                .orElseThrow(() -> UserNotFoundException.ofUserId(userId));

        req.setAttribute("user", user);
        return "/user/profile.jsp";
    }
}
