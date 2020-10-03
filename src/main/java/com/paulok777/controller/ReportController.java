package com.paulok777.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/senior_cashier/reports")
public class ReportController {
    @GetMapping("/x")
    public String makeXReport() {
        return "redirect:/orders";
    }

    @GetMapping("/z")
    public String makeZReport() {
        return "redirect:/orders";
    }
}
