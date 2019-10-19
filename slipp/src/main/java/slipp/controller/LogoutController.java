package slipp.controller;

import nextstep.mvc.asis.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LogoutController implements Controller {
    @Override
    public String execute(HttpServletRequest req, HttpServletResponse res) {
        req.getSession().removeAttribute(UserSessionUtils.USER_SESSION_KEY);
        return "redirect:/";
    }
}