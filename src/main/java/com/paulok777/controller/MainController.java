package com.paulok777.controller;

import com.paulok777.entity.Role;
import com.paulok777.entity.User;
import com.paulok777.repository.UserRepository;
import org.hibernate.mapping.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;

@Controller
public class MainController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public String greeting(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        return "greeting";
    }

    @GetMapping("/main")
    public String main(Model model) {
        Iterable<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "main";
    }

    @PostMapping("/main")
    public String addUser(@RequestParam String username, @RequestParam String password, @RequestParam String email) {
        System.out.println("HELLO");
        User user = new User(username, password, email);
        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        userRepository.save(user);
        return "redirect: ";
    }
}
