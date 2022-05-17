package com.paulok777.controllers;

import com.paulok777.dto.ReportDto;
import com.paulok777.services.OrderService;
import com.paulok777.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/senior_cashier/reports")
public class ReportController {

    private static final String X_REPORTS = "/x";
    private static final String Z_REPORTS = "/z";

    private static final String REPORTS_PAGE = "reports";
    private static final String REPORT_ATTRIBUTE = "report";

    private final OrderService orderService;
    private final UserService userService;

    @GetMapping(X_REPORTS)
    public String makeXReport(Model model) {
        log.info("(username: {}) make X report", userService.getCurrentUser().getUsername());
        ReportDto reportDTO = orderService.makeXReport();
        model.addAttribute(REPORT_ATTRIBUTE, reportDTO);
        return REPORTS_PAGE;
    }

    @GetMapping(Z_REPORTS)
    public String makeZReport(Model model) {
        log.info("(username: {}) make X report", userService.getCurrentUser().getUsername());
        ReportDto reportDTO = orderService.makeZReport();
        model.addAttribute(REPORT_ATTRIBUTE, reportDTO);
        return REPORTS_PAGE;
    }
}
