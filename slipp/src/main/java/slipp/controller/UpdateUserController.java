package slipp.controller;

import nextstep.mvc.asis.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import slipp.controller.exception.UserNotFoundException;
import slipp.dao.UserDao;
import slipp.domain.User;
import slipp.dto.UserUpdatedDto;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UpdateUserController implements Controller {
    private static final Logger log = LoggerFactory.getLogger(UpdateUserController.class);
    private final UserDao userDao = new UserDao();

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        // [TODO] 인터셉터가 있으면 좋겠다
        String userId = req.getParameter("userId");
        User user = userDao.findByUserId(userId)
                .orElseThrow(() -> UserNotFoundException.ofUserId(userId));
        if (!UserSessionUtils.isSameUser(req.getSession(), user)) {
            throw new IllegalStateException("다른 사용자의 정보를 수정할 수 없습니다.");
        }

        UserUpdatedDto updateUser = new UserUpdatedDto(
                req.getParameter("password"),
                req.getParameter("name"),
                req.getParameter("email"));
        log.debug("Update User : {}", updateUser);
        user.update(updateUser);
        userDao.update(user);
        return "redirect:/";
    }
}
