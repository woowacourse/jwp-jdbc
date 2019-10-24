package slipp.controller;

import nextstep.mvc.asis.Controller;
import slipp.dao.JdbcUserDao;
import slipp.dao.UserDao;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HomeController implements Controller {
    private static final UserDao USER_DAO = new JdbcUserDao();

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        req.setAttribute("users", USER_DAO.findAll());
        return "home.jsp";
    }
}
