package com.paulok777.controller;

import com.paulok777.dto.UserDTO;
import com.paulok777.entity.Role;
import com.paulok777.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@Log4j2
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("positions", Role.values());
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(@Validated UserDTO userDTO) {
        log.info("{}", userDTO);
        userService.saveNewUser(userDTO);
        return "redirect:/login";
    }
}
