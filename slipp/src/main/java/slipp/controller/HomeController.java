package slipp.controller;

import nextstep.mvc.asis.Controller;
import slipp.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HomeController implements Controller {

    private UserService userService = new UserService();

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        req.setAttribute("users", userService.findAll());
        return "home.jsp";
    }
}
