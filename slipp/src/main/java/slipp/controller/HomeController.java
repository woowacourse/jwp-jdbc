package slipp.controller;

import nextstep.jdbc.JdbcTemplate;
import nextstep.mvc.asis.Controller;
import slipp.dao.ConnectionManager;
import slipp.dao.UserDao;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HomeController implements Controller {
    private final UserDao userDao = new UserDao(new JdbcTemplate(ConnectionManager.getDataSource()));

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        req.setAttribute("users", userDao.findAll());
        return "home.jsp";
    }
}
