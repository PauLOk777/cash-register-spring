package com.paulok777.controller;

import com.paulok777.dto.UserDTO;
import com.paulok777.entity.Role;
import com.paulok777.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class MainController {
    private final UserService userService;

    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("positions", Role.values());
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(UserDTO userDTO) {
        userService.saveNewUser(userDTO);
        return "redirect:/login";
    }
}
