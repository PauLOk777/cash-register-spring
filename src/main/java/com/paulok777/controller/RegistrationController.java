package com.paulok777.controller;

import com.paulok777.entity.User;
import com.paulok777.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/registration")
public class RegistrationController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public String registration() {
        return "registration";
    }

    @PostMapping
    public String addUser(User user, Model model) {
        User userFromDb = userRepository.findByName(user.getName());

        if (userFromDb != null) {
            model.addAttribute("errorMessage", "User exists!");
            return "registration";
        }

        return "redirect:/login";
    }
}
