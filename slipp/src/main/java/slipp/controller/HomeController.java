package slipp.controller;

import slipp.dao.UserDao;
import slipp.support.db.DataBase;
import nextstep.mvc.asis.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HomeController implements Controller {
    private UserDao userDao = UserDao.getInstance();

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        req.setAttribute("users", userDao.findAll());
        return "home.jsp";
    }
}
