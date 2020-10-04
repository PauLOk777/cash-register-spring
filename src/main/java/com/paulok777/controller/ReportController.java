package com.paulok777.controller;

import com.paulok777.dto.ReportDTO;
import com.paulok777.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/senior_cashier/reports")
public class ReportController {
    private final OrderService orderService;

    @GetMapping("/x")
    public String makeXReport(Model model) {
        log.info("(username: {}) make X report", SecurityContextHolder.getContext().getAuthentication().getName());
        ReportDTO reportDTO = orderService.makeXReport();
        model.addAttribute("report", reportDTO);
        return "reports";
    }

    @GetMapping("/z")
    public String makeZReport(Model model) {
        log.info("(username: {}) make X report", SecurityContextHolder.getContext().getAuthentication().getName());
        ReportDTO reportDTO = orderService.makeZReport();
        model.addAttribute("report", reportDTO);
        return "reports";
    }
}
