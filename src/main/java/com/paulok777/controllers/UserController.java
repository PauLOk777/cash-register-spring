package com.paulok777.controllers;

import com.paulok777.dto.UserDto;
import com.paulok777.entities.Role;
import com.paulok777.services.UserService;
import com.paulok777.utils.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@Log4j2
@RequiredArgsConstructor
public class UserController {

    private static final String REGISTRATION = "/registration";
    private static final String LOGIN = "/login";

    private static final String REGISTRATION_PAGE = "registration";
    private static final String POSITIONS_ATTRIBUTE = "positions";
    private static final String REDIRECT_PREFIX = "redirect:";

    private final UserService userService;

    @GetMapping(REGISTRATION)
    public String registration(Model model) {
        model.addAttribute(POSITIONS_ATTRIBUTE, Role.values());
        return REGISTRATION_PAGE;
    }

    @PostMapping(REGISTRATION)
    public String addUser(UserDto userDTO) {
        log.info("{}", userDTO);
        Validator.validateUser(userDTO);
        userService.saveNewUser(userDTO);
        return REDIRECT_PREFIX + LOGIN;
    }
}
