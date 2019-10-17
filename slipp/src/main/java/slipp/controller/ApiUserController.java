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

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final UserDao userDao = new UserDao();

    @RequestMapping(value = "/api/users", method = RequestMethod.POST)
    public ModelAndView create(HttpServletRequest request, HttpServletResponse response) throws Exception {
        final UserCreatedDto createdDto = this.objectMapper.readValue(request.getInputStream(), UserCreatedDto.class);
        logger.debug("Created User : {}", createdDto);
        this.userDao.create(
                new User(
                        createdDto.getUserId(),
                        createdDto.getPassword(),
                        createdDto.getName(),
                        createdDto.getEmail()
                )
        );
        response.setHeader("Location", "/api/users?userId=" + createdDto.getUserId());
        response.setStatus(HttpStatus.CREATED.value());
        return new ModelAndView(new JsonView());
    }

    @RequestMapping(value = "/api/users", method = RequestMethod.GET)
    public ModelAndView show(HttpServletRequest request, HttpServletResponse response) throws Exception {
        final String userId = request.getParameter("userId");
        logger.debug("userId : {}", userId);
        return new ModelAndView(new JsonView()).addObject("user", this.userDao.findByUserId(userId));
    }

    @RequestMapping(value = "/api/users", method = RequestMethod.PUT)
    public ModelAndView update(HttpServletRequest request, HttpServletResponse response) throws Exception {
        final String userId = request.getParameter("userId");
        logger.debug("userId : {}", userId);
        final UserUpdatedDto updateDto = this.objectMapper.readValue(request.getInputStream(), UserUpdatedDto.class);
        logger.debug("Updated User : {}", updateDto);
        this.userDao.update(this.userDao.findByUserId(userId).update(updateDto));
        return new ModelAndView(new JsonView());
    }
}