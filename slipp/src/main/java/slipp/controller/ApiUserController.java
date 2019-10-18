package slipp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.mvc.JsonView;
import nextstep.mvc.ModelAndView;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.annotation.RequestMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import slipp.dao.UserDao;
import slipp.domain.User;
import slipp.dto.UserCreatedDto;
import slipp.dto.UserUpdatedDto;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class ApiUserController {
    private static final Logger logger = LoggerFactory.getLogger(ApiUserController.class);

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private final UserDao userDao;

    public ApiUserController(UserDao userDao) {
        this.userDao = userDao;
    }

    @RequestMapping(value = "/api/users", method = RequestMethod.POST)
    public ModelAndView create(HttpServletRequest req, HttpServletResponse res) throws Exception {
        final UserCreatedDto createdDto = this.objectMapper.readValue(req.getInputStream(), UserCreatedDto.class);
        this.userDao.create(
                new User(
                        createdDto.getUserId(),
                        createdDto.getPassword(),
                        createdDto.getName(),
                        createdDto.getEmail()
                )
        );
        logger.debug("Created User : {}", createdDto);
        res.setHeader("Location", "/api/users?userId=" + createdDto.getUserId());
        res.setStatus(HttpStatus.CREATED.value());
        return new ModelAndView(new JsonView());
    }

    @RequestMapping(value = "/api/users", method = RequestMethod.GET)
    public ModelAndView show(HttpServletRequest req, HttpServletResponse res) {
        final String userId = req.getParameter("userId");
        logger.debug("userId : {}", userId);
        return this.userDao.findByUserId(userId).map(user ->
            new ModelAndView(new JsonView()).addObject("user", user)
        ).orElseThrow(() -> new NullPointerException("사용자를 찾을 수 없습니다."));
    }

    @RequestMapping(value = "/api/users", method = RequestMethod.PUT)
    public ModelAndView update(HttpServletRequest req, HttpServletResponse res) throws Exception {
        final String userId = req.getParameter("userId");
        logger.debug("userId : {}", userId);
        final UserUpdatedDto updateDto = this.objectMapper.readValue(req.getInputStream(), UserUpdatedDto.class);
        return this.userDao.findByUserId(userId).map(user -> {
            logger.debug("Updated User : {}", updateDto);
            this.userDao.update(user.update(updateDto));
            return new ModelAndView(new JsonView());
        }).orElseThrow(() -> new NullPointerException("사용자를 찾을 수 없습니다."));
    }
}