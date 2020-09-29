package com.paulok777.controller;

import com.paulok777.dto.UserDTO;
import com.paulok777.entity.Role;
import com.paulok777.entity.User;
import com.paulok777.repository.UserRepository;
import com.paulok777.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@Controller
public class RegistrationController {
    @Autowired
    private UserService userService;

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(UserDTO userDTO) {
        userService.saveNewUser(userDTO);
        return "redirect:/login";
    }
}
